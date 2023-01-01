# Parallel Longest Sequence

Consider the problem of finding the longest sequence of some number in a list of numbers. 
The class `LongestSequenceCalculator<T>` has a constructor `LongestSequenceCalculator(List<T> list)` that receives as 
input a `List<T> list` of elements of type `T`. This class provides a method `parallelLongestSequence(T value)` that returns 
the longest number of consecutive `value` in `list`.
For example, if `list` is `[2, 17, 17, 8, 17, 17, 17, 0, 17, 1]` (a subtype of `List<Integer>`) then `parallelLongestSequence(17)` 
is `3` and `parallelLongestSequence(9)` is `0`. The method must solve the task in parallel.
The combination of two subproblems may turn out to be more complicated than expected.
You have to worry about the challenging possibility that the longest sequence was in the middle of the list, 
and that you split it when dividing the problem into two halves. 

Source: [ForkJoin Framework Exercises www.carleton.edu/computer-science/](https://www.cs.carleton.edu/faculty/dmusican/cs348/forkjoin/#orgd088b8d)