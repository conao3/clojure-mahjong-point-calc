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
                   (update-in acc ((juxt :type :number) inxes) (fnil inc 0))))
               (into [] (repeat 4 (into [] (repeat 9 0)))))))

(mx/defn get-available-pair :- [:seqable c.schema/tile]
  [tiles :- [:seqable c.schema/tile]]
  (let [tile-matrix (tiles->matrix tiles)]
    (->> tiles
         frequencies
         (keep (fn [[k v]] (when (<= 2 v) k)))
         (map (fn [candidate]
                (let [inxes (tile-inxes candidate)
                      matrix (atom (update-in tile-matrix ((juxt :type :number) inxes) - 2))]
                  ;; m, p, sは刻子と順子を構成する
                  (doseq [i (range 3)]
                    (doseq [j (range 9)]
                      (let [cnt (get-in @matrix [i j])]
                        (when (pos? cnt)
                          ;; まず対象の牌を0にする
                          ;; mod 3した数を次と次の次の牌から引く
                          ;; まず刻子を抜き出した後、順子を抜き出す操作に相当する
                          (swap! matrix assoc-in [i j] 0)
                          (swap! matrix update-in [i (inc j)] - (mod cnt 3))
                          (swap! matrix update-in [i (inc (inc j))] - (mod cnt 3))))))
                  ;; zは刻子のみ
                  (let [i 3]
                    (doseq [j (range 9)]
                      (when (pos? (get-in @matrix [i j]))
                        (swap! matrix update-in [i j] mod 3))))
                  [candidate @matrix])))
         (keep (fn [[pair matrix]]
                 (when (->> matrix (every? (partial every? zero?))) pair))))))

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
