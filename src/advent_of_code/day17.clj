(ns advent-of-code.day17
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]))

(defn- setup-data [lines]
  (map list (map parse-long lines) (range)))

(defn- value [s] (list (reduce + (map first s)) (count s)))

(defn part-1
  "Day 17 Part 1"
  [input]
  (->> input
       u/to-lines
       setup-data
       comb/subsets
       (map value)
       (filter #(= 150 (first %)))
       count))

(defn- get-min-val [x]
  (x (apply min (keys x))))

(defn part-2
  "Day 17 Part 2"
  [input]
  (->> input
       u/to-lines
       setup-data
       comb/subsets
       (map value)
       (filter #(= 150 (first %)))
       (group-by last)
       get-min-val
       count))
