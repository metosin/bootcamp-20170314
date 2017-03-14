(ns bootcamp.core
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [bootcamp.db.migrate]
            [bootcamp.http.server]
            [bootcamp.nrepl]))

(defn start []
  (log/infof "Server starting...")
  (mount/start)
  (log/info "Server running."))

(defn stop []
  (log/info "Server stopping...")
  (mount/stop)
  (log/info "Server stopped."))
