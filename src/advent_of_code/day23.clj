(ns advent-of-code.day23
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(def ^:private op-pattern
  (re-pattern (str/join "|"
                        (list "(?:(hlf)\\s+([ab]))"
                              "(?:(tpl)\\s+([ab]))"
                              "(?:(inc)\\s+([ab]))"
                              "(?:(jmp)\\s+([-+]\\d+))"
                              "(?:(jie)\\s+([ab]),\\s+([-+]\\d+))"
                              "(?:(jio)\\s+([ab]),\\s+([-+]\\d+))"))))

;; Stepper fn for opcodes that don't jump around
(defn- step [state] (update state :pc inc))

(def ^:private opcodes
  {:hlf (fn [state r]     (step (update state r quot 2)))
   :tpl (fn [state r]     (step (update state r * 3)))
   :inc (fn [state r]     (step (update state r inc)))
   :jmp (fn [state off]   (update state :pc + off))
   :jie (fn [state r off] (if (even? (state r))
                            (update state :pc + off)
                            (step state)))
   :jio (fn [state r off] (if (= (state r) 1)
                            (update state :pc + off)
                            (step state)))})

(defn- parse-line [line]
  (let [[_ & ops] (re-find op-pattern line)]
    (map #(if (re-matches #"[-+]\d+" %)
            (Integer/parseInt %)
            (keyword %))
         (filter (comp not nil?) ops))))

(defn- parse-lines [lines] (mapv parse-line lines))

(defn- exec [state op]
  (let [[ins & args] op
        call-this    (opcodes ins)]
    (apply call-this (cons state args))))

(defn- run [state code]
  (loop [state state]
    (let [op (get code (:pc state))]
      (cond
        (nil? op) state
        :else     (recur (exec state op))))))

(defn part-1
  "Day 23 Part 1"
  [input]
  (->> input
       u/to-lines
       parse-lines
       (run {:pc 0, :a 0, :b 0})
       :b))

(defn part-2
  "Day 23 Part 2"
  [input]
  (->> input
       u/to-lines
       parse-lines
       (run {:pc 0, :a 1, :b 0})
       :b))
