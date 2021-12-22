package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Range.LongRange;

import java.util.HashSet;
import java.util.Set;

import static go.solve.it.util.string.Regex.findAll;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;

public final class Day22 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day22/input")));
        System.out.println(part2(Input.lines("y2021/day22/input")));
    }

    private static long part1(final String... lines) {
        return extracted(-50, 50, lines);
    }

    private static long part2(final String... lines) {
        return extracted(Long.MIN_VALUE, Long.MAX_VALUE, lines);
    }

    private static long extracted(final long min, final long max, final String[] lines) {
        return stream(lines).map(line -> {
                    final var numbers = findAll(line, "(-?\\d+)").stream().mapToLong(Long::parseLong).toArray();
                    final var xRange = LongRange.between(max(numbers[0], min), min(numbers[1], max));
                    final var yRange = LongRange.between(max(numbers[2], min), min(numbers[3], max));
                    final var zRange = LongRange.between(max(numbers[4], min), min(numbers[5], max));
                    final var spanning = Cube.spanning(xRange, yRange, zRange);
                    return Tuple.of(spanning, line.startsWith("on"));
                })
                .filter(cube -> cube.head().isValid())
                .reduce((Set<Cube>) new HashSet<Cube>(), (cubes, cube) -> merge(cubes, cube.head(), cube.tail()), (l, r) -> l)
                .stream()
                .mapToLong(Cube::size)
                .sum();
    }

    public static Set<Cube> merge(final Set<Cube> cubes, final Cube cube, final boolean on) {
        final var result = new HashSet<Cube>();
        for (final var c : cubes) {
            if (c.overlaps(cube)) {
                result.addAll(split(c, cube));
            } else {
                result.add(c);
            }
        }
        if (on) {
            result.add(cube);
        }
        return result;
    }

    private static Set<Cube> split(final Cube l, final Cube r) {
        final var result = new HashSet<Cube>();
        final var xOverlap = LongRange.between(max(l.xRange().from(), r.xRange().from()), min(l.xRange().toInclusive(), r.xRange().toInclusive()));
        final var zOverlap = LongRange.between(max(l.zRange().from(), r.zRange().from()), min(l.zRange().toInclusive(), r.zRange().toInclusive()));
        // 'left' slice
        result.add(Cube.spanning(LongRange.between(l.xRange().from(), r.xRange().from() - 1), l.yRange(), l.zRange()));
        // 'right' slice
        result.add(Cube.spanning(LongRange.between(r.xRange().toInclusive() + 1, l.xRange().toInclusive()), l.yRange(), l.zRange()));
        // 'front' slice
        result.add(Cube.spanning(xOverlap, l.yRange(), LongRange.between(r.zRange().toInclusive() + 1, l.zRange().toInclusive())));
        // 'back' slice
        result.add(Cube.spanning(xOverlap, l.yRange(), LongRange.between(l.zRange().from(), r.zRange().from() - 1)));
        // 'bottom' slice
        result.add(Cube.spanning(xOverlap, LongRange.between(l.yRange().from(), r.yRange().from() - 1), zOverlap));
        // 'top' slice
        result.add(Cube.spanning(xOverlap, LongRange.between(r.yRange().toInclusive() + 1, l.yRange().toInclusive()), zOverlap));
        // remove all for which max > min
        result.removeIf(cube -> !cube.isValid());
        return result;
    }

    static final record Cube(LongRange xRange, LongRange yRange, LongRange zRange) {

        static Cube spanning(LongRange xRange, LongRange yRange, LongRange zRange) {
            return new Cube(xRange, yRange, zRange);
        }

        private boolean overlaps(final Cube other) {
            return xRange().overlaps(other.xRange()) && yRange().overlaps(other.yRange()) && zRange().overlaps(other.zRange());
        }

        public long size() {
            return xRange().size() * yRange().size() * zRange().size();
        }

        public boolean isValid() {
            return !(xRange().isDecreasing() || yRange().isDecreasing() || zRange().isDecreasing());
        }
    }
}
