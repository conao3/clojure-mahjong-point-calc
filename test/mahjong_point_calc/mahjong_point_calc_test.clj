(ns mahjong-point-calc.mahjong-point-calc-test
  (:require
   [clojure.test :as t]
   [mahjong-point-calc.mahjong-point-calc :as c.mahjong-point-calc]))

(t/deftest add-test
  (t/testing "Adding 2 positive numbers"
    (t/is (= 3 (c.mahjong-point-calc/add 1 2)))))
