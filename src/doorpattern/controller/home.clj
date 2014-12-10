(ns doorpattern.controller.home
  (:use compojure.core)
  (:require [doorpattern.models.db :as db]
            [noir.response :as resp]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.deprecated :as jdeprecated]
            )
  )


(defmacro get-sql-metadata [db method & args]
  `(jdeprecated/with-connection
     ~db
     (doall
       (jdeprecated/resultset-seq
         (~method
           (.getMetaData (jdeprecated/connection))
           ~@args)))))


(defn oracle-map [address user pass]
  {:classname "oracle.jdbc.OracleDriver"
   :subprotocol "oracle"
   :subname (str "thin:@" address)
   :user user
   :password pass
   }
  )

(defn postgres-map [dbname address user pass]
  {:classname "org.postgresql.Driver" ; must be in classpath
   :subprotocol "postgresql"
   :subname (str "//" address "/" dbname)
   :user user
   :password pass}
  )


(defn checkconnect [host dbname user password databsetype]
  (let [
         db (if (= "0" databsetype) (postgres-map dbname host user password) (oracle-map host user password))
         tables-list (get-sql-metadata db .getTables nil nil nil (into-array ["TABLE" "VIEW"]))
         ]

    (resp/json {:success true :msg "ok" :tables (map :table_name tables-list)})
    )


  )



