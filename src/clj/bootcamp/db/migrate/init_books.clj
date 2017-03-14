(ns bootcamp.db.migrate.init-books
  (:require [datomic.api :as d]
            [bootcamp.db.conn :as conn]))

(def authors {:fogus {:fname "Michael" :lname "Fogus"}
              :houser {:fname "Chris" :lname "Houser"}
              :cesarini {:fname "Francesco" :lname "Cesarini"}
              :thompson {:fname "Simon" :lname "Thompson"}
              :rochester {:fname "Eric" :lname "Rochester"}
              :venkat {:fname "Venkat" :lname "Subramaniam"}
              :friedman {:fname "Daniel" :lname "Friedman"}
              :felleisen {:fname "Matthias" :lname "Felleisen"}
              :sussman {:fname "Gerald" :lname "Sussman"}
              :abelson {:fname "Harold" :lname "Abelson"}
              :jsussman {:fname "Julie" :lname "Sussman"}})

(def books [{:title "The Joy of Clojure"
             :langs #{:clojure}
             :pages 328
             :authors [:fogus :houser]}
            {:title "Erlang Programming"
             :langs #{:erlang}
             :pages 470
             :authors [:cesarini :thompson]}
            {:title "Clojure Data Analysis Cookbook"
             :langs #{:clojure}
             :pages 326
             :authors [:rochester]}
            {:title "Programming Concurrency on the JVM"
             :langs #{:java :ruby :groovy :scala :clojure}
             :pages 270
             :authors [:venkat]}
            {:title "The Little Schemer"
             :langs #{:scheme}
             :pages 196
             :authors [:friedman :felleisen :sussman]}
            {:title "Types and Programming Languages"
             :langs #{:haskel :java :fortran}
             :pages 623
             :authors [:abelson :sussman :jsussman]}])

; source: Metosin library

(defn dump-authors [conn]
  (->> authors
       (vals)
       (map (fn [{:keys [fname lname]}]
              {:db/id (str fname " " lname)
               :entity/type :entity.type/author
               :author/first-name fname
               :author/last-name lname}))
       (d/transact conn)
       (deref)))

(defn find-author [db {:keys [fname lname]}]
  (d/q '[:find ?author .
         :in $ ?fname ?lname
         :where
         [?author :author/first-name ?fname]
         [?author :author/last-name ?lname]]
       db fname lname))

(defn dump-books [conn]
  (let [db (d/db conn)]
    (->> books
         (map (fn [book]
                {:db/id (-> book :title)
                 :entity/type :entity.type/book
                 :book/title (-> book :title)
                 :book/langs (-> book :langs)
                 :book/pages (-> book :pages)
                 :book/authors (->> book
                                    :authors
                                    (map authors)
                                    (map (partial find-author db)))}))
         (d/transact conn)
         (deref))))

(defn dump-all []
  (let [conn conn/conn]
    (dump-authors conn)
    (dump-books conn)))

(comment
  (dump-all)
  )