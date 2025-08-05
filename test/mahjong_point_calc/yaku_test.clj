(ns mahjong-point-calc.yaku-test
  (:require
   [clojure.test :as t]
   [mahjong-point-calc.yaku :as c.yaku]))

(t/deftest toitoi?-test
  (t/is (true? (c.yaku/toitoi? {:win-tile :7p
                                :pair :4s
                                :kotsu [[:1m :1m :1m] [:4s :4s :4s] [:2z :2z :2z] [:8p :8p :8p]]
                                :shuntsu []})))
  (t/is (false? (c.yaku/toitoi? {:win-tile :7p
                                 :pair :4s
                                 :kotsu [[:1m :1m :1m] [:4s :4s :4s]]
                                 :shuntsu [[:2m :3m :4m]]}))))

(t/deftest sanankou?-test
  (t/is (true? (c.yaku/sanankou? {:win-tile :7p
                                  :pair :4s
                                  :kotsu [[:1m :1m :1m] [:4s :4s :4s] [:2z :2z :2z] [:8p :8p :8p]]
                                  :shuntsu [[:2m :3m :4m]]})))
  (t/is (false? (c.yaku/sanankou? {:win-tile :7p
                                   :pair :4s
                                   :kotsu [[:1m :1m :1m] [:4s :4s :4s]]
                                   :shuntsu [[:2m :3m :4m] [:5s :6s :7s]]}))))

(t/deftest suuankou?-test
  (t/is (true? (c.yaku/suuankou? {:win-tile :7p
                                  :pair :4s
                                  :kotsu [[:1m :1m :1m] [:4s :4s :4s] [:2z :2z :2z] [:8p :8p :8p]]
                                  :shuntsu []}))))

(t/deftest tanyao?-test
  (t/is (true? (c.yaku/tanyao? {:win-tile :5p
                                :pair :4s
                                :kotsu [[:3m :3m :3m] [:5s :5s :5s]]
                                :shuntsu [[:3m :4m :5m] [:6p :7p :8p]]})))
  (t/is (false? (c.yaku/tanyao? {:win-tile :1p
                                 :pair :4s
                                 :kotsu [[:3m :3m :3m] [:5s :5s :5s]]
                                 :shuntsu [[:2m :3m :4m] [:6p :7p :8p]]}))))

(t/deftest pinfu?-test
  (t/is (true? (c.yaku/pinfu? {:win-tile :5p
                               :pair :4s
                               :kotsu []
                               :shuntsu [[:2m :3m :4m] [:6p :7p :8p] [:3s :4s :5s] [:1m :2m :3m]]}))))

(t/deftest honitsu?-test
  (t/is (true? (c.yaku/honitsu? {:win-tile :1m
                                 :pair :2m
                                 :kotsu [[:3m :3m :3m] [:5z :5z :5z]]
                                 :shuntsu [[:4m :5m :6m] [:7m :8m :9m]]}))))

(t/deftest chinitsu?-test
  (t/is (true? (c.yaku/chinitsu? {:win-tile :1m
                                  :pair :2m
                                  :kotsu [[:3m :3m :3m] [:9m :9m :9m]]
                                  :shuntsu [[:4m :5m :6m] [:7m :8m :9m]]}))))

(t/deftest iipeikou?-test
  (t/is (true? (c.yaku/iipeikou? {:win-tile :1m
                                  :pair :2m
                                  :kotsu []
                                  :shuntsu [[:1m :2m :3m] [:1m :2m :3m] [:4s :5s :6s] [:7p :8p :9p]]}))))

(t/deftest ryanpeikou?-test
  (t/is (true? (c.yaku/ryanpeikou? {:win-tile :1m
                                    :pair :9s
                                    :kotsu []
                                    :shuntsu [[:1m :2m :3m] [:1m :2m :3m] [:4s :5s :6s] [:4s :5s :6s]]})))
  (t/is (false? (c.yaku/ryanpeikou? {:win-tile :1m
                                     :pair :9s
                                     :kotsu [[:7p :7p :7p]]
                                     :shuntsu [[:1m :2m :3m] [:1m :2m :3m] [:4s :5s :6s]]}))))

(t/deftest sanshoku-doujun?-test
  (t/is (true? (c.yaku/sanshoku-doujun? {:win-tile :1m
                                         :pair :2m
                                         :kotsu [[:9m :9m :9m]]
                                         :shuntsu [[:1m :2m :3m] [:1s :2s :3s] [:1p :2p :3p]]}))))

(t/deftest sanshoku-doukou?-test
  (t/is (true? (c.yaku/sanshoku-doukou? {:win-tile :1m
                                         :pair :9s
                                         :kotsu [[:3m :3m :3m] [:3p :3p :3p] [:3s :3s :3s] [:7z :7z :7z]]
                                         :shuntsu []})))
  (t/is (false? (c.yaku/sanshoku-doukou? {:win-tile :1m
                                          :pair :9s
                                          :kotsu [[:3m :3m :3m] [:3p :3p :3p] [:5s :5s :5s]]
                                          :shuntsu [[:1m :2m :3m]]}))))

(t/deftest ikkitsuukan?-test
  (t/is (true? (c.yaku/ikkitsuukan? {:win-tile :5s
                                     :pair :5s
                                     :kotsu [[:9s :9s :9s]]
                                     :shuntsu [[:1m :2m :3m] [:4m :5m :6m] [:7m :8m :9m]]}))))

(t/deftest chanta?-test
  (t/is (true? (c.yaku/chanta? {:win-tile :1m
                                :pair :9p
                                :kotsu [[:1s :1s :1s] [:9m :9m :9m] [:5z :5z :5z]]
                                :shuntsu [[:1m :2m :3m]]}))))

(t/deftest junchan?-test
  (t/is (true? (c.yaku/junchan? {:win-tile :1m
                                 :pair :9p
                                 :kotsu [[:1s :1s :1s] [:9m :9m :9m]]
                                 :shuntsu [[:1m :2m :3m] [:7p :8p :9p]]}))))

(t/deftest honroutou?-test
  (t/is (true? (c.yaku/honroutou? {:win-tile :1m
                                   :pair :9p
                                   :kotsu [[:1s :1s :1s] [:9m :9m :9m] [:1z :1z :1z] [:5z :5z :5z]]
                                   :shuntsu []}))))

(t/deftest chinroutou?-test
  (t/is (true? (c.yaku/chinroutou? {:win-tile :1m
                                    :pair :9p
                                    :kotsu [[:1s :1s :1s] [:9m :9m :9m] [:1p :1p :1p] [:9s :9s :9s]]
                                    :shuntsu []}))))

(t/deftest tsuuiisou?-test
  (t/is (true? (c.yaku/tsuuiisou? {:win-tile :1z
                                   :pair :2z
                                   :kotsu [[:3z :3z :3z] [:5z :5z :5z] [:6z :6z :6z] [:7z :7z :7z]]
                                   :shuntsu []}))))

(t/deftest ryuuiisou?-test
  (t/is (true? (c.yaku/ryuuiisou? {:win-tile :2s
                                   :pair :6z
                                   :kotsu [[:3s :3s :3s] [:6s :6s :6s] [:8s :8s :8s]]
                                   :shuntsu [[:2s :3s :4s]]}))))

(t/deftest chuurenpoutou?-test
  (t/is (true? (c.yaku/chuurenpoutou? {:win-tile :2m
                                       :pair :2m
                                       :kotsu [[:1m :1m :1m] [:9m :9m :9m]]
                                       :shuntsu [[:3m :4m :5m] [:6m :7m :8m]]})))
  (t/is (false? (c.yaku/chuurenpoutou? {:win-tile :5m
                                        :pair :5m
                                        :kotsu [[:2m :2m :2m]]
                                        :shuntsu [[:3m :4m :5m]]}))))

(t/deftest junsei-chuurenpoutou?-test
  (t/is (true? (c.yaku/junsei-chuurenpoutou? {:win-tile :1m
                                              :pair :2m
                                              :kotsu [[:1m :1m :1m] [:9m :9m :9m]]
                                              :shuntsu [[:3m :4m :5m] [:6m :7m :8m]]})))
  (t/is (false? (c.yaku/junsei-chuurenpoutou? {:win-tile :5m
                                               :pair :5m
                                               :kotsu [[:2m :2m :2m]]
                                               :shuntsu [[:3m :4m :5m]]}))))

(t/deftest shousuushii?-test
  (t/is (true? (c.yaku/shousuushii? {:win-tile :5m
                                     :pair :1z
                                     :kotsu [[:2z :2z :2z] [:3z :3z :3z] [:4z :4z :4z]]
                                     :shuntsu [[:5m :6m :7m]]}))))

(t/deftest daisuushii?-test
  (t/is (true? (c.yaku/daisuushii? {:win-tile :5m
                                    :pair :5m
                                    :kotsu [[:1z :1z :1z] [:2z :2z :2z] [:3z :3z :3z] [:4z :4z :4z]]
                                    :shuntsu []}))))

(t/deftest shousangen?-test
  (t/is (true? (c.yaku/shousangen? {:win-tile :1m
                                    :pair :5z
                                    :kotsu [[:6z :6z :6z] [:7z :7z :7z] [:1m :1m :1m]]
                                    :shuntsu [[:2m :3m :4m]]}))))

(t/deftest daisangen?-test
  (t/is (true? (c.yaku/daisangen? {:win-tile :1m
                                   :pair :1m
                                   :kotsu [[:5z :5z :5z] [:6z :6z :6z] [:7z :7z :7z] [:2m :2m :2m]]
                                   :shuntsu []}))))
