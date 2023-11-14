(ns advent-of-code.day21
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]
            [clojure.math.combinatorics :as comb]))

(def ^:private weapons
  [{:name "Dagger", :cost 8, :damage 4, :armor 0}
   {:name "Shortsword", :cost 10, :damage 5, :armor 0}
   {:name "Warhammer", :cost 25, :damage 6, :armor 0}
   {:name "Longsword", :cost 40, :damage 7, :armor 0}
   {:name "Greataxe", :cost 74, :damage 8, :armor 0}])

(def ^:private armor
  [{:name "NoArmor", :cost 0, :damage 0, :armor 0}
   {:name "Leather", :cost 13, :damage 0, :armor 1}
   {:name "Chainmail", :cost 31, :damage 0, :armor 2}
   {:name "Splintmail", :cost 53, :damage 0, :armor 3}
   {:name "Bandedmail", :cost 75, :damage 0, :armor 4}
   {:name "Platemail", :cost 102, :damage 0, :armor 5}])

(def ^:private rings
  [{:name "NoRing", :cost 0, :damage 0, :armor 0}
   {:name "Damage +1", :cost 25, :damage 1, :armor 0}
   {:name "Damage +2", :cost 50, :damage 2, :armor 0}
   {:name "Damage +3", :cost 100, :damage 3, :armor 0}
   {:name "Defense +1", :cost 20, :damage 0, :armor 1}
   {:name "Defense +2", :cost 40, :damage 0, :armor 2}
   {:name "Defense +3", :cost 80, :damage 0, :armor 3}])

(def ^:private all-combos
  (map flatten (comb/cartesian-product weapons
                                       armor
                                       (cons (repeat 2 (rings 0))
                                             (comb/combinations rings 2)))))

(def ^:private cost-table
  (sort-by :cost (map (fn [x]
                        {:name   (str/join ", " (map :name x))
                         :cost   (reduce + (map :cost x))
                         :damage (reduce + (map :damage x))
                         :armor  (reduce + (map :armor x))}) all-combos)))

(defn- boss-stats [data]
  ;; Nothing fancy. Line 1 is HP, 2 is damage, 3 is armor.
  (let [nums (map #(Integer/parseInt %) (re-seq #"\d+" data))]
    (into {} (map hash-map [:hp :damage :armor] nums))))

(defn- hit
  "p1 hits p2"
  [p1 p2]
  (update p2 :hp - (max 1 (- (:damage p1) (:armor p2)))))

(defn- fight [hp boss equip]
  (let [player {:hp hp, :damage (:damage equip), :armor (:armor equip)}]
    (loop [p player, b boss, pturn true]
      (cond
        (< (:hp p) 1) (list false equip)
        (< (:hp b) 1) (list true equip)
        pturn         (recur p (hit p b) false)
        :else         (recur (hit b p) b true)))))

(defn- best-fight [hp boss]
  (first (filter first (map #(fight hp boss %) cost-table))))

(defn part-1
  "Day 21 Part 1"
  [input]
  (->> input
       boss-stats
       (best-fight 100)))

(defn- worst-fight [hp boss]
  (first (filter #(not (first %))
                 (map #(fight hp boss %) (reverse cost-table)))))

(defn part-2
  "Day 21 Part 2"
  [input]
  (->> input
       boss-stats
       (worst-fight 100)))
