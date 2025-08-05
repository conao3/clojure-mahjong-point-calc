(ns mahjong-point-calc.yaku
  (:require
   [mahjong-point-calc.schema :as c.schema]
   [mahjong-point-calc.logic :as c.logic]
   [malli.experimental :as mx]))

;; 以下の役は別途確認する
;; 基本系列: 三槓子
;; 基本系列: 四槓子
;; 基本系列: 七対子
;; 全体系列: 国士無双
;; 全体系列: 国士無双13面待ち

(def hand-composition [:map
                       [:win-tile c.schema/tile]
                       [:pair c.schema/tile]
                       [:kotsu [:seqable [:seqable c.schema/tile]]]
                       [:shuntsu [:seqable [:seqable c.schema/tile]]]
                       [:kantsu {:optional true} [:seqable [:seqable c.schema/tile]]]
                       [:kokushi-tiles {:optional true} [:seqable c.schema/tile]]])

(mx/defn ^:private terminal-honor? :- :boolean
  [tile :- c.schema/tile]
  (let [{:keys [type number]} (c.logic/tile-info tile)]
    (or (= type c.schema/tile-type-jihai)
        (= number 1)
        (= number 9))))

(mx/defn ^:private terminal? :- :boolean
  [tile :- c.schema/tile]
  (let [{:keys [type number]} (c.logic/tile-info tile)]
    (and (not= type c.schema/tile-type-jihai)
         (or (= number 1) (= number 9)))))

(mx/defn ^:private honor? :- :boolean
  [tile :- c.schema/tile]
  (let [{:keys [type]} (c.logic/tile-info tile)]
    (= type c.schema/tile-type-jihai)))

(mx/defn toitoi? :- :boolean
  "基本系列: 対々和"
  [{:keys [_win-tile _pair _kotsu shuntsu]} :- hand-composition]
  (empty? shuntsu))

(mx/defn sanankou? :- :boolean
  "基本系列: 三暗刻"
  [{:keys [_win-tile _pair kotsu _shuntsu]} :- hand-composition]
  (>= (count kotsu) 3))

(mx/defn suuankou? :- :boolean
  "基本系列: 四暗刻"
  [{:keys [_win-tile _pair kotsu _shuntsu]} :- hand-composition]
  (>= (count kotsu) 4))

(mx/defn tanyao? :- :boolean
  "全体系列: 断么九"
  [{:keys [win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [win-tile pair] (flatten kotsu) (flatten shuntsu))]
    (not-any? terminal-honor? all-tiles)))

(mx/defn pinfu? :- :boolean
  "全体系列: 平和"
  [{:keys [_win-tile _pair kotsu shuntsu]} :- hand-composition]
  (and (empty? kotsu)
       (= (count shuntsu) 4)))

(mx/defn honitsu? :- :boolean
  "全体系列: 混一色"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu) (flatten shuntsu))
        types (set (map #(:type (c.logic/tile-info %)) all-tiles))
        number-types (disj types c.schema/tile-type-jihai)]
    (and (= (count number-types) 1)
         (contains? types c.schema/tile-type-jihai))))

(mx/defn chinitsu? :- :boolean
  "全体系列: 清一色"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu) (flatten shuntsu))
        types (set (map #(:type (c.logic/tile-info %)) all-tiles))]
    (and (= (count types) 1)
         (not (contains? types c.schema/tile-type-jihai)))))

(mx/defn chanta? :- :boolean
  "全体系列: 全帯幺九"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [has-terminal-honor? (fn [tiles]
                              (some terminal-honor? tiles))]
    (and (has-terminal-honor? [pair])
         (every? has-terminal-honor? kotsu)
         (every? has-terminal-honor? shuntsu))))

(mx/defn junchan? :- :boolean
  "全体系列: 純全帯么九"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [has-terminal? (fn [tiles]
                        (some terminal? tiles))]
    (and (has-terminal? [pair])
         (every? has-terminal? kotsu)
         (every? has-terminal? shuntsu))))

(mx/defn honroutou? :- :boolean
  "全体系列: 混老頭"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu))]
    (and (empty? shuntsu)
         (every? terminal-honor? all-tiles))))

(mx/defn chinroutou? :- :boolean
  "全体系列: 清老頭"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu))]
    (and (empty? shuntsu)
         (every? terminal? all-tiles))))

(mx/defn tsuuiisou? :- :boolean
  "全体系列: 字一色"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu))]
    (and (empty? shuntsu)
         (every? honor? all-tiles))))

(mx/defn ryuuiisou? :- :boolean
  "全体系列: 緑一色"
  [{:keys [_win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [pair] (flatten kotsu) (flatten shuntsu))
        green-tiles #{:2s :3s :4s :6s :8s :6z}]
    (every? green-tiles all-tiles)))

(mx/defn chuurenpoutou? :- :boolean
  "全体系列: 九連宝燈"
  [{:keys [win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [win-tile pair] (flatten kotsu) (flatten shuntsu))
        tile-counts (frequencies all-tiles)
        types (set (map #(:type (c.logic/tile-info %)) all-tiles))]
    (and (= (count types) 1)
         (not (contains? types c.schema/tile-type-jihai))
         (= (count all-tiles) 14)
         (let [type-keyword (first types)
               type-suffix (case type-keyword
                             :manzu "m"
                             :pinzu "p"
                             :souzu "s")
               base-pattern {(keyword (str "1" type-suffix)) 3
                             (keyword (str "2" type-suffix)) 1
                             (keyword (str "3" type-suffix)) 1
                             (keyword (str "4" type-suffix)) 1
                             (keyword (str "5" type-suffix)) 1
                             (keyword (str "6" type-suffix)) 1
                             (keyword (str "7" type-suffix)) 1
                             (keyword (str "8" type-suffix)) 1
                             (keyword (str "9" type-suffix)) 3}]
           (every? (fn [[expected-tile expected-count]]
                     (>= (get tile-counts expected-tile 0) expected-count))
                   base-pattern)))))

(mx/defn junsei-chuurenpoutou? :- :boolean
  "全体系列: 純正九連宝燈"
  [{:keys [win-tile pair kotsu shuntsu]} :- hand-composition]
  (let [all-tiles (concat [win-tile pair] (flatten kotsu) (flatten shuntsu))
        tile-counts (frequencies all-tiles)
        types (set (map #(:type (c.logic/tile-info %)) all-tiles))]
    (and (= (count types) 1)
         (not (contains? types c.schema/tile-type-jihai))
         (= (count all-tiles) 14)
         (let [type-keyword (first types)
               type-suffix (case type-keyword
                             :manzu "m"
                             :pinzu "p"
                             :souzu "s")
               pure-pattern {(keyword (str "1" type-suffix)) 3
                             (keyword (str "2" type-suffix)) 1
                             (keyword (str "3" type-suffix)) 1
                             (keyword (str "4" type-suffix)) 1
                             (keyword (str "5" type-suffix)) 1
                             (keyword (str "6" type-suffix)) 1
                             (keyword (str "7" type-suffix)) 1
                             (keyword (str "8" type-suffix)) 1
                             (keyword (str "9" type-suffix)) 3}
               win-tile-num (:number (c.logic/tile-info win-tile))]
           (= tile-counts (assoc pure-pattern win-tile (inc (get pure-pattern win-tile))))
           (or (= win-tile-num 1) (= win-tile-num 9))))))

(mx/defn iipeikou? :- :boolean
  "隣接系列: 一盃口"
  [{:keys [_win-tile _pair _kotsu shuntsu]} :- hand-composition]
  (let [shuntsu-freq (frequencies shuntsu)]
    (boolean (some #(>= % 2) (vals shuntsu-freq)))))

(mx/defn ryanpeikou? :- :boolean
  "隣接系列: 二盃口"
  [{:keys [_win-tile _pair kotsu shuntsu]} :- hand-composition]
  (let [shuntsu-freq (frequencies shuntsu)]
    (and (empty? kotsu)
         (= (count (filter #(>= % 2) (vals shuntsu-freq))) 2))))

(mx/defn sanshoku-doujun? :- :boolean
  "隣接系列: 三色同順"
  [{:keys [_win-tile _pair _kotsu shuntsu]} :- hand-composition]
  (let [shuntsu-info (map (fn [tiles]
                            (let [{:keys [number type]} (c.logic/tile-info (first tiles))]
                              {:number number :type type}))
                          shuntsu)]
    (boolean
     (some (fn [num]
             (let [types-with-num (set (map :type
                                            (filter #(= (:number %) num) shuntsu-info)))]
               (>= (count (disj types-with-num c.schema/tile-type-jihai)) 3)))
           (range 1 8)))))

(mx/defn sanshoku-doukou? :- :boolean
  "隣接系列: 三色同刻"
  [{:keys [_win-tile _pair kotsu _shuntsu]} :- hand-composition]
  (let [kotsu-info (map (fn [tiles]
                          (let [{:keys [number type]} (c.logic/tile-info (first tiles))]
                            {:number number :type type}))
                        kotsu)]
    (boolean
     (some (fn [num]
             (let [types-with-num (set (map :type
                                            (filter #(= (:number %) num) kotsu-info)))]
               (>= (count (disj types-with-num c.schema/tile-type-jihai)) 3)))
           (range 1 10)))))

(mx/defn ikkitsuukan? :- :boolean
  "隣接系列: 一気通貫"
  [{:keys [_win-tile _pair _kotsu shuntsu]} :- hand-composition]
  (let [shuntsu-by-type (group-by #(:type (c.logic/tile-info (first %))) shuntsu)]
    (boolean
     (some (fn [[type tiles]]
             (and (not= type c.schema/tile-type-jihai)
                  (let [starting-nums (set (map #(:number (c.logic/tile-info (first %))) tiles))]
                    (and (contains? starting-nums 1)
                         (contains? starting-nums 4)
                         (contains? starting-nums 7)))))
           shuntsu-by-type))))

(mx/defn shousuushii? :- :boolean
  "字牌系列: 小四喜"
  [{:keys [_win-tile pair kotsu _shuntsu]} :- hand-composition]
  (let [all-groups (concat [[pair pair pair]] kotsu)
        wind-tiles #{:1z :2z :3z :4z}
        wind-groups (filter #(wind-tiles (first %)) all-groups)]
    (= (count wind-groups) 4)))

(mx/defn daisuushii? :- :boolean
  "字牌系列: 大四喜"
  [{:keys [_win-tile _pair kotsu _shuntsu]} :- hand-composition]
  (let [wind-tiles #{:1z :2z :3z :4z}
        wind-kotsu (filter #(wind-tiles (first %)) kotsu)]
    (= (count wind-kotsu) 4)))

(mx/defn shousangen? :- :boolean
  "字牌系列: 小三元"
  [{:keys [_win-tile pair kotsu _shuntsu]} :- hand-composition]
  (let [all-groups (concat [[pair pair]] kotsu)
        dragon-tiles #{:5z :6z :7z}
        dragon-groups (filter #(dragon-tiles (first %)) all-groups)]
    (= (count dragon-groups) 3)))

(mx/defn daisangen? :- :boolean
  "字牌系列: 大三元"
  [{:keys [_win-tile _pair kotsu _shuntsu]} :- hand-composition]
  (let [dragon-tiles #{:5z :6z :7z}
        dragon-kotsu (filter #(dragon-tiles (first %)) kotsu)]
    (= (count dragon-kotsu) 3)))
