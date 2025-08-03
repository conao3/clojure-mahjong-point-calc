(ns test
  (:require
   [malli.dev :as malli.dev]
   [malli.dev.pretty :as malli.dev.pretty]))

(set! *warn-on-reflection* true)

(malli.dev/start! {:report (malli.dev.pretty/thrower)})
