(ns mahjong-point-calc.core
  (:require
   [com.stuartsierra.component :as component]
   [org.httpkit.server :as httpkit.server]
   [clojure.tools.logging :as log]))

(defn handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defrecord Server [handler server server-stop-fn]
  component/Lifecycle
  (start [this]
    (log/info "Starting Server...")
    (let [ret (httpkit.server/run-server handler {:port 0})
          server (:server (meta ret))]
      (log/infof "Started Server at port: %d" (httpkit.server/server-port server))
      (->  this
           (assoc :server server)
           (assoc :server-stop-fn ret))))

  (stop [this]
    (log/info "Stopping Server...")
    (server-stop-fn :timeout 1000)
    (log/info "Stopped Server")
    (assoc this :server nil)))

(defn new-system []
  (component/system-map
   :handler handler
   :server (component/using
            (map->Server {})
            [:handler])))
