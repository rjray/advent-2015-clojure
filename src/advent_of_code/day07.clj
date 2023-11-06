(ns advent-of-code.day07
  (:require [advent-of-code.utils :as u]))

(def ^:private line-re
  (re-pattern
   (str "(?:([a-z0-9]+) -> ([a-z]+))" "|"
        "(?:([a-z0-9]+) (AND|OR|LSHIFT|RSHIFT) ([a-z0-9]+) -> ([a-z]+))" "|"
        "(?:(NOT) ([a-z0-9]+) -> ([a-z]+))")))

;; This will force numeric values to unsigned 16-bit
(defn- mask [x] (bit-and x 0xffff))

(def ^:private op-table {"AND"    (fn [a b] (bit-and a b)),
                         "OR"     (fn [a b] (bit-or a b)),
                         "LSHIFT" (fn [a b] (mask (bit-shift-left a b))),
                         "RSHIFT" (fn [a b] (mask (bit-shift-right a b))),
                         "NOT"    (fn [_ b] (mask (bit-not b)))})

(defn- num-or-kw [val]
  (if (re-matches #"\d+" val)
    (parse-long val)
    (keyword val)))

(defn- parse-line [line]
  (let [parts (vec (rest (re-matches line-re line)))]
    (cond
      (not (nil? (parts 0))) {(keyword (parts 1)) {:value
                                                   (num-or-kw (parts 0))}}
      (not (nil? (parts 2))) {(keyword (parts 5)) {:value (op-table (parts 3)),
                                                   :lt (num-or-kw (parts 2)),
                                                   :rt (num-or-kw (parts 4))}}
      (not (nil? (parts 6))) {(keyword (parts 8)) {:value (op-table (parts 6)),
                                                   :lt 0,
                                                   :rt (num-or-kw (parts 7))}}
      :else                  "error")))

;; Resolve all posts in the circuit.
(defn- resolve-all [circuit]
  (let [posts (vec (sort (keys circuit)))]
    (loop [[p & ps] posts, circuit circuit]
      (cond
        (nil? p) circuit
        :else
        (let [r (circuit p), v (:value r)]
          (cond
            (number? v)  (recur ps circuit)
            (keyword? v) (let [r' (circuit v)]
                           (if (number? (:value r'))
                             (recur ps (assoc-in circuit
                                                 [p :value] (:value r')))
                             (recur (conj (vec ps) p) circuit)))
            :else
            ;; :value is a fn
            (if (and (number? (:lt r)) (number? (:rt r)))
              (recur ps (assoc-in circuit [p :value] (v (:lt r) (:rt r))))
              (let [lt (:lt r), rt (:rt r)]
                (if (number? lt)
                  ;; If lt is a number then rt must have been a keyword
                  (let [rt' (get-in circuit [rt :value])]
                    (if (number? rt')
                      (recur (conj (vec ps) p) (assoc-in circuit [p :rt] rt'))
                      ;; The value of :rt isn't ready yet.
                      (recur (conj (vec ps) p) circuit)))
                  ;; Then rt must be a number and lt is a keyword
                  (let [lt' (get-in circuit [lt :value])]
                    (if (number? lt')
                      (recur (conj (vec ps) p) (assoc-in circuit [p :lt] lt'))
                      ;; The value of :lt isn't ready yet.
                      (recur (conj (vec ps) p) circuit))))))))))))

(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-line)
       (into {})
       resolve-all
       :a
       :value))

(defn part-2
  "Day 07 Part 2"
  [input]
  (let [a        (part-1 input),
        circuit  (->> input
                      u/to-lines
                      (map parse-line)
                      (into {}))
        circuit' (assoc-in circuit [:b :value] a)]
    (get-in (resolve-all circuit') [:a :value])))
