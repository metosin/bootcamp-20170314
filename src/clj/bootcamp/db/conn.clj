(ns bootcamp.db.conn
  (:require [clojure.tools.logging :as log]
            [clojure.string :as str]
            [mount.core :as mount]
            [datomic.api :as d]))

(defn start [uri]
  (log/infof "connecting to datomic: uri=%s" uri)
  (d/create-database uri)
  (d/connect uri))

(defn stop [conn]
  (when conn
    (log/info "disconnecting from Datomic")
    (d/release conn))
  nil)

(mount/defstate conn
  :start (start "datomic:mem://bootcamp")
  :stop (stop conn))


(comment

  ;
  ; Nuke DB, careful now...
  ; -------------------------
  (let [uri "datomic:free://localhost:4334/bootcamp"]
    (require 'bootcamp.db.migrate)
    (mount/stop #'bootcamp.db.conn/conn #'bootcamp.db.migrate/schema)
    (d/delete-database uri)
    (mount/start #'bootcamp.db.conn/conn #'bootcamp.db.migrate/schema))

  )
