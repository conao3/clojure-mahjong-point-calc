(ns mahjong-point-calc.logging
  (:import [org.slf4j LoggerFactory]))

(defn init-logging
  "Initialize logging for Native Image"
  []
  (try
    (LoggerFactory/getLogger "mahjong-point-calc")
    (catch Exception e
      (println "Failed to initialize logging:" (.getMessage e))
      nil)))
