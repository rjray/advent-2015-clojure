(ns advent-of-code.day02
  (:require [advent-of-code.utils :as u]))

(defn- get-dimensions [line]
  (map parse-long (re-seq #"\d+" line)))

(defn- compute-paper [line]
  (let [[l w h ] (get-dimensions line)
        a        (* l w)
        b        (* w h)
        c        (* l h)]
    (+ (* 2 a) (* 2 b) (* 2 c) (min a b c))))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> input
       u/to-lines
       (map compute-paper)
       (apply +)))

(defn- compute-ribbon [[l w h]]
  (let [face1 (+ l l w w)
        face2 (+ w w h h)
        face3 (+ l l h h)
        vol   (* l w h)]
    (+ vol (min face1 face2 face3))))

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> input
       u/to-lines
       (map get-dimensions)
       (map compute-ribbon)
       (apply +)))
