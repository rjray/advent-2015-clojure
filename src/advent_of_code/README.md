# Breakdown of Files

Jump to day: [1](#day01clj)&nbsp;|&nbsp;[2](#day02clj)&nbsp;|&nbsp;[3](#day03clj)&nbsp;|&nbsp;[4](#day04clj)&nbsp;|&nbsp;[5](#day05clj)&nbsp;|&nbsp;[6](#day06clj)&nbsp;|&nbsp;[7](#day07clj)&nbsp;|&nbsp;[8](#day08clj)&nbsp;|&nbsp;[9](#day09clj)&nbsp;|&nbsp;[10](#day10clj)&nbsp;|&nbsp;[11](#day11clj)&nbsp;|&nbsp;[12](#day12clj)&nbsp;|&nbsp;[13](#day13clj)&nbsp;|&nbsp;[14](#day14clj)&nbsp;|&nbsp;[15](#day15clj)&nbsp;|&nbsp;[16](#day16clj)&nbsp;|&nbsp;[17](#day17clj)&nbsp;|&nbsp;[18](#day18clj)&nbsp;|&nbsp;[19](#day19clj)&nbsp;|&nbsp;[20](#day20clj)&nbsp;|&nbsp;[21](#day21clj)&nbsp;|&nbsp;[22](#day22clj)&nbsp;|&nbsp;[23](#day23clj)&nbsp;|&nbsp;[24](#day24clj)&nbsp;|&nbsp;[25](#day25clj)

Here is a breakdown of the various files in this directory. Files with names of the form `dayNN.clj` represent the code actually used to solve the problems (with some tweaking done using a static analysis plug-in for Leinengen). Files with `bis` in the name are modified/tuned versions of the given original day. (If you see comments in a file, I can promise you they were added after the fact.)

The numbers in parentheses in the descriptions of the files represent the rank I had for when my solutions were submitted and accepted. Time, if given, is a rough estimate of how long it took to solve both halves.

(Since this was done in 2023, there are no rankings or times recorded.)

## [day01.clj](day01.clj)

Day 1 (--/--).

Simple parsing of parentheses, a la S-expressions in a way. Part 2 was just creating an invariant to exit as soon as the floor-number dropped below 0.

## [day02.clj](day02.clj)

Day 2 (--/--).

Still simple, so far. Part 1 was just math-processing, and part 2 was different math-processing. In part 1, I initially wanted to have two utility defn's, one to split the numbers from the line and one to do the calculation. I ended up bundling them together, and then part 2 would have been a snap if I had kept the separate defn for splitting numbers. But then, after the fact, I remembered having exactly this utility function in `utils.clj`...

## [day02bis.clj](day02bis.clj)

...so I went ahead and made an improvement file. It just replaces the explicit `defn` that split the numbers with calls to `parse-out-longs` from `utils.clj`, and adds some comments.

## [day03.clj](day03.clj)

Day 3 (--/--).

Here, part 1 was simple as expected, but I anticipated the wrong twist for part 2. As a result, part 2 wasn't able to reuse very much from part 1.

## [day04.clj](day04.clj)

Day 4 (--/--).

Part 1 went fine, but a small logic error kept my part 2 from working for the longest time. I looked over past solutions, which told me that the answer shouldn't have taken too terribly long, which in turn told me it was in my code.

There is probably a more algortihmic way of solving either of these parts, but this worked.

## [day05.clj](day05.clj)

Day 5 (--/--).

Simple regexp applications.

## [day06.clj](day06.clj)

Day 6 (--/--).

The first somewhat-tricky one. Part 1 went fine, but part 2 tricked me (causing my first wrong answer already).

## [day07.clj](day07.clj)

Day 7 (--/--).

This took much too long. Parsing was done easily, but the challenge was in how to evaluate the "circuit" while updating the individual input points.

## [day08.clj](day08.clj)

Day 8 (--/--).

This was a much easier task, and was easily done with careful use of regular expression match-and-replace.

## [day09.clj](day09.clj)

Day 9 (--/--).

This was a train-wreck, in that it is clearly a variation of the Traveling Salesman Problem so I pulled out code from the Stanford Algorithms Specialization classes I took through Coursera years ago. This gave me a wrong answer for part 1, because that algorithm (the dynamic programming approach to TSP) was a slightly different problem than this one.

I solved this with brute force, and part 2 turned out to be trivial to do given Clojure's functional nature. See [day09bis.clj](day09bis.clj) (next) for the notes on the actual DP approach. It needs some rewriting before I can get it working.

## [day09bis.clj](day09bis.clj)

This file has the dynamic programming approach, adapted from code I wrote some years ago. It is currently incomplete.

## [day10.clj](day10.clj)

Day 10 (--/--).

After the toils of day 9, this was extremely easy. A large part of that was due to Clojure's core `partition-by` primitive. This made the total file-length the shortest since day 1, line-wise.

## [day11.clj](day11.clj)

Day 11 (--/--).

This was some challenging string-manipulation for faux password generation. It turned out to be much easier when converting the letters to numeric values, doing the processing, then converting them back to letters. There was a certain trickiness to it, in that had I not read the description and examples extremely carefully I might have gotten another wrong answer.

## [day12.clj](day12.clj)

Day 12 (--/--).

This was parsing out numerical values in JSON. I used `clojure.data.json` for the parsing. In part 1, I originally solved it by just pulling all the numbers out via `re-seq` and totalling them. It wasn't until part 2 that I had to "really" work.

For part 2, I used `clojure.walk/prewalk` to locate every object that had a "red" value for any key and replace that object with a 0. This required the parsed JSON, which was then passed to a recursive fn to walk the data and gather the numbers. To test this, I rewrote part 1 to use parsed JSON and the `sum-up` fn.

So what is committed here isn't quite what I submitted the answer for part 1 with. But it got the right answer for part 1 and then part 2.

## [day13.clj](day13.clj)

Day 13 (--/--).

Taking a cue from day 9, I didn't try to finesse this one at all. Simple brute-force over the permuations of the dinner guests. This time, though, I did optimize a little bit by not calculating permutations that were exact reverses of an existing (already calculated) permuation.

Part 2 was just to add yourself in, with all "weights" between you and others being 0. While this took the permutations from 8! to 9!, the fact that I was only calculating half of them combined with Clojure's lazy sequences made quick work.

## [day14.clj](day14.clj)

Day 14 (--/--).

An interesting pair of problems. Part 1 was to see which of 9 reindeer would fly the farthest in a given timespan, based on their flying speed, endurance, and rest-time needed between flying periods. Doing it was pretty straightforward, though an error in the math of my algorithm cost me in terms of my first submission being wrong.

Part 2 was only a little trickier; instead of finding the one who'd flown the farthest, Santa wanted to use a points system to find the one that was in the lead the most often, at one-second intervals. Scoring was one point for each racer per second that they were in the lead, with the possibility that there might be a tie for the lead at any given `t`. At first I was concerned that the data structure I had chosen to represent the reindeer might be clumsy for part 2, but in the end it worked out fine.

I am a little annoyed, though, that I've made three incorrect submissions already by day 14.

## [day15.clj](day15.clj)

Day 15 (--/--).

This was an optimization problem of a form I haven't seen before. I'll need to find out exactly what, so I can understand it better.

For the solutions, I borrowed a `partitions` fn from the reddit solutions thread for this day. That produced all the combinations of measurements for the ingredients based on the assumption that everything had to appear at least once.

Part 1 was just a matter of scoring each mix and finding the maximum score. I used the `partial` primitive several times for this, the first time I've really used it extensively.

Part 2 upped the ante by requiring that only recipes that produced exactly 500 calories be considered. This was pretty similar to part 1, only requiring that each score also have a calorie value associated with it. Once those were calculated, `group-by` was used to group them by the calorie score. All the matches for 500 were then sorted by score and the highest one taken.

## [day16.clj](day16.clj)

Day 16 (--/--).

This was basically a field-based "grep" problem. Part 1 was solved with simple equality checks, but part 2 called for four of the fields to have different checks. So the first solution was slightly altered to use a map of the search-field keywords to the `=` function, then for the second part `assoc` was used to set the four keywords to either `<` or `>` as needed.

## [day17.clj](day17.clj)

Day 17 (--/--).

## [day18.clj](day18.clj)

Day 18 (--/--).

## [day19.clj](day19.clj)

Day 19 (--/--).

## [day20.clj](day20.clj)

Day 20 (--/--).

## [day21.clj](day21.clj)

Day 21 (--/--).

## [day22.clj](day22.clj)

Day 22 (--/--).

## [day23.clj](day23.clj)

Day 23 (--/--).

## [day24.clj](day24.clj)

Day 24 (--/--).

## [day25.clj](day25.clj)

Day 25 (--/--).
