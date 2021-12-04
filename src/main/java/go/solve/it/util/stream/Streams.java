package go.solve.it.util.stream;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class Streams {

    public static <T> Stream<T> stream(final T... elements) {
        return Stream.of(elements);
    }

    public static <I, O> Stream<O> snakeMap(final Stream<I> stream, final O head, final BiFunction<? super O, ? super I, ? extends O> tailMapping) {
        final var previous = new AtomicReference<>(head);
        return stream.map(input -> {
            final O next = tailMapping.apply(previous.get(), input);
            previous.set(next);
            return next;
        });
    }

    @SafeVarargs
    public static <T> Stream<T> concat(final Stream<T> left, final T... right) {
        return Stream.concat(left, Stream.of(right));
    }
}
