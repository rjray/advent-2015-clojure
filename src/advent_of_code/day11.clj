(ns advent-of-code.day11
  (:require [advent-of-code.utils :as u]))

(defn- l-to-i [l] (- (int l) 97))

(defn- i-to-l [i] (char (+ 97 i)))

(defn- pw-to-vec [p] (mapv l-to-i p))

(defn- vec-to-pw [v] (apply str (map i-to-l v)))

(def ^:private disallowed (set (map l-to-i '(\i \o \l))))

(def ^:private lmap (reduce (fn [c i]
                              (update c i inc))
                            (mapv (comp #(mod % 26) inc) (range 26))
                            (map l-to-i '(\h \k \n))))

(defn- valid-triple? [t]
  (= '(-1 -1) (map #(apply - %) (partition 2 1 t))))

(defn- has-doubles? [pwv]
  (< 1 (count (distinct (filter #(apply = %) (partition 2 1 pwv))))))

(defn- valid? [pwv]
  (and (not (some disallowed pwv))
       (some valid-triple? (partition 3 1 pwv))
       (has-doubles? pwv)))

(defn- rotate [pwv]
  (loop [[ltr & ltrs] (reverse pwv), newpw (), carry true]
    (cond
      (nil? ltr) (vec newpw)
      carry      (let [newc (lmap ltr)
                       carry (zero? newc)]
                   (recur ltrs (cons newc newpw) carry))
      :else      (recur ltrs (cons ltr newpw) carry))))

(defn- rotate-until-valid [pwv]
  (loop [pwv (rotate pwv)]
    (cond
      (valid? pwv) pwv
      :else        (recur (rotate pwv)))))

(defn part-1
  "Day 11 Part 1"
  [input]
  (->> input
       u/to-lines
       first
       pw-to-vec
       rotate-until-valid
       vec-to-pw))

(defn part-2
  "Day 11 Part 2"
  [input]
  (->> input
       u/to-lines
       first
       pw-to-vec
       rotate-until-valid
       rotate-until-valid
       vec-to-pw))
