package go.solve.it;

import go.solve.it.util.Input;

public final class Day1 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("day1/input")));
        System.out.println(part2(Input.longs("day1/input")));
    }

    private static long part1(final long... entries) {
        for (var i = 0; i < entries.length; i++) {
            for (var j = i + 1; j < entries.length; j++) {
                if (entries[i] + entries[j] == 2020) {
                    return entries[i] * entries[j];
                }
            }
        }
        throw new AssertionError();
    }

    private static long part2(final long... entries) {
        for (var i = 0; i < entries.length; i++) {
            for (var j = i + 1; j < entries.length; j++) {
                for (var k = j + 1; k < entries.length; k++) {
                    if (entries[i] + entries[j] + entries[k] == 2020) {
                        return entries[i] * entries[j] * entries[k];
                    }
                }
            }
        }
        throw new AssertionError();
    }
}
