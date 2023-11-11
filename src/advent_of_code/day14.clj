(ns advent-of-code.day14
  (:require [advent-of-code.utils :as u]))

(def ^:private line-re #"^(\w+).*?(\d+).*?(\d+).*?(\d+).*$")

(defn- parse-line [line]
  (let [[_ name speed endurance rest-time] (re-matches line-re line)]
    {:name (keyword name)
     :spd (Integer/parseInt speed)
     :end (Integer/parseInt endurance)
     :win (+ (Integer/parseInt endurance) (Integer/parseInt rest-time))}))

(defn- dist [t flyer]
  (let [{name :name, spd :spd, end :end, win :win} flyer
        rounds   (quot t win)
        left     (rem t win)
        distance (* spd end rounds)
        extra    (if (>= left end)
                   (* end spd)
                   (* left spd))]
    (list name (+ distance extra))))

(defn- get-fastest [t flyers]
  (last (sort-by last (map #(dist t %) flyers))))

(defn part-1
  "Day 14 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (get-fastest 2503)))

(defn- score [t scores flyers]
  (let [positions (group-by last (map #(dist t %) flyers))
        leads     (map first (positions (apply max (keys positions))))]
    (loop [[l & ls] leads, scores scores]
      (cond
        (nil? l) scores
        :else    (recur ls (update scores l inc))))))

(defn- race [t flyers]
  (let [names  (map :name flyers)
        scores (into {} (map hash-map names (repeat 0)))]
    (loop [time 1, scores scores]
      (cond
        (> time t) (last (sort-by last scores))
        :else      (recur (inc time) (score time scores flyers))))))

(defn part-2
  "Day 14 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (race 2503)))
