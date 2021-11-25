package go.solve.it.y2020;

import go.solve.it.util.container.Collectionsβ;
import go.solve.it.util.input.Input;

import java.util.Set;

import static go.solve.it.util.container.Collectionsβ.setOf;
import static go.solve.it.util.stream.Streams.split;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.joining;

public final class Day6 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day6/input")));
        System.out.println(part2(Input.lines("y2020/day6/input")));
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
                .map(responses -> responses.reduce(Collectionsβ::intersection).orElse(emptySet()))
                .mapToLong(Set::size)
                .sum();
    }
}