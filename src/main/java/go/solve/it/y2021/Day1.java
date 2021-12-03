package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import static go.solve.it.util.container.Arraysβ.sum;

public final class Day1 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2021/day1/input")));
        System.out.println(part2(Input.ints("y2021/day1/input")));
    }

    private static long part1(final int... depths) {
        var increments = 0;
        for (var i = 1; i < depths.length; i++) {
            if (depths[i] > depths[i - 1]) {
                increments++;
            }
        }
        return increments;
    }

    private static long part2(final int... depths) {
        var increments = 0;
        for (var i = 3; i < depths.length; i++) {
            if (sum(depths[i], depths[i - 1], depths[i - 2]) > sum(depths[i - 1], depths[i - 2], depths[i - 3])) {
                increments++;
            }
        }
        return increments;
    }
}