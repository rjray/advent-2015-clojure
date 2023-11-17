(ns advent-of-code.day25
  (:require [advent-of-code.utils :as u]))

(defn- next-code [n]
  (rem (* n 252533) 33554393))

;; Took this algorithm from:
;; https://www.reddit.com/r/adventofcode/comments/3y5jco/day_25_solutions/cyaram7/
(defn- unrolled [row col]
  (+ (+ 1 (apply + (range row)))
     (apply + (range (inc row) (+ row col)))))

(defn part-1
  "Day 25 Part 1"
  [input]
  (->> input
       u/parse-out-longs
       (apply unrolled)
       dec
       (nth (iterate next-code 20151125))))

(defn part-2
  "Day 25 Part 2"
  [input]
  input)
