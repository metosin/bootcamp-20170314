(ns bootcamp.db.migrate
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [bootcamp.db.conn :refer [conn]]
            [io.rkn.conformity :as conformity]))

(defn migrate-db [conn]
  (let [norms (conformity/read-resource "db/schema.edn")
        migrations (conformity/ensure-conforms conn norms)]
    (log/infof "migrated %d norms" (count migrations))
    (doseq [{:keys [norm-name tx-index]} migrations]
      (log/tracef "   %s (tx-index: %s)" norm-name tx-index))))

(mount/defstate schema :start (migrate-db conn))
