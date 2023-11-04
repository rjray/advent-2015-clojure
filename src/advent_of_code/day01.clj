(ns advent-of-code.day01)

(defn part-1
  "Day 01 Part 1"
  [input]
  (loop [[ch & chs] input, floor 0]
    (cond
      (nil? ch) floor
      (= ch \() (recur chs (inc floor))
      (= ch \)) (recur chs (dec floor))
      :else     "error")))

(defn part-2
  "Day 01 Part 2"
  [input]
  (loop [[ch & chs] input, floor 0, idx 0]
    (cond
      (nil? ch)   floor
      (< floor 0) idx
      (= ch \()   (recur chs (inc floor) (inc idx))
      (= ch \))   (recur chs (dec floor) (inc idx))
      :else       "error")))
