package go.solve.it;

import go.solve.it.util.Input;

import java.util.stream.Stream;

import static go.solve.it.util.Arrays.slice;
import static go.solve.it.util.Regex.findNext;
import static go.solve.it.util.Streams.split;
import static go.solve.it.util.Strings.reverse;
import static java.lang.Long.parseLong;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Day20 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("day20/input")));
        System.out.println(part2());
    }

    private static long part1(final String... lines) {
        final var tiles = split(lines, String::isBlank)
                .map(tileLines -> tileLines.toArray(String[]::new))
                .map(Tile::parse)
                .collect(toList());

        return tiles.stream().filter(tile -> {
            final var sides = tiles.stream().filter(not(tile::equals)).flatMap(Tile::sides).collect(toSet());
            final var count = tile.sides()
                    .filter(side -> !sides.contains(side) && !sides.contains(reverse(side)))
                    .count();
            return count == 2;
        }).mapToLong(Tile::id).reduce(1, Math::multiplyExact);
    }

    private static long part2() {
        throw new AssertionError();
    }

    static record Tile(long id, String top, String right, String bottom, String left) {

        static Tile parse(final String... tileLines) {
            final var id = parseLong(findNext(tileLines[0], "(\\d+)"));

            var top = tileLines[1];
            var bottom = tileLines[tileLines.length - 1];
            var right = "";
            var left = "";

            for (final var line : slice(tileLines, 1, tileLines.length)) {
                right += line.charAt(0);
                left += line.charAt(line.length() - 1);
            }
            return new Tile(id, top, right, bottom, left);
        }

        Stream<String> sides() {
            return Stream.of(top(), right(), bottom(), left());
        }
    }
}