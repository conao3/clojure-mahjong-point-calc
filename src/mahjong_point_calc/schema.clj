(ns mahjong-point-calc.schema)

(def tile [:enum
           ;; 萬子
           :1m :2m :3m :4m :5m :6m :7m :8m :9m
           ;; 筒子
           :1p :2p :3p :4p :5p :6p :7p :8p :9p
           ;; 索子
           :1s :2s :3s :4s :5s :6s :7s :8s :9s
           ;; 字牌 (東南西北白發中)
           :1z :2z :3z :4z :5z :6z :7z])

(def tile-type-manzu :manzu)
(def tile-type-pinzu :pinzu)
(def tile-type-souzu :souzu)
(def tile-type-jihai :jihai)
(def tile-type [:enum
                tile-type-manzu
                tile-type-pinzu
                tile-type-souzu
                tile-type-jihai])

;; 順子, 刻子, 槓子
(def meld-type-shuntsu :shuntsu)
(def meld-type-kotsu :kotsu)
(def meld-type-kantsu :kantsu)
(def meld-type [:enum
                meld-type-shuntsu
                meld-type-kotsu
                meld-type-kantsu])

(def hand-tiles `[:tuple ~@(repeat 13 tile)]) ; 13枚の手牌
(def meld-tiles [:tuple tile tile tile])
(def pair-tiles [:tuple tile tile])

(def win-hand [:map
               [:melds [:tuple meld-tiles meld-tiles meld-tiles meld-tiles]
                :pair pair-tiles
                :win-tile tile]])
