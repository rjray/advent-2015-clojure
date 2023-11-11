(ns advent-of-code.day13
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]))

(def ^:private line-re
  (re-pattern
   (str "(\\w+) would (lose|gain) (\\d+) "
        "happiness units by sitting next to "
        "(\\w+)[.]")))

(defn- parse-line [line]
  (let [[_ p1 sign num p2] (re-matches line-re line)
        value              (if (= sign "gain")
                             (parse-long num)
                             (- (parse-long num)))]
    (list (list (keyword p1) (keyword p2)) value)))

(defn- make-graph [tuples]
  (let [people (distinct (flatten (map first tuples)))
        edges  (into {} (map hash-map (map first tuples) (map last tuples)))]
    {:people people, :edges edges}))

(defn- eval-seating [order edges]
  (let [orderv (vec (concat (list (last order)) order (list (first order))))]
    (reduce (fn [sum idx]
              (let [person (orderv idx)
                    left (orderv (dec idx))
                    right (orderv (inc idx))]
                (+ sum
                   (edges (list person left)) (edges (list person right)))))
            0 (range 1 (inc (count order))))))

(defn- calc-happiness [{people :people, edges :edges}]
  (let [orders (comb/permutations people)]
    (loop [[order & orders] orders, results {}]
      (cond
        (nil? order)                        results
        ;; If true, we've already calculated this order (in reverse).
        (contains? results (reverse order)) (recur orders results)
        :else
        (recur orders (assoc results order (eval-seating order edges)))))))

(defn part-1
  "Day 13 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       make-graph
       calc-happiness
       vals
       (apply max)))

(defn- add-me [{people :people, edges :edges}]
  (loop [[p & ps] people, edges edges]
    (cond
      (nil? p) {:people (cons :me people), :edges edges}
      :else    (recur ps (assoc edges (list :me p) 0, (list p :me) 0)))))

(defn part-2
  "Day 13 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       make-graph
       add-me
       calc-happiness
       vals
       (apply max)))
