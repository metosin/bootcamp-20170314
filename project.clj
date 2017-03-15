(defproject bootcamp-20170314 "0.0.0-SNAPSHOT"
  :description "Bootcamp 2017-03-14"
  :url "https://github.com/metosin/bootcamp-20170314"
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"}}

  :dependencies [[org.clojure/clojure "1.8.0"]

                 ; Common libs:
                 [prismatic/schema "1.1.3"]
                 [prismatic/plumbing "0.5.3"]
                 [metosin/potpuri "0.4.0"]
                 [mount "0.1.11"]
                 [org.clojure/core.async "0.3.441"]
                 [funcool/promesa "1.8.0"]

                 ; REST:
                 [ring/ring-core "1.5.1" :exclusions [clj-time]]
                 [ring/ring-defaults "0.2.3"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [metosin/compojure-api "1.2.0-alpha3"]
                 [metosin/ring-http-response "0.8.2"]
                 [metosin/ring-swagger-ui "2.2.8"]
                 [org.immutant/web "2.1.6" :exclusions [ring/ring-core
                                                        org.jboss.logging/jboss-logging]]

                 ; HTTP and HTML
                 [clj-http "3.4.1"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.6"]

                 ; ClojureScript:
                 [org.clojure/clojurescript "1.9.456"]
                 [org.clojure/tools.reader "1.0.0-beta4"]

                 ; Datomic:
                 [com.datomic/datomic-free "0.9.5544" :exclusions [com.google.guava/guava
                                                                   org.slf4j/slf4j-nop]]
                 [io.rkn/conformity "0.4.0"]

                 ; Logging:
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/jcl-over-slf4j "1.7.24"]
                 [org.slf4j/jul-to-slf4j "1.7.24"]
                 [org.slf4j/log4j-over-slf4j "1.7.24"]
                 [ch.qos.logback/logback-classic "1.2.1"]

                 ;; Frontend libs:
                 [reagent "0.6.1"]
                 [bidi "2.0.16"]
                 [cljs-http "0.1.42"]

                 ; Work flow:
                 [org.clojure/tools.namespace "0.3.0-alpha3"]
                 [org.clojure/tools.nrepl "0.2.12"]

                 ;; Frontend dev tools:
                 [binaryage/devtools "0.9.2"]
                 [metosin/reagent-dev-tools "0.1.0"]]

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]

  :plugins [[lein-pdo "0.1.1"]
            [deraen/lein-sass4clj "0.3.0"]
            [lein-cljsbuild "1.1.5"]]

  :profiles {:dev {:resource-paths ["dev-resources" "target/dev/resources"]
                   :sass {:target-path "target/dev/resources/public/css"}
                   :plugins [[lein-figwheel "0.5.9" :exclusions [org.clojure/clojure]]]}
             :prod {:resource-paths ["target/prod/resources"]
                    :sass {:target-path "target/prod/resources/public/css"}
                    :aot [bootcamp.main]
                    :main bootcamp.main
                    :uberjar-name "bootcamp.jar"}}

  :sass {:source-paths ["src/sass"]
         :target-path "target/dev/resources/public/css"
         :source-map true
         :output-style :compressed}

  :figwheel {:css-dirs ["target/dev/resources/public/css"]
             :repl false}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljc" "src/cljs"]
                        :figwheel {:websocket-host :js-client-host}
                        :compiler {:main bootcamp.ui.main
                                   :asset-path "/js/out"
                                   :output-to "target/dev/resources/public/js/main.js"
                                   :output-dir "target/dev/resources/public/js/out"
                                   :source-map-timestamp true
                                   :closure-defines {goog.DEBUG true}
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install [:formatters :hints]}}}}
                       {:id "dist"
                        :source-paths ["src/cljc" "src/cljs"]
                        :compiler {:main bootcamp.ui.main
                                   :optimizations :advanced
                                   :output-to "target/prod/resources/public/js/main.js"
                                   :output-dir "target/prod/resources/public/js/out"
                                   :closure-defines {goog.DEBUG false}}}]}

  :auto-clean false

  :aliases {"dev" ["do"
                   ["clean"]
                   ["pdo"
                    ["sass4clj" "auto"]
                    ["figwheel"]]]
            "dist" ["with-profile" "dist" "do"
                    ["clean"]
                    ["sass4clj" "once"]
                    ["cljsbuild" "once" "dist"]
                    ["uberjar"]]})
