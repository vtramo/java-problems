package parallel_longest_sequence.parallelstream;

public record SequenceCounter<T>(
    T value,
    int repCounter,
    int length,
    boolean isFirstSequence,
    int firstSequenceLength,
    int longerLength,
    int lastSequenceLength,
    boolean isTheWholeSequence
) {
    public SequenceCounter(T value) {
        this(value, 0, 0, true, 0, 0, 0, false);
    }

    public SequenceCounter<T> accumulate(T elem) {
        final boolean match = value.equals(elem);
        final int newRepCounter = match ? this.repCounter + 1 : 0;
        return new SequenceCounter<>(
            value,
            newRepCounter,
            length + 1,
            match && isFirstSequence,
            match && isFirstSequence ? firstSequenceLength + 1 : firstSequenceLength,
            Math.max(newRepCounter, longerLength),
            newRepCounter,
            newRepCounter == length + 1
        );
    }

    public SequenceCounter<T> combine(SequenceCounter<T> sequenceCounter) {
        final int longerLength = Math.max(
            Math.max(
                this.lastSequenceLength() + sequenceCounter.firstSequenceLength(),
                this.longerLength()
            ),
            sequenceCounter.longerLength()
        );
        final boolean isTheWholeSequence = this.isTheWholeSequence() && sequenceCounter.isTheWholeSequence();
        return new SequenceCounter<>(
            value,
            repCounter,
            this.length() + sequenceCounter.length(),
            isFirstSequence,
            isTheWholeSequence ? longerLength : this.firstSequenceLength(),
            longerLength,
            isTheWholeSequence ? longerLength : sequenceCounter.lastSequenceLength(),
            isTheWholeSequence
        );
    }
}
