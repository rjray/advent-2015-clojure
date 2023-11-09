(ns advent-of-code.day10
  (:require [advent-of-code.utils :as u]))

(def ^:private dvec [nil \1 \2 \3 \4 \5 \6 \7 \8 \9])

(defn- round [digits]
  (mapcat #(list (dvec (count %)) (first %)) (partition-by identity digits)))

(defn- rounds [n digits]
  (loop [rd 0, digits digits]
    (cond
      (= rd n) digits
      :else    (recur (inc rd) (round digits)))))

(defn part-1
  "Day 10 Part 1"
  [input]
  (->> input
       u/to-lines
       ((comp seq first))
       (rounds 40)
       count))

(defn part-2
  "Day 10 Part 2"
  [input]
  (->> input
       u/to-lines
       ((comp seq first))
       (rounds 50)
       count))
