(ns mahjong-point-calc.system.handler 
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]))

(defn handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defrecord Handler [handler]
  component/Lifecycle
  (start [this]
    (log/info "Starting Handler...")
    (log/info "Started Handler")
    (assoc this :handler mahjong-point-calc.system.handler/handler))

  (stop [this]
    (log/info "Stopping Handler...")
    (log/info "Stopped Handler")
    (assoc this :handler nil)))
