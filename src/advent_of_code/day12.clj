(ns advent-of-code.day12
  (:require [clojure.walk :as walk]
            [clojure.data.json :as json]))

(defn- sum-up [x]
  (loop [[x & xs] (list x), sum 0]
    (cond
      (nil? x)    sum
      (number? x) (recur xs (+ sum x))
      (vector? x) (recur xs (+ sum (reduce + (map sum-up x))))
      (map? x)    (recur xs (+ sum (reduce + (map sum-up (vals x)))))
      ;; None of the above, probably a string. Skip.
      :else       (recur xs sum))))

(defn part-1
  "Day 12 Part 1"
  [input]
  (->> input
       json/read-str
       sum-up))

(defn part-2
  "Day 12 Part 2"
  [input]
  (->> input
       json/read-str
       (walk/prewalk (fn [x]
                       (if (and (map? x)
                                (some #(= "red" %) (vals x)))
                         0
                         x)))
       sum-up))
