(ns doorpattern.controller.home
  (:import
           (java.util HashSet Date Calendar)
           (java.text SimpleDateFormat)
           (java.lang.Math)
           )
  (:use compojure.core)
  (:require [doorpattern.models.db :as db]
            [noir.response :as resp]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.deprecated :as jdeprecated]
            [doorpattern.models.db :as db]
            [clj-time.local :as l]
            [clj-time.format :as f]
            )
  )


(def db-mem (atom {}))
(def status-mem (atom {}))
(def custom-formatter (f/formatter "yyyy-MM-dd HH:mm"))
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
   :naming {:keys clojure.string/lower-case :fields clojure.string/upper-case}
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


(defn patterndoornum [address]
  (re-seq #"[0-9A-Z一二三四五六七八九十东南西北甲乙丙丁－.、,()；;-]+[号室]" address)
  )
(defn patternmainroad [address]
  (re-seq #"[^村街道镇区]+(路|大道|街|街道)"  address)
  )
(defn patternsecroad [address]
  (re-seq #".+(弄|里|巷)"   address)
  )
(defn patternvillage [address]
  (re-seq #".+(村|新村|村村|居|小区|花园|社区|苑|公寓|墩|堂村|堂|花城)"   address)
  )
(defn patternunit [address]
  (re-seq #"[0-9０-９A-Z一二三四五六七八九十－-]+单元"   address)
  )
(defn patterbuilding [address]
  (re-seq #"[0-9０-９A-Z一二三四五六七八九十－-]+(栋|幢|楼|号楼)"  address)
  )

(defn currentstatus[]

  (resp/json (vals @status-mem))
  )

(defn getdb [databsetype dbname host user password]
  (if (= "0" databsetype) (postgres-map dbname host user password) (oracle-map host user password))
  )
(defn replaceall [s arr]
  (clojure.string/replace s
             (re-pattern (apply str (interpose "|" (filter (fn [x]
                                                             (not (nil? x))) arr)
                                      )))
             "")

  )
(defn makePatterArr[dbtype doorplate keyvalue key dbconn tablename maikey]
  (println "1111" keyvalue doorplate (= dbtype "1"))

  (let [
          doornum (patterndoornum doorplate)
          mainroad (patternmainroad doorplate)
          secroad (patternsecroad doorplate)
          village   (patternvillage doorplate)
          unit    (patternunit doorplate)
          building  (patterbuilding doorplate)
          doornumstr1  (first doornum)
          doornumstr2  (second doornum)
          mainroadstr  (first (first mainroad))
          secroadstr   (first (first secroad))
          villagestr  (first (first village))
          unitstr  (first unit)
          buildingstr  (first (first building))
          test (println "test be")
          jmdstr (replaceall doorplate [doornumstr1 doornumstr2  mainroadstr
                                          secroadstr  villagestr  unitstr   buildingstr
                                          ])
          test1 (println "test ed")
          statusvalue (get @status-mem key)
          progress (+ (/ 1 (int (:totalnum statusvalue))) (:value statusvalue) )

          ]
    (println (:value statusvalue) progress)
    (swap! status-mem assoc key (conj statusvalue {:value progress}))
    (if (= dbtype "0")(db/updatesplit dbconn tablename mainroadstr villagestr jmdstr secroadstr buildingstr
                      unitstr doornumstr1 doornumstr2
                      maikey keyvalue) (db/updatesplitorcl dbconn tablename mainroadstr villagestr jmdstr secroadstr buildingstr
                                                             unitstr doornumstr1 doornumstr2
                                                             maikey keyvalue))

    #_(println [doorplate   doornumstr1 doornumstr2  mainroadstr
              secroadstr  villagestr  unitstr   buildingstr jmdstr])

    )

  )


(defn makesearchsql [item]
  ;;" and (主要道路=? or 村社区=? or 居民点=? or 次要道路=?)"
  (let [
         mainroad (if (= (:主要道路 item) "") nil (:主要道路 item))
         village (if (= (:村社区 item) "") nil (:村社区 item))
         jmd (if (= (:居民点 item) "") nil (:居民点 item))
         secroad (if (= (:次要道路 item) "") nil (:次要道路 item))
         arr    [mainroad village jmd secroad]

         arrfilter (filter (fn [x]
                             (not (nil? x))) arr)
         ]

    [(str " and (\"主要道路\"" (if (nil? mainroad) "is null" "=?")  " or \"村社区\"" (if (nil? village) "is null" "=?") ")
     and (\"居民点\"" (if (nil? jmd) "is null" "=?") " or \"次要道路\"" (if (nil? secroad) "is null" "=?") ")" )
     arrfilter
     ]
    )

  )
(defn makeflag [similardata filterdata]
  (if (= (count similardata)0) 0 (if (= (count filterdata) 0) 2 (if (= (count filterdata) 1) 3 1)) )
  )
(defn patterbeginitem [item databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable]
  (println item)
  (let [
         customsql   (makesearchsql item)

         test (println customsql)
         similardata (if(= (last databsetype) "0")(db/getdoorplatebusinessdetail customsql dbbusiness (last proptable) (last sql) (last mainkey))
                       (db/getdoorplatebusinessdetailorcl customsql dbbusiness (last proptable) (last sql) (last mainkey))
                       )

         filterdata (filter (fn [x]
                              (and (=(:门牌1 x) (:门牌1 item)) (=(:门牌2 x) (:门牌2 item)))) similardata)
         flag (makeflag similardata filterdata)
         statusvalue (get @status-mem spacekey)
         progress (+ (/ 1 (int (:totalnum statusvalue))) (:value statusvalue) )
         ]

    (swap! status-mem assoc spacekey (conj statusvalue {:value progress}))
    (println flag filterdata)
    ;(println (makeflag similardata filterdata))

    (condp = flag
      1 (do (if (= (first databsetype) "0") (db/updatespaceone dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))])
          (db/updatespaceoneorcl [dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))]]))
          (let
            [rids (map #(get % (keyword (last mainkey))) filterdata)
             ridstr (clojure.string/join "," rids)
             ]
            (if (= (first databsetype) "0") (db/updatebusinessone dbbusiness (last proptable)  (last mainkey) ridstr [ (:uid item)])
              (db/updatebusinessoneorcl dbbusiness (last proptable)  (last mainkey) ridstr [ (:uid item)])
              )
            )

          )
      2 (if (= (first databsetype) "0") (db/updatespacetwo dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))])
          (db/updatespacetwoorcl dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))]))
      3 (if (= (first databsetype) "0") (db/updatespacethree dbspace (first proptable)  (first mainkey) [(:mapguid (first filterdata))(get item (keyword (first mainkey)))])
          (db/updatespacethreeorcl dbspace (first proptable)  (first mainkey) [(:mapguid (first filterdata)) (get item (keyword (first mainkey)))]))
      (if (= (first databsetype) "0") (db/updatespacezero dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))])
        (db/updatespacezeroorcl dbspace (first proptable)  (first mainkey) [(get item (keyword (first mainkey)))]))


    )

  )
  )

(defn patterbegin [databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable]
  (let
    [
      spacedata  (if(= (first databsetype) "0")(db/getdoorplatespacedetail dbspace (first proptable) (first sql) (first mainkey))
                   (db/getdoorplatespacedetailorcl dbspace (first proptable) (first sql) (first mainkey))
                   )

      ]
    (println "pattern begin")
    (dorun (map #(patterbeginitem % databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable ) spacedata) )

    )


  )






(defn patternaftersplit [databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable]
  (let [
         spacestatusvalue (get @status-mem spacekey)
         businessstatusvalue  (get @status-mem businesskey)
         ]

    (swap! status-mem assoc spacekey (conj spacestatusvalue {:value 0
                                                             :statue "匹配"
                                                             }))


    (swap! status-mem dissoc businesskey)


    (patterbegin databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable)

    (swap! status-mem assoc spacekey (conj spacestatusvalue {:value 1
                                                             :statue "匹配完成"
                                                             }))


    )

  )




(defn splitetail [  databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable issplit]

  (if (first issplit)  (dorun (map #(makePatterArr(first databsetype)  (:doorplate %) (get %  (keyword (first mainkey))) spacekey dbspace (first proptable) (first mainkey))
                                   (if(= (first databsetype) "0")(db/getdoorplatespace dbspace (first proptable) (first sql) (first mainkey))
                                     (db/getdoorplatebusiness dbspace (first proptable) (first sql) (first mainkey))
                                     )))nil)

  (if (last issplit)  (dorun (map #(do (println %) (makePatterArr (last databsetype)  (:doorplate %) (get %  (keyword (last mainkey))) businesskey dbbusiness (last proptable) (last mainkey)) )
                                  (if(= (last databsetype) "0")(db/getdoorplatespace dbbusiness (last proptable) (last sql) (last mainkey))
                                    (db/getdoorplatebusiness dbbusiness (last proptable) (last sql) (last mainkey)))))nil )



  (patternaftersplit databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable)

  )


(defn splitbytable [user password mainkey dbname databsetype sql proptable issplit host]
  (let [
         dbspace    (getdb (first databsetype) (first dbname) (first host)(first user)(first password))
         dbbusiness (getdb (last databsetype) (last dbname) (last host)(last user)(last password))
         spacekey   (str (first host)(first user) (first proptable))
         businesskey  (str (last host)(last user) (last proptable))
         df   (new SimpleDateFormat "yyyy-MM-dd HH:mm")
         ;spacedoorplate  (db/getdoorplatespace dbspace (first proptable) (first sql))
         ;businessdoorplate (db/getdoorplatespace dbbusiness (last proptable) (last sql))
        ]
    (swap! status-mem assoc spacekey {:totalnum (first (vals (first (if(= (first databsetype) "0") (db/getcountspace dbspace (first proptable) (first sql))
                                                                      (db/getcountbusiness dbspace (first proptable) (first sql))
                                                                      ))))
                                      :table (first proptable)
                                      :user  (first user)
                                      :value 0
                                      :statue "分解"
                                      :time  (.format df (.getTime (new Date)))
                                      })

    (swap! status-mem assoc businesskey {:totalnum (first (vals (first (if(= (last databsetype) "0")(db/getcountspace dbbusiness (last proptable) (last sql))
                                                                         (db/getcountbusiness dbbusiness (last proptable) (last sql))
                                                                         ))))
                                      :table (last proptable)
                                      :user  (last user)
                                      :value 0
                                      :statue "分解"
                                      :time  (.format df (.getTime (new Date)))
                                      })
    ;(swap! status-mem assoc businesskey {:totalnum (first (vals (first (db/getcountspace dbbusiness (last proptable) (last sql)))))})

    (println status-mem )
    ;;(println (keyword (last mainkey)))
    ;(future (dorun (map #(makePatterArr  (:doorplate %) (get %  (keyword (first mainkey))) spacekey dbspace (first proptable) (first mainkey)) (db/getdoorplatespace dbspace (first proptable) (first sql) (first mainkey)))))
    (future (splitetail databsetype sql mainkey spacekey businesskey dbspace dbbusiness proptable issplit))
    ;(dorun (map #(makePatterArr (:doorplate %)) businessdoorplate))
    ;(println status-mem )
    )

  )



(defn patterndoor [user password mainkey dbname databsetype sql proptable issplit host]
  ;(println user password mainkey dbname databsetype sql proptable issplit host)
  (try (do (splitbytable user password mainkey dbname databsetype sql proptable issplit host)(resp/json {:success true :msg "ok"}))(catch Exception e (resp/json {:sucess false :msg (.getMessage e)})))


  )

(defn checkconnect [host dbname user password databsetype]
  (try (let [
         db (getdb databsetype dbname host user password)
         tables-list (get-sql-metadata db .getTables nil nil nil (into-array ["TABLE" "VIEW"]))
         ]
    (resp/json {:success true :msg "ok" :tables (map :table_name tables-list)})
    )(catch Exception e (resp/json {:sucess false :msg (.getMessage e)})))
  )




