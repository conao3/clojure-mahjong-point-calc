(ns mahjong-point-calc.core
  (:require
   [com.stuartsierra.component :as component]
   [org.httpkit.server :as httpkit.server]
   [clojure.tools.logging :as log]))

(defn handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defrecord Server [server handler]
  component/Lifecycle
  (start [this]
    (log/info "Starting Server...")
    (let [server (httpkit.server/run-server handler)]
      (log/info "Started Server")
      (assoc this :server server)))

  (stop [this]
    (log/info "Stopping Server...")
    (server :timeout 1000)
    (log/info "Stopped Server")
    (assoc this :server nil)))

(defn new-system []
  (component/system-map
   :handler handler
   :server (component/using
            (map->Server {})
            [:handler])))
