(ns advent-of-code.day03
  (:require [advent-of-code.utils :as u]))

(defn- deliver-to-houses [grid input]
  (loop [[ch & chs] input, x 0, y 0, grid grid]
    (let [grid (assoc-in grid [[x y]] (inc (get grid [[x y]] 0)))]
      (cond
        (nil? ch) grid
        (= ch \^) (recur chs (inc x) y grid)
        (= ch \v) (recur chs (dec x) y grid)
        (= ch \<) (recur chs x (dec y) grid)
        (= ch \>) (recur chs x (inc y) grid)
        :else     "error"))))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> input
       (deliver-to-houses {})
       keys
       count))

(defn- new-loc [ch [x y]]
  (cond
    (= ch \^) (vec (list (inc x) y))
    (= ch \v) (vec (list (dec x) y))
    (= ch \<) (vec (list x (dec y)))
    (= ch \>) (vec (list x (inc y)))))

(defn- split-delivery [input]
  (loop [[[sch rch] & chs] (partition 2 input),
         santa [0 0], robot [0 0], grid {}]
    (let [grid (assoc-in grid [santa] (inc (get grid [santa] 0)))
          grid (assoc-in grid [robot] (inc (get grid [robot] 0)))]
      (cond
        (nil? sch) grid
        :else      (recur chs (new-loc sch santa) (new-loc rch robot) grid)))))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> input
       split-delivery
       keys
       count))
