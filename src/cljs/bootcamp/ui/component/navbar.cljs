(ns bootcamp.ui.component.navbar
  (:require [bootcamp.ui.routing :as routing]))

(defn go-home [e]
  (.preventDefault e)
  (routing/set-view! [:book-list]))

(defn navbar []
  [:div.navbar
   [:div.navbar-brand
    [:div.navbar-brand__title
     [:a.navbar-brand__home {:on-click go-home} "Bootcamp Bookstore"]]
    [:div.navbar-brand__motto "Boot your mind with our books"]]])
