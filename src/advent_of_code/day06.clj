(ns advent-of-code.day06
  (:require [advent-of-code.utils :as u]))

(def ^:private line-re #"(on|off|toggle)\s+(\d+),(\d+) through (\d+),(\d+)")

(defn- make-grid [val]
  (vec (repeat 1000 (vec (repeat 1000 val)))))

(defn- parse-line [line]
  (let [parts (rest (re-find line-re line))]
    (cons (first parts) (map parse-long (rest parts)))))

(defn- adjust [grid x1 y1 x2 y2 f]
  (let [lights (for [y (range (min y1 y2) (inc (max y1 y2)))
                     x (range (min x1 x2) (inc (max x1 x2)))]
                 [y x])]
    (loop [[light & lights] lights, grid grid]
      (cond
        (nil? light) grid
        :else        (recur lights (update-in grid light f))))))

(defn- manage-lights [grid on off toggle lines]
  (loop [[[op x1 y1 x2 y2] & lines] lines, grid grid]
    (case op
      "on"     (recur lines (adjust grid x1 y1 x2 y2 on))
      "off"    (recur lines (adjust grid x1 y1 x2 y2 off))
      "toggle" (recur lines (adjust grid x1 y1 x2 y2 toggle))
      ;; Default, when op is nil.
      grid)))

(defn part-1
  "Day 06 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (manage-lights (make-grid false)
                      {true true, false true}
                      {true false, false false}
                      {true false, false true})
       flatten
       (filter identity)
       count))

(defn part-2
  "Day 06 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (manage-lights (make-grid 0)
                      inc (fn [x] (max 0 (dec x))) (fn [x] (+ 2 x)))
       flatten
       (reduce +)))
