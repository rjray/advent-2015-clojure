(ns advent-of-code.day04
  (:import [java.security MessageDigest]
           [java.math BigInteger]))

(def ^:private MD5 (MessageDigest/getInstance "MD5"))

;; Taken from comments on https://gist.github.com/jizhang/4325757
(defn- md5 [^String s]
  (let [raw (.digest MD5 (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn- is-match [lead s n]
  (= lead (take (count lead) (md5 (format "%s%d" s n)))))

(defn- find-smallest [input match-to]
  (first (filter #(is-match match-to input %) (range))))

(defn part-1
  "Day 05 Part 1"
  [input]
  (find-smallest input '(\0 \0 \0 \0 \0)))

(defn part-2
  "Day 05 Part 2"
  [input]
  (find-smallest input '(\0 \0 \0 \0 \0 \0)))
