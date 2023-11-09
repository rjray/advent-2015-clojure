(ns advent-of-code.day09
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]
            [clojure.math.combinatorics :as comb]))

;; For purposes, we need an equivalent of pos-Inf:
(def ^:private +Inf Integer/MAX_VALUE)

(defn- parse-line [line]
  (let [[src _ dst _ dist] (str/split line #"\s+")]
    (list src dst (parse-long dist))))

(defn- build-graph [tuples]
  (let [cities-list (sort (distinct (apply concat
                                           (map #(list (first %) (second %))
                                                tuples))))
        cities-map  (into {} (map hash-map
                                  cities-list
                                  (range 1 (inc (count cities-list)))))]
    (loop [[[s d ds] & tuples] tuples, edges {}]
      (cond
        (nil? s) (list (count cities-list) edges)
        :else    (recur tuples
                        (assoc edges #{(cities-map s) (cities-map d)} ds))))))

(defn- calc-cost [p edges]
  (apply + (map edges (map set (partition 2 1 p)))))

(defn- brute-force [fun ival [n edges]]
  (reduce fun ival (map #(calc-cost % edges)
                        (comb/permutations (range 1 (inc n))))))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       build-graph
       (brute-force min Integer/MAX_VALUE)))

(defn part-2
  "Day 09 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       build-graph
       (brute-force max 0)))
