(ns mahjong-point-calc.system.server
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [org.httpkit.server :as httpkit.server]))

(defrecord Server [config handler server server-stop-fn]
  component/Lifecycle
  (start [this]
    (log/info "Starting Server...")
    (let [ret (httpkit.server/run-server
               (:handler handler)
               {:port (:server/port (:config config))})
          server (:server (meta ret))]
      (log/info "Started Server at port:" (httpkit.server/server-port server))
      (->  this
           (assoc :server server)
           (assoc :server-stop-fn ret))))

  (stop [this]
    (log/info "Stopping Server...")
    (when server-stop-fn
      (server-stop-fn :timeout 1000))
    (log/info "Stopped Server")
    (-> this
        (assoc :server nil)
        (assoc :server-stop-fn nil))))
