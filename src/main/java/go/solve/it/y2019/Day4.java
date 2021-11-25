package go.solve.it.y2019;

import go.solve.it.util.container.Arraysβ;
import go.solve.it.util.input.Input;
import go.solve.it.util.primitive.Ints;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class Day4 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2019/day4/input", "-")));
        System.out.println(part2(Input.ints("y2019/day4/input", "-")));
    }

    private static long part1(final int[] bounds) {
        return IntStream.range(bounds[0], bounds[1])
                .filter(password -> neverDecreasing(password))
                .filter(password -> groupMatching(password, group -> group.length >= 2))
                .count();
    }

    private static long part2(final int[] bounds) {
        return IntStream.range(bounds[0], bounds[1])
                .filter(password -> neverDecreasing(password))
                .filter(password -> groupMatching(password, group -> group.length == 2))
                .count();
    }

    private static boolean neverDecreasing(final int password) {
        final var digits = Ints.split(password);
        var previous = digits[0];
        for (var i = 1; i < digits.length; i++) {
            final var current = digits[i];
            if (current < previous) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    private static boolean groupMatching(final int password, final Predicate<int[]> predicate) {
        return Arraysβ.group(Ints.split(password))
                .stream()
                .anyMatch(predicate);
    }
}