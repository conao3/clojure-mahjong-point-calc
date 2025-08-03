(ns mahjong-point-calc.util 
  (:require
   [malli.experimental :as mx]))

(mx/defn find-value :- [:maybe :any]
  [coll :- [:seqable :any]
   access-fn :- ifn?
   value :- :any]
  (->> coll
       (some #(when (= (access-fn %) value) %))))

(mx/defn find-value-index :- [:maybe [:tuple :int :any]]
  [coll :- [:seqable :any]
   access-fn :- ifn?
   value :- :any]
  (->> coll
       (map-indexed vector)
       (some (fn [[inx val]] (when (= (access-fn val) value) [inx val])))))

(mx/defn lpartial :- [:=> [:cat :any] :any]
  [f :- ifn?
   & args :- [:* :any]]
  (fn [x]
    (apply f x args)))

(mx/defn power-set :- [:seqable [:seqable :any]]
  [coll :- [:seqable :any]]
  (->> coll
       (reduce (fn [acc x] (into acc (map (lpartial conj x) acc))) [[]])))
