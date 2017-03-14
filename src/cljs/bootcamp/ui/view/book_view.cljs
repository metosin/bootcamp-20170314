(ns bootcamp.ui.view.book-view
  (:require [reagent.core :as r]
            [promesa.core :as p]
            [bootcamp.ui.db :as db]
            [bootcamp.ui.routing :as routing]
            [bootcamp.ui.http :as http]))

(defn load-book [id]
  (-> (http/get (str "/api/book/" id))
      (p/then (fn [book]
                (swap! db/app-state assoc-in [:views :book-view] book)))
      (p/catch (fn [error] (js/console.warn "oh no:" error)))))

(defn book-view [{:keys [id]}]
  (r/with-let [_ (load-book id)]
    (let [book (-> @db/app-state :views :book-view)]
      [:div
       [:h1 (-> book :book/title)]
       [:div (-> book :book/langs)]
       [:div (-> book :book/pages)]
       [:dic (-> book :book/authors)]])))

(defmethod routing/render-view :book-view [view]
  [book-view {:id (-> view :route-params :id)}])
