(ns advent-of-code.day08
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- repl [[_ ch hex]]
  (case ch
    ("\\" "\"") ch
    ;; If not one of those, then it's a hex-escape
    (str (char (Integer/parseInt hex 16)))))

(defn- cleanup [s]
  (let [s' (subs s 1 (dec (count s)))]
    (str/replace s' #"\\(\\|\"|x([0-9a-f]{2}))" repl)))

(defn part-1
  "Day 08 Part 1"
  [input]
  (let [lines (u/to-lines input)
        total (apply + (map count lines))
        clean (apply + (map (comp count cleanup) lines))]
    (- total clean)))

(defn- encode [s]
  (apply str (list \"
                   (str/replace s #"(\"|\\)" "\\$1")
                   \")))

(defn part-2
  "Day 08 Part 2"
  [input]
  (let [lines   (u/to-lines input)
        total   (apply + (map count lines))
        encoded (apply + (map (comp count encode) lines))]
    (- encoded total)))
