(ns mahjong-point-calc.logic
  (:require
   [malli.experimental :as mx]
   [mahjong-point-calc.schema :as c.schema]))

(def char->number
  {\1 1
   \2 2
   \3 3
   \4 4
   \5 5
   \6 6
   \7 7
   \8 8
   \9 9})

(def number->char (->> char->number
                       (map (fn [[k v]] [v k]))
                       (into {})))

(def char->tile-type
  {\m c.schema/tile-type-manzu
   \p c.schema/tile-type-pinzu
   \s c.schema/tile-type-souzu
   \z c.schema/tile-type-jihai})

(def tile-type->char (->> char->tile-type
                          (map (fn [[k v]] [v k]))
                          (into {})))

(mx/defn tile-info :- [:map
                       [:num :int]
                       [:type c.schema/tile-type]]
  [tile :- c.schema/tile]
  (let [[n t] (name tile)]
    {:num (char->number n)
     :type (char->tile-type t)}))

(mx/defn sort-tiles :- [:seqable c.schema/tile]
  [tiles :- [:vector c.schema/tile]]
  (let [type-order {c.schema/tile-type-manzu 0
                    c.schema/tile-type-pinzu 1
                    c.schema/tile-type-souzu 2
                    c.schema/tile-type-jihai 3}]
    (->> tiles
         (sort-by #(let [info (tile-info %)]
                     [(type-order (:type info)) (:num info)])))))

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

;; (mx/defn get-all-available-mentsu :- [:vector c.schema/tile]
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
