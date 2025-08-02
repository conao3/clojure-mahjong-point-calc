(ns mahjong-point-calc.system.config
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [lambdaisland.config :as config]))

(defrecord Config [config]
  component/Lifecycle
  (start [this]
    (log/info "Starting Config...")
    (let [c (config/create {:prefix "mahjong-point-calc"})
          _ (config/get c :server/port)
          config (-> (config/values c)
                     (update :server/port parse-long))]
      (log/info "Started Config:" (pr-str config))
      (assoc this :config config)))

  (stop [this]
    (log/info "Stopping Config...")
    (log/info "Stopped Config")
    (assoc this :config nil)))
