package parallel_longest_sequence.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.RecursiveTask;

public class LongestSequenceForkJoinRecursiveTask<T> extends RecursiveTask<Result> {

    private static final long THRESHOLD = 5;

    private final List<T> list;
    private final T value;
    private final int start, end;

    public LongestSequenceForkJoinRecursiveTask(List<T> list, T value) {
        this(list, value, 0, list.size());
    }

    private LongestSequenceForkJoinRecursiveTask(List<T> list, T value, int start, int end) {
        this.list  = (list instanceof RandomAccess) ? list : new ArrayList<>(list);
        this.value = value;
        this.start = start;
        this.end   = end;
    }

    @Override
    protected Result compute() {
        final int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially(value);
        }
        final var leftTask = new LongestSequenceForkJoinRecursiveTask<>(list, value, start, start + length / 2);
        leftTask.fork();
        final var rightTask = new LongestSequenceForkJoinRecursiveTask<>(list, value, start + length / 2, end);
        final Result rightResult = rightTask.compute();
        final Result leftResult = leftTask.join();
        return combine(leftResult, rightResult);
    }

    private Result computeSequentially(final T value) {
        int leftSequenceLength = 0, maxRep = 0, rightSequenceLength = 0;
        int repCounter = 0;

        int i = start;
        while (i < end) {
            final var elem = list.get(i);
            if (elem.equals(value)) {
                repCounter++;
                i++;
            } else {
                maxRep = leftSequenceLength = repCounter;
                break;
            }
        }

        if (i >= end) {
            maxRep = leftSequenceLength = rightSequenceLength = repCounter;
            return new Result(leftSequenceLength, maxRep, rightSequenceLength);
        }

        repCounter = 0;
        for (; i < end; i++) {
            final var elem = list.get(i);
            if (elem.equals(value)) {
                repCounter++;
            } else {
                repCounter = (i + 1 == end) ? repCounter : 0;
            }
            maxRep = Math.max(repCounter, maxRep);
        }
        rightSequenceLength = repCounter;

        return new Result(leftSequenceLength, maxRep, rightSequenceLength);
    }

    private Result combine(final Result leftResult, final Result rightResult) {
        final int longerLength = Math.max(
            Math.max(
                leftResult.lastSequenceLength() + rightResult.firstSequenceLength(),
                leftResult.longerLength()
            ),
            rightResult.longerLength()
        );
        return new Result(0, longerLength, 0);
    }
}
