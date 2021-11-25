package go.solve.it.util.primitive;

import static java.util.stream.StreamSupport.stream;

public final class Longs {

    public static long[] toArray(final Iterable<Long> longs) {
        return stream(longs.spliterator(), false).mapToLong(Long::longValue).toArray();
    }
}
