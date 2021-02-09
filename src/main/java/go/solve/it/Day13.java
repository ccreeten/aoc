package go.solve.it;

import go.solve.it.util.Input;
import go.solve.it.util.Tuple;
import go.solve.it.util.Tuple.LongTuple;

import java.util.Scanner;

import static go.solve.it.util.BiStream.streamIndexed;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;

public final class Day13 {

    public static void main(final String... args) {
        try (Scanner scanner = Input.scan("day13/input")) {
            System.out.println(part1(scanner));
        }
        try (Scanner scanner = Input.scan("day13/input")) {
            System.out.println(part2(scanner));
        }
    }

    private static long part1(final Scanner scanner) {
        final var earliest = parseLong(scanner.nextLine());
        return stream(scanner.nextLine().split(","))
                .filter(not("x"::equals))
                .map(Long::parseLong)
                .min(comparing(id -> id - (earliest % id)))
                .map(id -> id * (id - (earliest % id)))
                .orElseThrow(AssertionError::new);
    }

    private static long part2(final Scanner scanner) {
        scanner.nextLine();

        // tuple of (bus index, bus id)
        final var buses = streamIndexed(scanner.nextLine().split(","))
                .filter((index, id) -> !id.equals("x"))
                .map((index, id) -> Tuple.ofLongs(index, parseLong(id)))
                .sorted(comparing(Tuple::tail))
                .toArray(LongTuple[]::new);

        var t = -buses[0].head();
        var timeStep = buses[0].tail();
        var currentBus = 1;

        for (; t < Long.MAX_VALUE; t += timeStep) {
            final var delta = buses[currentBus].head();
            final var id = buses[currentBus].tail();
            if ((t + delta) % id == 0) {
                timeStep *= id;
                currentBus++;
                if (currentBus == buses.length) {
                    return t;
                }
            }
        }
        throw new AssertionError();
    }
}