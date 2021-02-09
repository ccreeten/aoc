package go.solve.it;

import go.solve.it.util.Collections;
import go.solve.it.util.Input;

import java.util.Set;

import static go.solve.it.util.Collections.setOf;
import static go.solve.it.util.Streams.split;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.joining;

public final class Day6 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("day6/input")));
        System.out.println(part2(Input.lines("day6/input")));
    }

    private static long part1(final String... entries) {
        return split(entries, String::isBlank)
                .map(responses -> setOf(responses.collect(joining("")).split("")))
                .mapToLong(Set::size)
                .sum();
    }

    private static long part2(final String... entries) {
        return split(entries, String::isBlank)
                .map(responses -> responses.map(response -> setOf(response.split(""))))
                .map(responses -> responses.reduce(Collections::intersection).orElse(emptySet()))
                .mapToLong(Set::size)
                .sum();
    }
}