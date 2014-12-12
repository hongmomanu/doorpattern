(ns doorpattern.models.db
  (:use korma.core
        [korma.db :only [defdb with-db]])
  (:require [doorpattern.models.schema :as schema]))


(defn getcountspace [db tablename sql]

  (with-db db
           (exec-raw [(str "select count(*) from  \"" tablename "\" " sql) []] :results)
  )

  )
(defn getcountbusiness [db tablename sql]

  (with-db db
           (exec-raw [(str "select count(*) from  " tablename " " sql) []] :results)
  )

  )


(defn updatesplit [db tablename mainroadstr villagestr jmdstr secroadstr buildingstr
                   unitstr doornumstr1 doornumstr2
                            maikey keyvalue]
  ;,updatetime=now()
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set \"主要道路\"=?,\"村社区\"=?,\"居民点\"=?,
    \"次要道路\"=?,\"楼栋号\"=?,\"单元号\"=?,\"门牌1\"=?,\"门牌2\"=?  where " maikey "=? ") [mainroadstr villagestr jmdstr secroadstr buildingstr
                                                                                                unitstr doornumstr1 doornumstr2
                                                                                                 keyvalue]])
    )

  )
(defn updatesplitorcl [db tablename mainroadstr villagestr jmdstr secroadstr buildingstr
                   unitstr doornumstr1 doornumstr2
                            maikey keyvalue]
  ;,updatetime=now()

  (with-db db
    (exec-raw [(str "update " tablename  "  set 主要道路=?,村社区=?,居民点=?,
    次要道路=?,楼栋号=?,单元号=?,门牌1=?,门牌2=?  where " maikey "=? ") [mainroadstr villagestr jmdstr secroadstr buildingstr
                                                                                                unitstr doornumstr1 doornumstr2
                                                                                                 keyvalue]])
    )

  )
(defn getdoorplatespace [db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select doorplate ," mainkey " from  \"" tablename "\" " sql) []] :results)
    )

  )

(defn getdoorplatespacedetail [db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select " mainkey ", \"主要道路\",\"村社区\",\"居民点\",
    \"次要道路\",\"楼栋号\",\"单元号\",\"门牌1\",\"门牌2\",uid from  \"" tablename "\" " sql) []] :results)
    )

  )
(defn getdoorplatebusinessdetail [cusql db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select " mainkey ", \"主要道路\",\"村社区\",\"居民点\",
    \"次要道路\",\"楼栋号\",\"单元号\",\"门牌1\",\"门牌2\",mapguid  from  \"" tablename "\" " sql (first cusql)) (second cusql)] :results)
    )

  )

(defn getdoorplatespacedetailorcl [db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select " mainkey " as \"" mainkey "\", \"主要道路\",\"村社区\",\"居民点\",
    \"次要道路\",\"楼栋号\",\"单元号\",\"门牌1\",\"门牌2\",uid as \"uid\" from  " tablename " " sql ) []] :results)
    )
  )
(defn getdoorplatebusinessdetailorcl [cusql db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select " mainkey " as \"" mainkey "\", \"主要道路\",\"村社区\",\"居民点\",
    \"次要道路\",\"楼栋号\",\"单元号\",\"门牌1\",\"门牌2\",mapguid as \"mapguid\"  from  " tablename " " sql (first cusql)) (second cusql)] :results)
    )
  )



(defn getdoorplatebusiness [db tablename sql mainkey]

  (with-db db
    (exec-raw [(str "select doorplate as \"doorplate\" ," mainkey " as \"" mainkey "\" from  " tablename " " sql) []] :results)
    )
  )

(defn updatespacezeroorcl [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update " tablename  "  set flag=0  where " mainkey "=? ") data])
    )
  )

(defn updatespacezero [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set flag=0  where " mainkey "=? ") data])
    )
  )

(defn updatespaceoneorcl [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update " tablename  "  set flag=1  where " mainkey "=? ") data])
    )
  )
(defn updatespaceone [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set flag=1  where " mainkey "=? ") data])
    )
  )

(defn updatespacetwoorcl [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update " tablename  "  set flag=2  where " mainkey "=? ") data])
    )
  )

(defn updatespacetwo [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set flag=2  where " mainkey "=? ") data])
    )
  )
(defn updatespacethree [db tablename  mainkey data]
  (println tablename  mainkey data)
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set flag=3 ,workid=?  where " mainkey "=? ") data])
    )
  )

(defn updatespacethreeorcl [db tablename  mainkey data]
  (with-db db
    (exec-raw [(str "update " tablename  "  set flag=3 ,workid=?  where " mainkey "=? ") data])
    )
  )

(defn updatebusinessoneorcl [db tablename  mainkey rid data]
  (with-db db
    (exec-raw [(str "update " tablename  "  set mapid=?   where " mainkey " in (" rid ") ") data])
    )

  )
(defn updatebusinessone [db tablename  mainkey rid data]
  (with-db db
    (exec-raw [(str "update \"" tablename  "\"  set mapid=?   where " mainkey " in (" rid ") ") data])
    )

  )
