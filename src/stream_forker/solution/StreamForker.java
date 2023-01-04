package stream_forker.solution;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamForker<T> {

    private final Stream<T> stream;
    private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }

    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
        forks.put(key, f);
        return this;
    }

    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        } finally {
            consumer.finish();
        }
        return consumer;
    }

    private ForkingStreamConsumer<T> build() {
        List<BlockingQueue<T>> queues = new ArrayList<>();

        Map<Object, Future<?>> actions =
            forks.entrySet().stream().reduce(
                new HashMap<Object, Future<?>>(),
                (map, entry) -> {
                    map.put(entry.getKey(), getOperationResult(queues, entry.getValue()));
                    return map;
                },
                (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                }
            );

        return new ForkingStreamConsumer<>(queues, actions);
    }

    private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
        Stream<T> source = StreamSupport.stream(spliterator, false);
        return CompletableFuture.supplyAsync(() -> f.apply(source));
    }

    public static void main(String[] args) {
        final Stream<Integer> stream = Stream.<Integer>of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Results results = new StreamForker<Integer>(stream)
            .fork("x2", e -> e.map(i -> i * 2).toList())
            .fork("total", e -> e.mapToInt(i -> i).sum())
            .fork("theGreatest", e -> e.mapToInt(i -> i).max())
            .fork("onlyEven", e -> e.filter(i -> i % 2 == 0).toList())
            .getResults();
        System.out.println("x2 " + results.get("x2"));
        System.out.println("total " + results.get("total"));
        System.out.println("theGreatest " + ((OptionalInt) results.get("theGreatest")).getAsInt());
        System.out.println("onlyEven " + results.get("onlyEven"));
    }
}

