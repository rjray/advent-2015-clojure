(ns advent-of-code.day05
  (:require [advent-of-code.utils :as u]))

(defn- is-nice [s]
  (cond
    (re-find #"ab|cd|pq|xy" s)            false
    (not (re-find #"([A-Za-z])\1" s))     false
    (< (count (re-seq #"([aeiou])" s)) 3) false
    :else                                 true))

(defn part-1
  "Day 05 Part 1"
  [input]
  (->> input
       u/to-lines
       (filter is-nice)
       count))

(defn- is-nice2 [s]
  (cond
    (not (re-find #"([a-z]{2}).*\1" s)) false
    (not (re-find #"([a-z]).\1" s))     false     
    :else                               true))

(defn part-2
  "Day 05 Part 2"
  [input]
  (->> input
       u/to-lines
       (filter is-nice2)
       count))
