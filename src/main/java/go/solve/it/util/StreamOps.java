package go.solve.it.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public final class StreamOps {

    // usage: filter(single(T)), i.e. a stream consisting of at most 1 of given value
    public static <T> Predicate<T> single(final T value) {
        final AtomicBoolean found = new AtomicBoolean();
        return any -> {
            if (found.get()) {
                return false;
            }
            found.set(value.equals(any));
            return found.get();
        };
    }
}
