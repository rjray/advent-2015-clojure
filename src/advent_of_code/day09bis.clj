(ns advent-of-code.day09bis
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]
            [clojure.math.combinatorics :as comb]))

;;; This is the "improved" day 9 solution. It uses the Bellman-Ford DP
;;; algorithm to solve day 9 as a Traveling Salesman Problem. Unlike the more
;;; general use for TSP, this does not count a connecting edge back to the
;;; starting city.
;;;
;;; Because this problem doesn't use the full-loop approach that is usually
;;; done when Bellman-Ford is used for TSP, we can't arbitrarily choose the
;;; first city as a starting point. Rather, the algorithm has to run `n` times,
;;; where `n` is the number of cities. It then takes the smallest value from
;;; the resulting list. With small modifications, one can get the actual path
;;; taken as well.
;;;
;;; When run against the 8-city dataset for AoC, this implementation runs in
;;; roughly 10% of the time that the brute-force implementation does. Given
;;; that the brute-force approach iterates over all permutations of [1 .. n],
;;; it would grow significantly faster than this approach would for higher `n`
;;; values.
;;;
;;; Finally, note that this isn't a "typical" TSP approach: part 2 of the AoC
;;; puzzle was to find the MAXIMUM distance. For this reason, the `tsp` and
;;; `tsp-core` functions that mark the root of the actual algorithm take a
;;; function and comparison value in their parameter lists. For the usual
;;; (shortest-path) TSP, these are `min` and `Integer/MAX_VALUE`. For the part 2
;;; variation, `max` and `Integer/MIN_VALUE` are used.

(defn- parse-line [line]
  (let [[src _ dst _ dist] (str/split line #"\s+")]
    (list src dst (parse-long dist))))

;; Build the structure that represents the graph. Since the input is in the
;; form of city names and distances, first create a mapping of names to integer
;; indices. The indices will be used to create sets that represent the edges.
(defn- build-graph [tuples]
  (let [cities-list (sort (distinct (apply concat
                                           (map #(list (first %) (second %))
                                                tuples))))
        cities-map  (into {} (map hash-map cities-list (range)))]
    (loop [[[s d ds] & tuples] tuples, edges {}]
      (cond
        (nil? s) {:edges edges, :cities cities-map}
        :else    (recur tuples
                        (assoc edges #{(cities-map s) (cities-map d)} ds))))))

;; Create a list of the numbers from 0..(n-1) with m excluded.
(defn- n-sans-m [n m]
  (filter #(not= m %) (range n)))

;; Create a vector of the sets that are used as the `m` column indices in the
;; dynamic programming array.
(defn- create-sets [n m]
  (let [all-sets (map set (map (partial cons m) (comb/subsets (n-sans-m n m))))
        grouped  (group-by count all-sets)
        sets-vec (vec (repeat n nil))]
    (reduce (fn [ret x]
              (assoc ret x (grouped (inc x))))
            sets-vec (range n))))

;; Create an array column for the given list of sets. Use `template` as the
;; initial value of each column element. The original TSP code used `transient`
;; and `persistent!`, but they might be overkill here...
(defn- create-column [sets template]
  (persistent!
   (reduce (fn [ret x]
             (assoc! ret x template))
           (transient {}) sets)))

;; Calculate the final answer by scanning over the solutions for the full set
;; and taking the minimum value.
(defn- get-final-answer [f m-cur sets]
  (let [finals (m-cur (first sets))]
    (apply f (filter (comp not nil?) finals))))

;; Technically, *this* is the inner-most loop. But in the algorithm's
;; specification, it's just expressed as a minimum operation over the values
;; of s, excluding j. Here, we use `f` to determine what function to apply to
;; the numbers (`min` or `max`) because of the nature of the AoC problem here.
(defn- f-val-over-s [f m-prev s j weights]
  (let [s'       (disj s j)
        elements (sort s')]
    (loop [[k & ks] elements, values ()]
      (cond
        (nil? k) (apply f values)
        :else    (recur ks (cons (+ (get-in m-prev [s' k])
                                    (weights #{k j}))
                                 values))))))

;; Run the inner-most loop, the loop of j over the elements of set s, excluding
;; the value `st`.
(defn- j-loop [f st m-prev m-cur s weights]
  (let [js (sort (disj s st))]
    (loop [[j & js] js, m-cur m-cur]
      (cond
        (nil? j) m-cur
        :else    (recur js
                        (assoc-in m-cur [s j]
                                  (f-val-over-s f m-prev s j weights)))))))

;; Run the middle of the three loops, the loop over sets of size m:
(defn- sets-loop [f st m-prev m-cur sets weights]
  (loop [[s & ss] sets, m-cur m-cur]
    (cond
      (nil? s) m-cur
      :else    (recur ss (j-loop f st m-prev m-cur s weights)))))

;; This is the "core" of the Bellman-Ford application to TSP. It runs the outer
;; loop (the "m" loop) with calls to `sets-loop` for the middle loop. This was
;; originally the primary `tsp` function, until it was determined that it was
;; necessary to run a "meta-loop" over all the cities to determine the actual
;; correct answer.
(defn- tsp-core [n weights f s start]
  (let [;; The `template` for a given value of `start` puts `s` at index `start`
        template (assoc (vec (repeat n nil)) start s)
        ;; Create all the sets based on `n`, with `start` as the fixed element
        sets     (create-sets n start)
        ;; Initial `m-prev` has only #{start} as a set-key
        m-prev   (hash-map #{start} (assoc (vec (repeat n nil)) start 0))]
    (loop [[m & ms] (range 1 (inc n)), m-prev m-prev]
      (cond
        (nil? ms) (get-final-answer f m-prev (sets (dec n)))
        :else
        (let [m-cur (create-column (sets m) template)]
          (recur ms (sets-loop f start m-prev m-cur (sets m) weights)))))))

;; This is "meta-loop" of the algorithm, the part that controls calling the
;; algorithm proper for all possible start-points, then selects the best result
;; from the list that is generated.
(defn- tsp [f s graph]
  (let [n       (count (:cities graph))
        weights (:edges graph)]
    (apply f (map (partial tsp-core n weights f s) (range n)))))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       build-graph
       ;; Part 1 calls for the minimum path. So we pass `min` as the reducer
       ;; function and `Integer/MAX_VALUE` in place of +Infinity.
       (tsp min Integer/MAX_VALUE)))

(defn part-2
  "Day 09 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       build-graph
       ;; Part 2 calls for the maximum path. So we pass `max` as the reducer
       ;; function and `Integer/MIN_VALUE` in place of -Infinity.
       (tsp max Integer/MIN_VALUE)))
