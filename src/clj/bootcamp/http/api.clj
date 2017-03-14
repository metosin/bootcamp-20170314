(ns bootcamp.http.api
  (:require [compojure.api.sweet :refer [context] :as c]
            [bootcamp.domain.book :as book]
            [ring.swagger.coerce :as coerce]
            [compojure.api.middleware :as mw]
            [clojure.tools.logging :as log]
            [clojure.string :as str]))

;;
;; Coerce datomic entities to regular maps for JSON:
;;

(defn entity->map-coercion [data]
  (if (instance? datomic.Entity data)
    (into {:db/id (:db/id data)} data)
    data))

(defn coercion [schema]
  entity->map-coercion)

;;
;; API handler:
;;

(defn create-handler []
  (c/api
    {:coercion (mw/create-coercion (assoc-in mw/default-coercion-options [:response :default] coercion))
     :swagger {:ui "/api-docs"
               :spec "/swagger.json"
               :data {:info {:title "Bootcamp sample application"
                             :description "Compojure API example"}
                      :tags [{:name "book", :description "Book API"}]
                      :securityDefinitions {:api_key {:type "apiKey"
                                                      :name "x-apikey"
                                                      :in "header"}}}}}
    (context "/api" []
      (context "/book" []
        :tags ["book"]
        book/book-routes))))
