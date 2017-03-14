(ns bootcamp.main
  (:gen-class))

(defn -main [& args]
  (require 'bootcamp.core)
  ((resolve 'bootcamp.core/start)))
