(ns bootcamp.http.server
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [immutant.web :as immutant]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.util.http-response :as resp]
            [bootcamp.http.api :as api]
            [bootcamp.http.index :as index]
            [clojure.string :as str]))

;;
;; Request/response logging middleware:
;;

(defn wrap-logging [handler]
  (fn [req]
    (try
      (when-let [resp (handler req)]
        (log/infof "%s %s -> %s"
                   (-> req :request-method name str/upper-case)
                   (-> req :uri)
                   (-> resp :status))
        resp)
      (catch Exception e
        (log/warnf e "%s %s -> error"
                   (-> req :request-method name str/upper-case)
                   (-> req :uri))
        (throw e)))))

;;
;; Serve static resources:
;;

(defn create-resources-handler []
  (fn [{:keys [request-method uri]}]
    (if (and (= request-method :get)
             (or (str/starts-with? uri "/js/")
                 (str/starts-with? uri "/css/")))
      (resp/resource-response uri {:root "/public"}))))

;;
;; HTTP handler:
;;

(defn create-handler []
  (some-fn (-> (api/create-handler)
               (wrap-defaults api-defaults)
               (wrap-logging))
           (-> (create-resources-handler)
               (wrap-content-type {:mime-types {"js" "application/javascript; charset=utf-8"}}))
           (index/create-handler)
           (constantly (resp/not-found "The will is strong, but the path is unknown"))))

;;
;; HTTP server:
;;

(defn start-server [{:keys [port host]}]
  (log/infof "Starting HTTP server at %s:%d" host port)
  (immutant/run (create-handler) {:host host
                                  :port port
                                  :path "/"}))

(defn stop-server [server]
  (when server
    (log/info "Stopping HTTP server")
    (immutant/stop)))

(mount/defstate server
  :start (start-server {:host "0.0.0.0" :port 8000})
  :stop (stop-server server))

