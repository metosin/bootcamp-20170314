(ns bootcamp.domain.book
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [datomic.api :as d]
            [ring.util.http-response :as resp]
            [compojure.api.sweet :refer [routes GET POST] :as c]
            [schema-tools.core :as st]
            [bootcamp.db.conn :as conn]))

(s/defschema Author
  (-> {:author/first-name s/Str
       :author/last-name s/Str
       :db/id s/Int
       :entity/type s/Keyword}
      (st/optional-keys)))

(s/defschema Book
  (-> {:book/title s/Str
       :book/langs #{s/Keyword}
       :book/pages s/Int
       :book/authors #{Author}
       :db/id s/Int
       :entity/type s/Keyword}
      (st/optional-keys)))

(defn find-books [db]
  (->> (d/q '[:find [?book ...]
              :where
              [?book :entity/type :entity.type/book]]
            db)
       (map (fn [book-id]
              (d/entity db book-id)))))

(def book-routes
  (routes
    (GET "/" []
      :return [Book]
      (resp/ok (find-books (d/db conn/conn))))

    (GET "/:id" []
      :path-params [id :- s/Int]
      :return Book
      (let [book (d/entity (d/db conn/conn) id)]
        (if (= (:entity/type book) :entity.type/book)
          (resp/ok book)
          (resp/not-found (str "can't find book with id " id)))))))

