(ns build
  (:require
   [clojure.tools.build.api :as b]))

(def lib 'mahjong-point-calc/core)
(def main 'mahjong-point-calc.mahjong-point-calc)
(def version (format "0.1.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def jar-file (format "target/%s.jar" (namespace lib)))
(def uber-file (format "target/%s-standalone.jar" (namespace lib)))
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn uber [_]
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile [main]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main main}))
