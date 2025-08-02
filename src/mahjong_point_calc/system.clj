(ns mahjong-point-calc.system
  (:require
   [com.stuartsierra.component :as component]
   [mahjong-point-calc.system.config :as c.system.config]
   [mahjong-point-calc.system.handler :as c.system.handler]
   [mahjong-point-calc.system.server :as c.system.server]))

(defn new-system []
  (component/system-map
   :config (c.system.config/map->Config {})
   :handler (c.system.handler/map->Handler {})
   :server (component/using
            (c.system.server/map->Server {})
            [:config :handler])))
