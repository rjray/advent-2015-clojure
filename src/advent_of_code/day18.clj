(ns advent-of-code.day18
  (:require [advent-of-code.utils :as u]))

(def ^:private char-map {\. 0, \# 1})

(defn- run [n grid]
  (let [sy        (count grid)
        sx        (count (first grid))
        neighbors (fn [g y x]
                    (for [y' (range (dec y) (+ y 2))
                          x' (range (dec x) (+ x 2))]
                      (cond
                        (and (= x x')
                             (= y y')) 0
                        :else          (get-in g [y' x'] 0))))
        newval    (fn [g y x]
                    (let [n (reduce + (neighbors g y x))]
                      (case (get-in g [y x])
                        0 (if (= 3 n)   1 0)
                        1 (if (< 1 n 4) 1 0))))]
    (loop [i 0, grid grid]
      (cond
        (= i n) grid
        :else   (recur (inc i) (mapv vec (for [y (range sy)]
                                           (for [x (range sx)]
                                             (newval grid y x)))))))))

(defn part-1
  "Day 18 Part 1"
  [input]
  (->> input
       u/to-matrix
       (mapv #(mapv char-map %))
       (run 100)
       flatten
       (reduce +)))

(defn- run2 [n grid]
  (let [sy        (count grid)
        sy'       (dec sy)
        sx        (count (first grid))
        sx'       (dec sx)
        neighbors (fn [g y x]
                    (for [y' (range (dec y) (+ y 2))
                          x' (range (dec x) (+ x 2))]
                      (cond
                        (and (= x x')
                             (= y y')) 0
                        :else          (get-in g [y' x'] 0))))
        newval    (fn [g y x]
                    (let [n (reduce + (neighbors g y x))]
                      (cond
                        (and (= 0 y)
                             (= 0 x))   1
                        (and (= 0 y)
                             (= sx' x)) 1
                        (and (= sy' y)
                             (= 0 x))   1
                        (and (= sy' y)
                             (= sx' x)) 1
                        :else           (case (get-in g [y x])
                                          0 (if (= 3 n)   1 0)
                                          1 (if (< 1 n 4) 1 0)))))]
    (loop [i 0, grid grid]
      (cond
        (= i n) grid
        :else   (recur (inc i) (mapv vec (for [y (range sy)]
                                           (for [x (range sx)]
                                             (newval grid y x)))))))))

(defn- set-corners [grid]
  (let [sy (dec (count grid)), sx (dec (count (first grid)))]
    (assoc-in (assoc-in (assoc-in (assoc-in grid [0 0] 1) [0 sx] 1) [sy 0] 1)
              [sy sx] 1)))

(defn part-2
  "Day 18 Part 2"
  [input]
  (->> input
       u/to-matrix
       (mapv #(mapv char-map %))
       set-corners
       (run2 100)
       flatten
       (reduce +)))
