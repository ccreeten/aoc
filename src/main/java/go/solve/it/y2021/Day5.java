package go.solve.it.y2021;

import go.solve.it.util.container.Tuple.IntTuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.LineSegment2D;
import go.solve.it.util.math.Positions.Position2D;
import go.solve.it.util.stream.Streams;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static go.solve.it.util.stream.Streams.zip;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public final class Day5 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day5/input")));
        System.out.println(part2(Input.lines("y2021/day5/input")));
    }

    private static long part1(final String... lines) {
        return countOverlapsWithout(parseInputs(lines), segment -> segment.xRange().size() == 0 || segment.yRange().size() == 0);
    }

    private static long part2(final String... lines) {
        return countOverlapsWithout(parseInputs(lines), __ -> true);
    }

    private static long countOverlapsWithout(final Stream<LineSegment2D> segments, final Predicate<LineSegment2D> segmentFilter) {
        return segments
                .filter(segmentFilter)
                .flatMap(segment -> segment.xRange().size() == segment.yRange().size()
                        ? zip(segment.xRange().values(), segment.yRange().values(), Position2D::of)
                        : segment.xRange().values().flatMap(x -> segment.yRange().values().map(y -> Position2D.of(x, y)))
                )
                .collect(groupingBy(identity(), counting()))
                .values()
                .stream()
                .filter(count -> count >= 2)
                .count();
    }

    private static Stream<LineSegment2D> parseInputs(final String... lines) {
        return stream(lines).map(line -> {
            final var points = line.split(" -> ");
            final var p1 = IntTuple.ofPartition(points[0], ",");
            final var p2 = IntTuple.ofPartition(points[1], ",");
            return LineSegment2D.between(
                    Position2D.of(parseInt(p1.head()), parseInt(p1.tail())),
                    Position2D.of(parseInt(p2.head()), parseInt(p2.tail()))
            );
        });
    }
}