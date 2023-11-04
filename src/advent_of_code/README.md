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

## [day05.clj](day05.clj)

Day 5 (--/--).

## [day06.clj](day06.clj)

Day 6 (--/--).

## [day07.clj](day07.clj)

Day 7 (--/--).

## [day08.clj](day08.clj)

Day 8 (--/--).

## [day09.clj](day09.clj)

Day 9 (--/--).

## [day10.clj](day10.clj)

Day 10 (--/--).

## [day11.clj](day11.clj)

Day 11 (--/--).

## [day12.clj](day12.clj)

Day 12 (--/--).

## [day13.clj](day13.clj)

Day 13 (--/--).

## [day14.clj](day14.clj)

Day 14 (--/--).

## [day15.clj](day15.clj)

Day 15 (--/--).

## [day16.clj](day16.clj)

Day 16 (--/--).

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
