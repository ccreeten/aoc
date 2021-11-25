package go.solve.it.y2019;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.LineSegment2D;
import go.solve.it.util.math.Positions.Position2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public final class Day3 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2019/day3/input", 2)));
        System.out.println(part2(Input.lines("y2019/day3/input", 2)));
    }

    private static long part1(final String... lines) {
        return calculateLeastDistance(parseWirePath(lines[0]), parseWirePath(lines[1]), 1);
    }

    private static long part2(final String... lines) {
        return calculateLeastDistance(parseWirePath(lines[0]), parseWirePath(lines[1]), 2);
    }

    private static int calculateLeastDistance(final List<LineSegment2D> wire1, final List<LineSegment2D> wire2, final int part) {
        final var port = Position2D.of(0, 0);

        var leastDistance = MAX_VALUE;
        var wire1Length = 0;
        var wire2Length = 0;

        for (final var segment1 : wire1) {
            wire2Length = 0;
            for (final var segment2 : wire2) {
                final var horizontal = segment1.xRange().size() != 0 ? segment1 : segment2;
                final var vertical = horizontal == segment1 ? segment2 : segment1;

                final var hxRange = horizontal.xRange();
                final var hyRange = horizontal.yRange();
                final var vxRange = vertical.xRange();
                final var vyRange = vertical.yRange();

                final var x = hxRange.firstContained(vxRange.from(), vxRange.toInclusive()).orElse(MAX_VALUE);
                final var y = vyRange.firstContained(hyRange.from(), hyRange.toInclusive()).orElse(MAX_VALUE);

                if (x != MAX_VALUE && y != MAX_VALUE && (x | y) != 0) {
                    final var intersection = Position2D.of(x, y);
                    leastDistance = part == 1
                            ? min(leastDistance, intersection.manhattanDistanceFrom(port))
                            : min(leastDistance, wire1Length + wire2Length + intersection.manhattanDistanceFrom(horizontal.p1()) + intersection.manhattanDistanceFrom(vertical.p1()));
                }
                wire2Length += segment2.floorLength();
            }
            wire1Length += segment1.floorLength();
        }
        return leastDistance;
    }

    private static List<LineSegment2D> parseWirePath(final String path) {
        final var segments = new ArrayList<LineSegment2D>();
        var start = Position2D.of(0, 0);
        for (final var move : path.split(",")) {
            segments.add(LineSegment2D.between(start, start = move(start, move)));
        }
        return segments;
    }

    private static Position2D move(final Position2D origin, final String move) {
        return Map.of(
                'U', Position2D.of(0, 1),
                'D', Position2D.of(0, -1),
                'L', Position2D.of(-1, 0),
                'R', Position2D.of(1, 0)
        ).get(move.charAt(0)).multiply(parseInt(move.substring(1))).add(origin);
    }
}