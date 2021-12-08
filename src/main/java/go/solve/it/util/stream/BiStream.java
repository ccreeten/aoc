package go.solve.it.util.stream;

import go.solve.it.util.container.Tuple;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public final class BiStream<L, R> {

    private final Stream<Tuple<L, R>> delegate;

    private BiStream(final Stream<Tuple<L, R>> delegate) {
        this.delegate = delegate;
    }

    public static <L, R> BiStream<L, R> zip(final Stream<? extends L> left, final Stream<? extends R> right) {
        return new BiStream<>(Streams.zip(left, right, Tuple::of));
    }

    public static <K, V> BiStream<K, V> biStream(final Map<? extends K, ? extends V> map) {
        return new BiStream<>(map.entrySet().stream().map(entry -> Tuple.of(entry.getKey(), entry.getValue())));
    }

    public static BiStream<Integer, Integer> streamPositions(final char[][] grid) {
        return new BiStream<>(
                range(0, grid.length)
                        .boxed()
                        .flatMap(row -> range(0, grid[row].length).mapToObj(col -> Tuple.ofInts(row, col)))
        );
    }

    public static BiStream<Integer, Integer> streamPositions(final Object[][] grid) {
        return new BiStream<>(
                range(0, grid.length)
                        .boxed()
                        .flatMap(row -> range(0, grid[row].length).mapToObj(col -> Tuple.ofInts(row, col)))
        );
    }

    public static <T> BiStream<Integer, T> streamIndexed(final T... values) {
        return new BiStream<>(range(0, values.length).mapToObj(index -> Tuple.of(index, values[index])));
    }

    public Stream<L> lefts() {
        return map((left, right) -> left);
    }

    public BiStream<L, R> filter(final BiPredicate<? super L, ? super R> predicate) {
        return new BiStream<>(delegate.filter(tuple -> predicate.test(tuple.head(), tuple.tail())));
    }

    public <T> Stream<T> map(final BiFunction<? super L, ? super R, ? extends T> mapping) {
        return delegate.map(tuple -> mapping.apply(tuple.head(), tuple.tail()));
    }

    public long count() {
        return delegate.count();
    }

    public void forEachOrdered(final BiConsumer<? super L, ? super R> handler) {
        delegate.forEachOrdered(tuple -> handler.accept(tuple.head(), tuple.tail()));
    }

    public Map<L, R> toMap() {
        return delegate.collect(Collectors.toMap(Tuple::head, Tuple::tail));
    }
}
