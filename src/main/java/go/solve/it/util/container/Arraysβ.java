package go.solve.it.util.container;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;
import static java.util.stream.IntStream.range;

public final class Arraysβ {

    public static long count(final char[] values, final char value) {
        return range(0, values.length).filter(index -> values[index] == value).count();
    }

    public static String[] slice(final String[] values, final int fromIndex, final int toIndexExclusive) {
        return copyOfRange(values, fromIndex, toIndexExclusive);
    }

    public static char[] slice(final char[] values, final int fromIndex, final int toIndexExclusive) {
        return copyOfRange(values, fromIndex, toIndexExclusive);
    }

    public static int[] slice(final int[] values, final int fromIndex, final int toIndexExclusive) {
        return copyOfRange(values, fromIndex, toIndexExclusive);
    }

    public static long[] slice(final long[] values, final int fromIndex, final int toIndexExclusive) {
        return copyOfRange(values, fromIndex, toIndexExclusive);
    }

    public static String[] tail(final String... values) {
        return copyOfRange(values, 1, values.length);
    }

    public static int[] tail(final int... values) {
        return copyOfRange(values, 1, values.length);
    }

    public static int indexOf(final int[] values, final int value) {
        for (var i = 0; i < values.length; i++) {
            if (values[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(final int[] values, final int value) {
        return indexOf(values, value) != -1;
    }

    public static int indexOf(final char[] values, final char value) {
        for (var i = 0; i < values.length; i++) {
            if (values[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final int[] values, final int value, final int fromIndex) {
        for (var i = fromIndex; i >= 0; i--) {
            if (values[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static long sum(final long... values) {
        return LongStream.of(values).sum();
    }

    public static char[] concat(final char[] first, final char[] second) {
        final char[] result = copyOf(first, first.length + second.length);
        arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static List<int[]> group(final int... values) {
        final var result = new ArrayList<int[]>();
        var current = values[0];
        var from = 0;
        for (var to = 1; to < values.length; to++) {
            if (values[to] != current) {
                result.add(copyOfRange(values, from, to));
                current = values[to];
                from = to;
            }
        }
        if (from < values.length - 1) {
            result.add(copyOfRange(values, from, values.length));
        }
        return result;
    }
}
