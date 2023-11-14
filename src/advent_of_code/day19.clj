(ns advent-of-code.day19
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- setup-data [lines]
  (list (last lines)
        (group-by first (map #(re-seq #"\w+" %) (drop-last 2 lines)))))

(defn- sub-in [s pos key sub]
  (let [head (subs s 0 pos)
        tail (subs s (+ pos (count key)) (count s))]
    (str head sub tail)))

(defn- do-repl [base rtable]
  (let [re      (re-pattern (str/join "|" (sort (keys rtable))))
        matches (group-by last (u/re-pos re base))]
    (for [pat (keys matches), [pos key] (matches pat), sub (rtable pat)]
      (sub-in base pos key (last sub)))))

(defn part-1
  "Day 19 Part 1"
  [input]
  (->> input
       u/to-lines
       setup-data
       (apply do-repl)
       distinct
       count))

;; Approach is based on this Perl code:
;; https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/cy4k8ca/
(defn- find-fewest-steps [[start rtable]]
  (let [re   (re-pattern (str "(" (str/join "|" (sort (keys rtable))) ")"))
        repl (fn [[_ k]] (last (first (rtable k))))]
    (loop [last "", curr start, s 0]
      (cond
        (= curr "e")  s
        (= curr last) (list curr s)
        :else         (recur curr (str/replace-first curr re repl) (inc s))))))

(defn part-2
  "Day 19 Part 2"
  [input]
  (->> input
       u/to-lines
       (map #(apply str (reverse %)))
       setup-data
       find-fewest-steps))
