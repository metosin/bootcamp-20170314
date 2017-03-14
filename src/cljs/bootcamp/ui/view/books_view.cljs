(ns bootcamp.ui.view.books-view
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [promesa.core :as p]
            [bootcamp.ui.routing :as routing]
            [bootcamp.ui.http :as http]
            [bootcamp.ui.db :as db]))

(defn load-books []
  (-> (http/get "/api/book" {:title ""})
      (p/then (fn [books] (swap! db/app-state assoc-in [:views :book-list] books)))
      (p/catch (fn [error] (js/console.warn "oh no:" error)))))

(defn go-book [id]
  (fn [_] (routing/set-view! [:book-view :id id])))

(defn book-row [{{:keys [db/id book/title book/langs book/pages]} :book}]
  [:tr.books-view-row {:on-click (go-book id)}
   [:td title]
   [:td (str/join ", " (map name langs))]
   [:td pages]])

(defn book-list-view []
  (r/with-let [_ (load-books)]
    (let [books (-> @db/app-state :views :book-list)]
      [:table.books-view-table
       [:thead
        [:tr
         [:th "Title"]
         [:th "Langs"]
         [:th "Pages"]]]
       [:tbody
        (for [book books]
          ^{:key (:db/id book)}
          [book-row {:book book}])]])))

(defmethod routing/render-view :book-list [view]
  [:div.books-view
   [:h1 "Book list"]
   [book-list-view]])
