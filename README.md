# advent-2015-clojure

This is my code for the 2015 [Advent of Code](https://adventofcode.com/2015), all solutions in [Clojure](https://clojure.org/).

All code is under the `src` directory. Each solution-file is named `dayNN.clj` and contains both puzzle solutions for that day. These are the publically-facing functions `part-1` and `part-2`. These files are the code *exactly as I used it to solve and submit the answers*. If I revisit any of the days and try to clean up or optimize the solutions, that work will be in a separate file that will be named `dayNNbis.clj`. (Except that I will likely go back and comment the code after the fact, when I'm not racing the clock.)

The `resources` directory contains the input data for each day. These files are named for the day (`dayNN.txt`), and files with the example input are named `dayNN-example.txt`.

## Stats

Number of answers correct on first submission: 11/12 (92.67%)

Highest finish for first half: N/A

Highest finish for second half: N/A

## Usage

This project is managed with [Leiningen](https://leiningen.org/). Running the following will download any dependencies and start a REPL:

```
lein repl
```
