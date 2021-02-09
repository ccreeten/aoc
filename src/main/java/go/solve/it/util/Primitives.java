package go.solve.it.util;

import static java.util.stream.StreamSupport.stream;

public final class Primitives {

    public static long[] toArray(final Iterable<Long> longs) {
        return stream(longs.spliterator(), false).mapToLong(Long::longValue).toArray();
    }

    public static int[] ints(final int... ints) {
        return ints;
    }
}
