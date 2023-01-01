package parallel_longest_sequence;

import parallel_longest_sequence.forkjoin.LongestSequenceForkJoinRecursiveTask;
import parallel_longest_sequence.forkjoin.Result;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

public class LongestSequenceCalculator<T> {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final List<T> list;
    public LongestSequenceCalculator(List<T> list) {
        Objects.requireNonNull(list);
        this.list = list;
    }

    public int longestSequence(T value) {
        int maxRep = 0;
        int repCounter = 0;
        for (T elem: list) {
            repCounter = (elem.equals(value)) ?
                repCounter + 1 :
                0;
            maxRep = Math.max(repCounter, maxRep);
        }
        return maxRep;
    }

    public int parallelLongestSequence(T value) {
        final var forkJoinTask = new LongestSequenceForkJoinRecursiveTask<T>(list, value);
        final Result result = forkJoinPool.invoke(forkJoinTask);
        return result.longerLength();
    }

    public static void main(String[] args) {
        final var list = List.of(2, 17, 17, 8, 17, 17, 17, 0, 17, 1);
        final int value = 17;
        final var longestSequenceCalculator = new LongestSequenceCalculator<Integer>(list);
        System.out.println(longestSequenceCalculator.longestSequence(value));
        System.out.println(longestSequenceCalculator.parallelLongestSequence(value));
    }
}
