package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static go.solve.it.util.container.Collectionsβ.permutations;
import static go.solve.it.util.stream.BiStream.zip;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class Day8 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day8/input")));
        System.out.println(part2(Input.lines("y2021/day8/input")));
    }

    private static long part1(final String... lines) {
        final var uniqueDigitLengths = Set.of(2, 3, 4, 7);
        return stream(lines)
                .map(line -> Tuple.ofPartition(line, " [|] ").tail())
                .flatMap(digits -> stream(digits.split(" ")))
                .filter(digits -> uniqueDigitLengths.contains(digits.length()))
                .count();
    }

    private static long part2(final String... lines) {
        return stream(lines)
                .map(line -> Tuple.ofPartition(line, " [|] "))
                .map(digits -> digits.mapHead(head -> head.split(" ")).mapTail(tail -> tail.split(" ")))
                .mapToLong(digits -> deduceValue(digits.head(), digits.tail()))
                .sum();
    }

    private static long deduceValue(final String[] signals, final String[] output) {
        final var segments = List.of("a", "b", "c", "d", "e", "f", "g");
        final var digits = Map.of(
                "abcefg", 0,
                "cf", 1,
                "acdeg", 2,
                "acdfg", 3,
                "bcdf", 4,
                "abdfg", 5,
                "abdefg", 6,
                "acf", 7,
                "abcdefg", 8,
                "abcdfg", 9
        );
        return permutations(segments).stream()
                .map(permutation -> zip(permutation.stream(), segments.stream()).toMap())
                .filter(mapping -> stream(signals)
                        .map(signal -> rewire(signal, mapping))
                        .allMatch(digits::containsKey)
                )
                .mapToLong(mapping -> stream(output)
                        .map(digit -> rewire(digit, mapping))
                        .mapToInt(digits::get)
                        .reduce(0, (result, digit) -> result * 10 + digit)
                )
                .findFirst()
                .getAsLong();
    }

    private static String rewire(final String signal, final Map<String, String> mapping) {
        return stream(signal.split("")).map(mapping::get).sorted().collect(joining());
    }
}