package go.solve.it;

import go.solve.it.util.Input;
import go.solve.it.util.Tuple;
import go.solve.it.util.Tuple.LongTuple;

public final class Day25 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("day25/input")));
        System.out.println(part2());
    }

    private static long part1(final long... keys) {
        final var key1 = keys[0];
        final var key2 = keys[1];

        final var cracked = crack(key1, key2);
        return transform(cracked.head(), cracked.tail());
    }

    private static LongTuple crack(long key1, long key2) {
        var matchCount = 0L;
        var result = 1L;
        for (var iteration = 1L; iteration < 1L << 32; iteration++) {
            result *= 7;
            result %= 20201227;
            if (result == key1 && ++matchCount == 2) {
                return Tuple.ofLongs(key2, iteration);
            }
            if (result == key2 && ++matchCount == 2) {
                return Tuple.ofLongs(key1, iteration);
            }
        }
        throw new AssertionError();
    }

    private static long transform(final long subjectNumber, final long iterations) {
        var result = 1L;
        for (var iteration = 0; iteration < iterations; iteration++) {
            result *= subjectNumber;
            result %= 20201227;
        }
        return result;
    }

    private static long part2() {
        return 42;
    }
}