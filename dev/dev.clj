(ns dev
  (:require
   [clojure.main :as clojure.main]
   #_{:clj-kondo/ignore [:unused-referred-var]}
   [com.stuartsierra.component.repl :as component.repl :refer [system start stop reset initializer]]
   [mahjong-point-calc.system :as c.system]
   [malli.dev :as malli.dev]
   [malli.dev.pretty :as malli.dev.pretty]))

(apply require clojure.main/repl-requires)

(component.repl/set-init (fn [_] (c.system/new-system)))
(malli.dev/start! {:report (malli.dev.pretty/reporter
                            (malli.dev.pretty/-printer
                             {:colors
                              ;; default: malli.dev.virhe.-dark-colors
                              {:title 33
                               :title-dark 28
                               :text 238
                               :link 21
                               :string 136
                               :constant 91
                               :type 30
                               :error 160}}))})
