package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import java.util.TreeSet;

import static go.solve.it.util.container.Arraysβ.pairWise;
import static go.solve.it.util.container.Arraysβ.slice;

public final class Day9 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("y2020/day9/input")));
        System.out.println(part2(Input.longs("y2020/day9/input")));
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