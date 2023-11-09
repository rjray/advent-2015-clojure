(ns advent-of-code.day09bis
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
        (nil? s) {:edges edges, :cities cities-map}
        :else    (recur tuples
                        (assoc edges #{(cities-map s) (cities-map d)} ds))))))

;; Create a vector of the sets that are used as the first column indices in
;; the dynamic programming array.
(defn- create-sets [n]
  (let [all-sets (map set (map #(cons 1 %) (comb/subsets (range 2 (inc n)))))
        grouped  (group-by count all-sets)
        sets-vec (vec (repeat (inc n) nil))]
    (reduce (fn [ret x]
              (assoc ret x (grouped x)))
            sets-vec (range 1 (inc n)))))

;; Create an array column for the given list of sets. Start them with +Inf in
;; slot 1, the rest of the slots nil.
(defn- create-column [sets template]
  (persistent!
   (reduce (fn [ret x]
             (assoc! ret x template))
           (transient {}) sets)))

;; Calculate the final answer by scanning over the solutions for the full set
;; and taking the minimum value.
(defn- get-final-answer [m-cur sets]
  (let [finals (m-cur (first sets))]
    (apply min (filter #(not (nil? %)) finals))))

;; Technically, *this* is the inner-most loop. But in the algorithm's
;; specification, it's just expressed as a minimum operation over the values
;; of s, excluding j.
(defn- min-val-over-s [m-prev s j weights]
  (let [s'       (disj s j)
        elements (sort s')]
    (loop [[k & es] elements, values ()]
      (cond
        (nil? k) (apply min values)
        :else    (recur es (cons (+ (get-in m-prev [s' k])
                                    (weights #{k j}))
                                 values))))))

;; Run the inner-most loop, the loop of j over the elements of set s, excluding
;; the value 1.
(defn- j-loop [m-prev m-cur s weights]
  (let [elements (rest (sort s))]
    (loop [[j & es] elements, m-cur m-cur]
      (cond
        (nil? j) m-cur
        :else    (recur es
                        (assoc-in m-cur [s j]
                                  (min-val-over-s m-prev s j weights)))))))

;; Run the middle of the three loops, the loop over sets of size m:
(defn- sets-loop [m-prev m-cur sets weights]
  (loop [[s & ss] sets, m-cur m-cur]
    (cond
      (nil? s) m-cur
      :else    (recur ss (j-loop m-prev m-cur s weights)))))

(defn- tsp [graph]
  (let [n        (apply max (vals (:cities graph)))
        template (assoc (vec (repeat (inc n) nil)) 1 +Inf)
        sets     (create-sets n)
        m-prev   (hash-map #{1} (assoc (vec (repeat (inc n) nil)) 1 0))
        weights  (:edges graph)]
    (loop [m 2, m-prev m-prev]
      (cond
        (> m n) (get-final-answer m-prev (sets n))
        :else
        (let [m-cur (create-column (sets m) template)]
          (recur (inc m)
                 (sets-loop m-prev m-cur (sets m) weights)))))))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       build-graph
       tsp))

(defn part-2
  "Day 09 Part 2"
  [input]
  input)
