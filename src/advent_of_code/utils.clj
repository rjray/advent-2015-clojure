(ns advent-of-code.utils
  (:require [clojure.string :as str]))

(defn to-blocks
  "Turn a blob (probably from `slurp`) into a seq of blocks"
  [input]
  (str/split input #"\n\n"))

(defn to-lines
  "Turn a blob or block into a seq of lines"
  [input]
  (str/split-lines input))

(defn to-matrix
  "Turn a blob (or block) into a vector of vectors"
  [input]
  (->> input
       to-lines
       (mapv vec)))

(defn parse-out-longs
  "Parse out all numbers in `line` that are integers (longs)"
  [line]
  (map parse-long (re-seq #"-?\d+" line)))

;; Like the core time macro, but rather than printing the elapsed time it
;; returns a list of (result, time).
(defmacro time-it [expr]
  `(let [start# (. System (nanoTime))
         ret#   ~expr
         end#   (/ (double (- (. System (nanoTime)) start#)) 1000000.0)]
     (list ret# end#)))

(defn tee [expr]
  (print expr "\n")
  expr)
