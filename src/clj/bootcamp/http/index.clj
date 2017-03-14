(ns bootcamp.http.index
  (:require [ring.util.http-response :as response]
            [hiccup.core :as h]
            [hiccup.page :as p]))

(def index-response
  (-> (h/html
        (p/html5
          [:head
           [:title "Bootcamp"]
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           (p/include-css "/css/style.css")]
          [:body
           [:div#app
            [:div.loading
             [:h1 "Be patient, the application is loading..."]]]
           [:div#dev]
           (p/include-js "/js/main.js")]))
      (response/ok)
      (response/content-type "text/html; charset=utf-8")))

(defn reserved-uri [uri]
  (re-matches #"\/(api|js|css)\/.*" uri))

(defn create-handler []
  (fn [request]
    (if (and (-> request :request-method (= :get))
             (-> request :uri reserved-uri not))
      index-response)))
