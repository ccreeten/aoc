package go.solve.it.y2019;

import go.solve.it.util.input.Input;

import java.util.stream.LongStream;

public final class Day1 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("y2019/day1/input")));
        System.out.println(part2(Input.longs("y2019/day1/input")));
    }

    private static long part1(final long... masses) {
        return LongStream.of(masses)
                .map(mass -> mass / 3 - 2)
                .sum();
    }

    private static long part2(final long... masses) {
        return LongStream.of(masses)
                .map(Day1::fuelRequirementFor)
                .sum();
    }

    private static long fuelRequirementFor(final long mass) {
        final var fuel = mass / 3 - 2;
        if (fuel <= 0) {
            return 0;
        }
        return fuel + fuelRequirementFor(fuel);
    }
}