(ns advent-of-code.day16
  (:require [advent-of-code.utils :as u]))

(def ^:private data {:children 3,
                     :cats 7,
                     :samoyeds 2,
                     :pomeranians 3,
                     :akitas 0,
                     :vizslas 0,
                     :goldfish 5,
                     :trees 3,
                     :cars 2,
                     :perfumes 1})

(def ^:private cmp (into {} (map #(hash-map % =) (keys data))))

(defn- parse-line [line]
  (into {} (map #(hash-map (keyword (first %))
                           (Integer. (last %)))
                (partition 2 (re-seq #"\w+|\d+" line)))))

(defn- find-sue [cmps data sues]
  (letfn [(is-match? [s]
            (not (some #(= false %)
                       (map #((cmps %) (s %) (data %))
                            (rest (sort (keys s)))))))]
    (filter is-match? sues)))

(defn part-1
  "Day 16 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (find-sue cmp data)
       ((comp :Sue first))))

(defn part-2
  "Day 16 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (find-sue (assoc cmp :cats > :trees > :pomeranians < :goldfish <) data)
       ((comp :Sue first))))
