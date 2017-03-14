(ns bootcamp.ui.view.not-found
  (:require [bootcamp.ui.routing :as routing]))

(defmethod routing/render-view :default [view]
  [:h1 "Not found"])
