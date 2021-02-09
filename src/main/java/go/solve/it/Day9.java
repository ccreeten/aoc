package go.solve.it;

import go.solve.it.util.Input;

import java.util.TreeSet;

import static go.solve.it.util.Arrays.slice;
import static go.solve.it.util.Streams.pairWise;

public final class Day9 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("day9/input")));
        System.out.println(part2(Input.longs("day9/input")));
    }

    private static long part1(final long... numbers) {
        for (var i = 25; i < numbers.length; i++) {
            final var index = i;
            final var preamble = slice(numbers, index - 25, index);
            if (pairWise(preamble, Long::sum).noneMatch(sum -> sum == numbers[index])) {
                return numbers[index];
            }
        }
        throw new AssertionError();
    }

    private static long part2(final long... numbers) {
        final var target = part1(numbers);

        final var subset = new TreeSet<Long>();
        var current = numbers[0];
        var from = 0;
        var to = 0;
        var sum = current;

        while (sum != target) {
            if (sum < target) {
                subset.add(current = numbers[++to]);
                sum += current;
            } else {
                subset.remove(current = numbers[from++]);
                sum -= current;
            }
        }
        return subset.first() + subset.last();
    }
}