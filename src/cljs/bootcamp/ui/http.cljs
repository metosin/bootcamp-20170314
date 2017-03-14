(ns bootcamp.ui.http
  (:refer-clojure :exclude [get])
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [promesa.core :as p]))

(defn get [uri & [params]]
  (p/promise (fn [resolve reject]
               (go
                 (let [{:keys [status body]} (<! (http/get uri {:query-params params
                                                                :headers {"Accept" "application/edn"}}))]
                   (if (= status 200)
                     (resolve body)
                     (reject (js/Error. (str status ": " body)))))))))
