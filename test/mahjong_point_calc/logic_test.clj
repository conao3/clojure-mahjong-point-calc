(ns mahjong-point-calc.logic-test
  (:require
   [clojure.test :as t]
   [mahjong-point-calc.logic :as c.logic]
   [mahjong-point-calc.schema :as c.schema]))

(t/deftest tile-info-test
  (t/is (= {:number 1
            :type c.schema/tile-type-manzu}
           (c.logic/tile-info :1m)))

  (t/is (= {:number 4
            :type c.schema/tile-type-jihai}
           (c.logic/tile-info :4z))))

(t/deftest sort-tiles-test
  (t/is (= [:5m :6m :6m :7m :2p :3p :4p :6p :6p :5s :6s :7s :1z :4z]
           (c.logic/sort-tiles [:5s :4z :3p :6p :6p :5m :1z :7m :2p :4p :6m :6m :7s :6s]))))

(t/deftest parse-tiles
  (t/is (= [:5m :2s :8m]
           (c.logic/parse-tiles "5m2s8m")))

  (t/is (= [:5m :2s :8m :1m :5m :1z]
           (c.logic/parse-tiles "5m2s815m1z"))))

(t/deftest tiles->matrix-test
  (t/is (= [[0 0 0 0 2 2 2 0 0]
            [0 1 1 1 0 2 0 0 0]
            [0 0 0 0 1 1 1 0 0]
            [0 0 0 0 0 0 0 0 0]]
           (-> "55677m23466p567s6m"
               c.logic/parse-tiles
               c.logic/tiles->matrix))))

(t/deftest matrix->tiles-test
  (t/is (= (-> "55677m23466p567s6m"
               c.logic/parse-tiles
               c.logic/sort-tiles)
           (-> [[0 0 0 0 2 2 2 0 0]
                [0 1 1 1 0 2 0 0 0]
                [0 0 0 0 1 1 1 0 0]
                [0 0 0 0 0 0 0 0 0]]
               c.logic/matrix->tiles
               c.logic/sort-tiles))))

(t/deftest get-available-pairs-test
  (t/is (= [:6p]
           (-> "55677m23466p567s6m"
               c.logic/parse-tiles
               c.logic/get-available-pair)))

  (t/is (= [:2m :5m]
           (-> "2234455m234s234p3m"
               c.logic/parse-tiles
               c.logic/get-available-pair
               c.logic/sort-tiles))))

(t/deftest get-available-shuntsu-test
  (t/is (= [:1m :2m :3p]
           (-> "345p12334m2m"
               c.logic/parse-tiles
               c.logic/get-available-shuntsu)))

  (t/is (= nil
           (-> "345p12334m2m2m"
               c.logic/parse-tiles
               c.logic/get-available-shuntsu))))

(t/deftest get-available-kotsu-test
  (t/is (= [[]]
           (-> "55677m234p567s6m"
               c.logic/parse-tiles
               c.logic/get-available-kotsu)))

  (t/is (= [[:5m :6m]]
           (-> "55677m234p567s6m555m666m"
               c.logic/parse-tiles
               c.logic/get-available-kotsu)))

  (t/is (= [[:5m :5m :6m]]
           (-> "55677m234p567s6m555m666m555m"
               c.logic/parse-tiles
               c.logic/get-available-kotsu)))

  (t/is (= [[:2p]]
           (-> "233444556m222p"
               c.logic/parse-tiles
               c.logic/get-available-kotsu))))
