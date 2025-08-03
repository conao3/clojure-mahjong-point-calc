(ns mahjong-point-calc.util 
  (:require
   [malli.experimental :as mx]))

(mx/defn find-value :- [:maybe :any]
  [coll :- [:seqable :any]
   access-fn :- ifn?
   value :- :any]
  (->> coll
       (filter #(= (access-fn %) value))
       first))

(mx/defn find-value-index :- [:maybe [:tuple :int :any]]
  [coll :- [:seqable :any]
   access-fn :- ifn?
   value :- :any]
  (->> coll
       (map-indexed (fn [inx val] [inx val]))
       (filter (fn [[_ val]] (= (access-fn val) value)))
       first))
