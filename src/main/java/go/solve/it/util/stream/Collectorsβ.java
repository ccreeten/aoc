package go.solve.it.util.stream;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toCollection;

public final class Collectorsβ {

    public static <T> Collector<T, ?, Deque<T>> toDeque() {
        return toCollection(ArrayDeque::new);
    }
}
