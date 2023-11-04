(ns advent-of-code.day02bis
  (:require [advent-of-code.utils :as u]))

;; The main reason for using `let` to pre-compute the 3 faces is so that they
;; can be passed to `min` as well.
(defn- compute-paper [[l w h]]
  (let [a        (* l w)
        b        (* w h)
        c        (* l h)]
    (+ (* 2 a) (* 2 b) (* 2 c) (min a b c))))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (map compute-paper)
       (apply +)))

;; Computing the ribbon is also easy. Again, the three "faces" are pre-computed
;; to make the `min` expr less noisy.
(defn- compute-ribbon [[l w h]]
  (let [face1 (+ l l w w)
        face2 (+ w w h h)
        face3 (+ l l h h)]
    (+ (* l w h) (min face1 face2 face3))))

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (map compute-ribbon)
       (apply +)))
