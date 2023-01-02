package parallel_longest_sequence.forkjoin;

public record Result(
    int firstSequenceLength,
    int longerLength,
    int lastSequenceLength,
    boolean isTheWholeString
) {
}
