(ns bootcamp.ui.main
  (:require [goog.dom :as dom]
            [reagent.core :as r]
            [reagent-dev-tools.core :as dev-tools]
            [reagent-dev-tools.state-tree :as dev-state]
            [promesa.core :as p]
            [bootcamp.ui.db :as db]
            [bootcamp.ui.component.navbar :as navbar]
            [bootcamp.ui.routing :as routing]
            [bootcamp.ui.views]))

;; navigation
;; nav-bar
;; write sumthin

(defn main-view []
  [:div.main-view
   [navbar/navbar]
   (routing/render-view (routing/view))])

(defn render-dev-tools []
  (when goog.DEBUG
    (dev-state/register-state-atom "app-state" db/app-state)
    (r/render [dev-tools/dev-tool {}] (.getElementById js/document "dev"))))

(defn init! []
  (render-dev-tools)
  (routing/init!)
  (r/render [main-view] (.getElementById js/document "app")))

(init!)

