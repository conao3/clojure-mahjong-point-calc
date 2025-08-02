(ns dev
  (:require
   [clojure.main :as clojure.main]
   #_{:clj-kondo/ignore [:unused-referred-var]}
   [com.stuartsierra.component.repl :as component.repl :refer [system start stop reset]]
   [mahjong-point-calc.core :as c.core]))

(component.repl/set-init (fn [_] (c.core/new-system)))
(apply require clojure.main/repl-requires)
