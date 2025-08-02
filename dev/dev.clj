(ns dev
  (:require
   #_{:clj-kondo/ignore [:unused-referred-var]}
   [com.stuartsierra.component.repl :as component.repl :refer [system start stop reset]]
   [mahjong-point-calc.core :as c.core]))

(component.repl/set-init (fn [_] (c.core/new-system)))
