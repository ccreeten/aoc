package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.util.function.LongUnaryOperator;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.LongStream.rangeClosed;

public final class Day7 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("y2021/day7/input", ",")));
        System.out.println(part2(Input.longs("y2021/day7/input", ",")));
    }

    private static long part1(final long... positions) {
        return lowestFuelCost(syncPosition -> fuelCostPart1(syncPosition, positions), positions);
    }

    private static long part2(final long... positions) {
        return lowestFuelCost(syncPosition -> fuelCostPart2(syncPosition, positions), positions);
    }

    private static long fuelCostPart1(final long syncPosition, final long... positions) {
        return stream(positions).map(position -> abs(position - syncPosition)).sum();
    }

    private static long fuelCostPart2(final long syncPosition, final long... positions) {
        return stream(positions).map(position -> {
            final var distance = abs(position - syncPosition);
            return distance * (distance + 1) / 2;
        }).sum();
    }

    private static long lowestFuelCost(final LongUnaryOperator fuelCost, final long... positions) {
        final var min = stream(positions).min().getAsLong();
        final var max = stream(positions).max().getAsLong();
        return rangeClosed(min, max).map(fuelCost).min().getAsLong();
    }
}