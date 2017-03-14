(ns bootcamp.nrepl
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]
            [mount.core :as mount]))

(defn start-nrepl [{:keys [host port]}]
  (when (and host port)
    (log/infof "staring nrepl on %s:%d..." host port)
    (nrepl/start-server :bind host :port port)))

(defn stop-nrepl [nrepl]
  (when nrepl
    (log/info "stopping nrepl server...")
    (nrepl/stop-server nrepl)))

(mount/defstate nrepl :start (start-nrepl {:host "127.0.0.1" :port 6000})
                      :stop (stop-nrepl nrepl))
