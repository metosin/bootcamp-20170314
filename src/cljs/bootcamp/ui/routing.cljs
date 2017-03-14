(ns bootcamp.ui.routing
  (:require [bidi.bidi :as bidi]
            [clojure.set :as set]
            [bootcamp.ui.routes :as routes]
            [bootcamp.ui.db :as db]))

;;
;; Utils:
;;

(defn- path->view [path]
  (-> (bidi/match-route routes/routes path)
      (set/rename-keys {:handler :view-id})))

(defn- notify-route-change-listener [view-params]
  (let [path js/window.location.pathname
        view (or (path->view path)
                 (do (js/console.log "no matching view for path" path)
                     {:view-id :not-found}))]
    (swap! db/app-state assoc :view (if view-params
                                      (assoc view :view-params view-params)
                                      view))))

;;
;; Initialize routing:
;;

(defn init! []
  (set! (.-onpopstate js/window) (fn [_] (notify-route-change-listener nil)))
  (notify-route-change-listener nil))

;;
;; Public API:
;;

(def path-for (partial bidi/path-for routes/routes))

(defn set-view!
  ([path-args] (set-view! path-args nil))
  ([path-args view-params]
   (if-let [path (apply path-for path-args)]
     (do (js/window.history.pushState nil (pr-str path) path)
         (notify-route-change-listener view-params))
     (js/console.error "set-view!: unknown path:" (pr-str path-args)))))

(defn view []
  (-> @db/app-state :view))

;;
;; Render view multimethod:
;;

(defmulti render-view :view-id)
