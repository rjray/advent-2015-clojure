(ns advent-of-code.day20
  (:require [advent-of-code.utils :as u]))

(defn- presents [n]
  (let [n (/ n 10)]
    (for [i (range 1 (inc n))
          j (range i (inc n) i)]
      (list j (* 10 i)))))

(defn- find-first [pres base]
  (let [h (vec (repeat (+ 2 (/ base 10)) 0))]
    (ffirst
     (drop-while #(< (last %) base)
                 (partition
                  2 (interleave (range)
                                (loop [[p & l] (pres base), h h]
                                  (cond
                                    (nil? p) h
                                    :else
                                    (recur l (update h (first p)
                                                     + (last p)))))))))))

(defn part-1
  "Day 20 Part 1"
  [input]
  (->> input
       u/to-lines
       first
       parse-long
       (find-first presents)))

(defn- presents11 [n]
  (let [n (/ n 10)]
    (for [i (range 1 (inc n))
          j (range i (inc n) i)
          :while (<= (/ j i) 50)]
      (list j (* 11 i)))))

(defn part-2
  "Day 20 Part 2"
  [input]
  (->> input
       u/to-lines
       first
       parse-long
       (find-first presents11)))
