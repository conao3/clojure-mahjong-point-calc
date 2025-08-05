(ns mahjong-point-calc.logic
  (:require
   [mahjong-point-calc.schema :as c.schema]
   [mahjong-point-calc.util :as c.util]
   [malli.experimental :as mx]))

(def tile-types
  [{:char \m :value c.schema/tile-type-manzu}
   {:char \p :value c.schema/tile-type-pinzu}
   {:char \s :value c.schema/tile-type-souzu}
   {:char \z :value c.schema/tile-type-jihai}])

(def number-types
  [{:char \1 :value 1}
   {:char \2 :value 2}
   {:char \3 :value 3}
   {:char \4 :value 4}
   {:char \5 :value 5}
   {:char \6 :value 6}
   {:char \7 :value 7}
   {:char \8 :value 8}
   {:char \9 :value 9}])

(mx/defn inx->tile :- c.schema/tile
  [t :- :int
   n :- :int]
  (keyword (str (inc n) (-> (nth tile-types t) :char))))

(mx/defn tile-info :- [:map
                       [:type c.schema/tile-type]
                       [:number :int]]
  [tile :- c.schema/tile]
  (let [[n t] (name tile)]
    {:type (-> (c.util/find-value tile-types :char t) :value)
     :number (-> (c.util/find-value number-types :char n) :value)}))

(mx/defn tile-inxes :- [:map
                        [:type :int]
                        [:number :int]]
  [tile :- c.schema/tile]
  (let [[n t] (name tile)]
    {:type (-> (c.util/find-value-index tile-types :char t) first)
     :number (-> (c.util/find-value-index number-types :char n) first)}))

(mx/defn sort-tiles :- [:seqable c.schema/tile]
  [tiles :- [:seqable c.schema/tile]]
  (let [type-order {c.schema/tile-type-manzu 0
                    c.schema/tile-type-pinzu 1
                    c.schema/tile-type-souzu 2
                    c.schema/tile-type-jihai 3}]
    (->> tiles
         (sort-by #(let [info (tile-info %)]
                     [(type-order (:type info)) (:number info)])))))

(mx/defn parse-tiles :- [:seqable c.schema/tile]
  [inpt :- :string]
  (loop [result []
         nums []
         inpt inpt]
    (if inpt
      (let [cur (first inpt)]
        (if (#{\1 \2 \3 \4 \5 \6 \7 \8 \9} cur)
          (recur result (conj nums cur) (next inpt))
          (recur (->> nums
                      (reduce (fn [acc val]
                                (conj acc (keyword (str val cur))))
                              result))
                 []
                 (next inpt))))
      result)))

(def tile-counts `[:tuple ~@(repeat 9 :int)])

(mx/defn tiles->matrix :- `[:tuple ~@(repeat 4 tile-counts)]
  [tiles :- [:seqable c.schema/tile]]
  (->> tiles
       (reduce (fn [acc val]
                 (let [inxes (tile-inxes val)]
                   (update-in acc ((juxt :type :number) inxes) inc)))
               (vec (repeat 4 (vec (repeat 9 0)))))))

(mx/defn matrix->tiles :- [:seqable c.schema/tile]
  [matrix :- `[:tuple ~@(repeat 4 tile-counts)]]
  (for [[i counts] (map-indexed vector matrix)
        [j count] (map-indexed vector counts)
        _ (range count)]
    (inx->tile i j)))

(mx/defn get-available-pair :- [:seqable c.schema/tile]
  [tiles :- [:seqable c.schema/tile]]
  (->> tiles
       frequencies
       (keep (fn [[k v]] (when (<= 2 v) k)))
       (map (fn [candidate]
              [candidate
               (-> tiles
                   tiles->matrix
                   (update-in ((juxt :type :number) (tile-inxes candidate)) - 2)
                   ;; m, p, sは刻子と順子を構成する
                   ((fn [matrix]
                      (->> (for [i (range 3) j (range 7)] [i j]) ; 8, 9, 10の順子は構成できない
                           (reduce (fn [acc [i j]]
                                     (let [cnt (get-in acc [i j])]
                                       ;; まず対象の牌を0にする
                                       ;; mod 3した数を次と次の次の牌から引く
                                       ;; まず刻子を抜き出した後、順子を抜き出す操作に相当する
                                       (-> acc
                                           (assoc-in [i j] 0)
                                           (update-in [i (inc j)] - (mod cnt 3))
                                           (update-in [i (inc (inc j))] - (mod cnt 3)))))
                                   matrix))))
                   ;; zは刻子のみ
                   ((fn [matrix]
                      (let [i 3]
                        (->> (for [j (range 9)] [i j])
                             (reduce (fn [acc [i j]]
                                       (-> acc
                                           (update-in [i j] mod 3)))
                                     matrix))))))]))
       (keep (fn [[pair matrix]]
               (when (->> matrix (every? (partial every? zero?))) pair)))))

(mx/defn get-available-shuntsu :- [:maybe [:seqable c.schema/tile]]
  [tiles :- [:seqable c.schema/tile]]
  (-> tiles
      tiles->matrix
      ((fn [matrix]
         (->> (for [i (range 3) j (range 7)] [i j])
              (reduce (fn [[acc mat] [i j]]
                        (let [cnt (get-in mat [i j])]
                          [(cond-> acc
                             (pos? cnt) (conj (inx->tile i j)))
                           (cond-> mat
                             ;; まず対象の牌を0にする
                             ;; その数を次と次の次の牌から引く
                             ;; 順子を抜き出す操作に相当する
                             (pos? cnt) (-> (assoc-in [i j] 0)
                                            (update-in [i (inc j)] - cnt)
                                            (update-in [i (inc (inc j))] - cnt)))]))
                      [[] matrix]))))
      ((fn [[shuntsu matrix]]
         (when (->> matrix (every? (partial every? zero?)))
           shuntsu)))))

(mx/defn get-available-kotsu :- [:seqable [:seqable c.schema/tile]]
  [tiles :- [:seqable c.schema/tile]]
  (->> tiles
       frequencies
       (keep (fn [[k v]] (when (<= 3 v) [k (quot v 3)])))
       (mapcat (fn [[k n]] (repeat n k)))
       sort-tiles
       c.util/power-set
       (map (fn [candidate-lst]
              [candidate-lst
               (->> candidate-lst
                    (reduce
                     (fn [acc val]
                       (-> acc (update-in ((juxt :type :number) (tile-inxes val)) - 3)))
                     (tiles->matrix tiles))
                    matrix->tiles
                    get-available-shuntsu)]))
       (filter second)
       (map first)))

(mx/defn is-kanchan :- [:maybe c.schema/tile]
  [win-tile :- c.schema/tile
   shuntsu-start-tiles :- [:seqable c.schema/tile]]
  (->> shuntsu-start-tiles
       (some (fn [s]
               (when (= (-> win-tile
                            tile-inxes
                            (select-keys [:type :number])
                            (update :number dec))
                        (-> s tile-inxes (select-keys [:type :number])))
                 s)))))

(mx/defn is-penchan :- [:maybe c.schema/tile]
  [win-tile :- c.schema/tile
   pair-tile :- c.schema/tile]
  (when (= (-> win-tile tile-inxes (select-keys [:type :number]))
           (-> pair-tile tile-inxes (select-keys [:type :number])))
    pair-tile))

;; (mx/defn get-all-available-mentsu :- [:seqable c.schema/tile]
;;   [hand :- c.schema/hand
;;    win-tile :- c.schema/tile]
;;   nil)

(defn calc []
  ;; 成立可能な全ての面子構成を列挙する
  ;; それぞれに対し、以下の計算を行い、それぞれの面子構成での基本点を求める
  ;;   - 符の計算
  ;;   - 飜数の計算
  ;; 最大の基本点をその牌姿の基本点として採用する
  ;; 各自の負担点を計算する
  )
