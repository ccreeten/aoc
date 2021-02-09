package go.solve.it.util;

import java.util.stream.LongStream;

import static java.util.Arrays.copyOfRange;
import static java.util.stream.IntStream.range;

public final class Arrays {

    public static long count(final char[] array, final char value) {
        return range(0, array.length).filter(index -> array[index] == value).count();
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

    public static int indexOf(final int[] array, final int value) {
        for (var i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final char[] array, final char value) {
        for (var i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final int[] numbers, final int number, final int fromIndex) {
        for (var i = fromIndex; i >= 0; i--) {
            if (numbers[i] == number) {
                return i;
            }
        }
        return -1;
    }

    public static long sum(final long... memory) {
        return LongStream.of(memory).sum();
    }
}
