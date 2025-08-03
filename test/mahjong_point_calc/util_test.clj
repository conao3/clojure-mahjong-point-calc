(ns mahjong-point-calc.util-test
  (:require
   [clojure.test :as t]
   [mahjong-point-calc.util :as c.util]))

(t/deftest find-value-test
  (t/is (= {:char \m :value :manzu}
           (c.util/find-value [{:char \m :value :manzu}
                               {:char \k :value :kotsu}
                               {:char \s :value :souzu}
                               {:char \z :value :jihai}]
                              :char
                              \m)))

  (t/is (= nil
           (c.util/find-value [{:char \m :value :manzu}
                               {:char \k :value :kotsu}
                               {:char \s :value :souzu}
                               {:char \z :value :jihai}]
                              :char
                              \q))))

(t/deftest find-value-index-test
  (t/is (= [0 {:char \m :value :manzu}]
           (c.util/find-value-index [{:char \m :value :manzu}
                                     {:char \k :value :kotsu}
                                     {:char \s :value :souzu}
                                     {:char \z :value :jihai}]
                                    :char
                                    \m)))

  (t/is (= nil
           (c.util/find-value-index [{:char \m :value :manzu}
                                     {:char \k :value :kotsu}
                                     {:char \s :value :souzu}
                                     {:char \z :value :jihai}]
                                    :char
                                    \q))))

(t/deftest lpartial-test
  (t/is (= {:type 'a :value 2}
           ((c.util/lpartial update :value inc)
            {:type 'a
             :value 1}))))

(t/deftest power-set-test
  (t/is (= [[] [:5m] [:6m] [:5m :6m]]
           (c.util/power-set [:5m :6m]))))
