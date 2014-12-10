(ns doorpattern.routes.home
  (:require [compojure.core :refer :all]
            [doorpattern.layout :as layout]
            [doorpattern.util :as util]
            [doorpattern.controller.home :as chome]
            ))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn index-page []
  (layout/render
    "index.html"))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (index-page))
  (POST "/checkconnect" [host dbname user password databsetype] (chome/checkconnect host dbname user password databsetype))
  (GET "/about" [] (about-page)))
