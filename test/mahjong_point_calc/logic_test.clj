(ns mahjong-point-calc.logic-test
  (:require
   [clojure.test :as t]
   [mahjong-point-calc.logic :as c.logic]
   [mahjong-point-calc.schema :as c.schema]))

(t/deftest char->number-test
  (t/is (= 1 (c.logic/char->number \1)))
  (t/is (= 3 (c.logic/char->number \3))))

(t/deftest char->tile-type-test
  (t/is (= c.schema/tile-type-manzu (c.logic/char->tile-type \m)))
  (t/is (= c.schema/tile-type-jihai (c.logic/char->tile-type \z))))

(t/deftest tile-info-test
  (t/is (= {:num 1
            :type c.schema/tile-type-manzu}
           (c.logic/tile-info :1m)))

  (t/is (= {:num 4
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
