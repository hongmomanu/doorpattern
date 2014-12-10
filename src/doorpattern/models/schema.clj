(ns doorpattern.models.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))



(def db-store "site.db")
(def db-store-sqlite "sqlite.db3")

(def datapath (str (System/getProperty "user.dir") "/"))

;;(defdb korma-db
;;  (oracle { :user "xxx"
;;            :password "xxx"
;;            :host "my.oracle.db"
;;            :port 1521
;;            :make-pool? true }))

(def db-oracle  {:classname "oracle.jdbc.OracleDriver"
                 :subprotocol "oracle"
                 :subname "thin:@10.33.253.32:1521:orcl"
                 :user "sms"
                 :password "sms"
                 :naming {:keys clojure.string/lower-case :fields clojure.string/upper-case}})

(def db-oraclestation  {:classname "oracle.jdbc.OracleDriver"
                 :subprotocol "oracle"
                 :subname "thin:@10.33.253.70:1521:orcl"
                 :user "dgm"
                 :password "dgm"
                 :naming {:keys clojure.string/lower-case :fields clojure.string/upper-case}})

(def db-mysql {:subprotocol "mysql"
               ;;:subname "//127.0.0.1:9306?characterEncoding=utf8&maxAllowedPacket=512000"
               :subname "//10.33.5.103:3306/jopens"
               :user "root"
               :password "rootme"
               })

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str datapath db-store)
              :user "sa"
              :password ""
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})

(def db-spec-sqlite {:classname "org.sqlite.JDBC"
                     :subprotocol "sqlite"
                     :subname (str datapath db-store-sqlite)
                     })

(def db-h2-mem {:classname "org.h2.Driver"
                :subprotocol "h2"
                :subname "mem:session;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0" })


(defn initialized?
  "checks to see if the database schema is present"
  []
  ;;(.exists (new java.io.File (str (io/resource-path) db-store ".h2.db")))
  ;;(create-stations-table)
  ;(create-samplecache-table)
  ;;(create-suspend-table)
  (.exists (new java.io.File (str datapath db-store-sqlite "")))
  )


