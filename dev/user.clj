(ns user
  (:require
   #_{:clj-kondo/ignore [:unused-namespace :unused-referred-var]}
   [com.stuartsierra.component.user-helpers :refer [dev go]]))

(set! *warn-on-reflection* true)
