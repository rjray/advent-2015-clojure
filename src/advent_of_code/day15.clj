(ns advent-of-code.day15
  (:require [advent-of-code.utils :as u]))

;; Taken from a Clojure solution on reddit here:
;; https://www.reddit.com/r/adventofcode/comments/3wwj84/day_15_solutions/cy087mv/
(defn- partitions [n k]
  (if (> k 1)
    (mapcat (fn [x]
              (map (partial cons x)
                   (partitions (- n x) (- k 1))))
            (range 1 (inc (- n (- k 1)))))
    [[n]]))

(defn- cookie-score [ingredients mix]
  (let [weights (for [x (range (count mix))]
                  (map (partial * (nth mix x)) (nth ingredients x)))
        totals  (take 4 (apply (partial map +) weights))]
    (apply * (map #(if (pos? %) % 0) totals))))

(defn- max-score [spoons ingredients]
  (reduce max (map (partial cookie-score ingredients)
                   (partitions spoons (count ingredients)))))

(defn part-1
  "Day 15 Part 1"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (max-score 100)))

(defn- score-with-cals [ingredients mix]
  (let [score (cookie-score ingredients mix)
        cals  (apply + (for [x (range (count mix))]
                         (* (nth mix x) (last (nth ingredients x)))))]
    (list score cals)))

(defn- max-by-calories [cals spoons ingredients]
  (last (sort-by first
                 (get (group-by last
                                (map (partial score-with-cals ingredients)
                                     (partitions spoons (count ingredients))))
                      cals))))

(defn part-2
  "Day 15 Part 2"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (max-by-calories 500 100)
       first))
