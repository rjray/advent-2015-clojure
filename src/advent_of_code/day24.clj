(ns advent-of-code.day24
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]))

(defn- get-size [n ns]
  (/ (reduce + ns) n))

(defn- get-smallest-size [ns s]
  (count (first (filter #(= s (apply + %)) (comb/subsets ns)))))

(defn- find-group [n ns]
  (let [size (get-size n ns)
        small (get-smallest-size ns size)
        choices (filter #(= size (apply + %)) (comb/combinations ns small))]
    (first (sort (map #(reduce * %) choices)))))

(defn part-1
  "Day 24 Part 1"
  [input]
  (->> input
       u/to-lines
       (map #(Integer/parseInt %))
       (find-group 3)))

(defn part-2
  "Day 24 Part 2"
  [input]
  (->> input
       u/to-lines
       (map #(Integer/parseInt %))
       (find-group 4)))
