(ns dev
  (:require
   [clojure.main :as clojure.main]
   #_{:clj-kondo/ignore [:unused-referred-var]}
   [com.stuartsierra.component.repl :as component.repl :refer [system start stop reset initializer]]
   [mahjong-point-calc.system :as c.system]
   [malli.dev :as malli.dev]))

(apply require clojure.main/repl-requires)

(component.repl/set-init (fn [_] (c.system/new-system)))
(malli.dev/start!)
