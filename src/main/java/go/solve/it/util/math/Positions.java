package go.solve.it.util.math;

import go.solve.it.util.math.Range.IntRange;

import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.util.function.Function.identity;
import static java.util.stream.IntStream.rangeClosed;

public final class Positions {

    public static Stream<Position2D> generateFor(final int[][] grid) {
        return generate2D(IntRange.between(0, grid[0].length - 1), IntRange.between(0, grid.length - 1));
    }

    public static Stream<Position2D> generate2D(final IntRange xRange, final IntRange yRange) {
        return rangeClosed(xRange.from(), xRange.toInclusive())
                .mapToObj(x -> rangeClosed(yRange.from(), yRange.toInclusive())
                .mapToObj(y -> Position2D.of(x, y)))
                .flatMap(identity());
    }

    public static Stream<Position3D> generate3D(final IntRange xRange, final IntRange yRange, final IntRange zRange) {
        return rangeClosed(xRange.from(), xRange.toInclusive())
                .mapToObj(x -> rangeClosed(yRange.from(), yRange.toInclusive())
                .mapToObj(y -> rangeClosed(zRange.from(), zRange.toInclusive())
                .mapToObj(z -> Position3D.of(x, y, z))))
                .flatMap(identity())
                .flatMap(identity());
    }

    public static Stream<Position4D> generate4D(final IntRange xRange, final IntRange yRange, final IntRange zRange, final IntRange wRange) {
        return rangeClosed(xRange.from(), xRange.toInclusive())
                .mapToObj(x -> rangeClosed(yRange.from(), yRange.toInclusive())
                .mapToObj(y -> rangeClosed(zRange.from(), zRange.toInclusive())
                .mapToObj(z -> rangeClosed(wRange.from(), wRange.toInclusive())
                .mapToObj(w -> Position4D.of(x, y, z, w)))))
                .flatMap(identity())
                .flatMap(identity())
                .flatMap(identity());
    }

    public record Position2D(int x, int y) implements Addable<Position2D> {

        public static Position2D of(final int x, final int y) {
            return new Position2D(x, y);
        }

        public static Position2D atOrigin() {
            return Position2D.of(0, 0);
        }

        public boolean exists(final int[][] grid) {
            return y() >= 0 && x() >= 0 && y() < grid.length && x() < grid[y()].length;
        }

        public void set(final int[][] grid, final int value) {
            grid[y()][x()] = value;
        }

        public int get(final int[][] grid) {
            return grid[y()][x()];
        }

        @Override
        public Position2D add(final Position2D other) {
            return Position2D.of(x() + other.x(), y() + other.y());
        }

        public Position2D swap() {
            return Position2D.of(y(), x());
        }

        public Position2D multiply(final int value) {
            return Position2D.of(x() * value, y() * value);
        }

        public Position2D multiplyX(final int value) {
            return Position2D.of(x() * value, y());
        }

        public Position2D multiplyY(final int value) {
            return Position2D.of(x(), y() * value);
        }

        public Position2D translateX(final int delta) {
            return Position2D.of(x() + delta, y());
        }

        public Position2D translateY(final int delta) {
            return Position2D.of(x(), y() + delta);
        }

        public int manhattanDistanceFrom(final Position2D other) {
            return abs(x() - other.x()) + abs(y() - other.y());
        }
    }

    public record Position3D(int x, int y, int z) implements Addable<Position3D> {

        public static Position3D of(final int x, final int y, final int z) {
            return new Position3D(x, y, z);
        }

        public static Position3D atOrigin() {
            return Position3D.of(0, 0, 0);
        }

        @Override
        public Position3D add(final Position3D other) {
            return Position3D.of(x() + other.x(), y() + other.y(), z() + other.z());
        }

        public Position3D multiply(final int value) {
            return Position3D.of(x() * value, y() * value, z() * value);
        }
    }

    public record Position4D(int x, int y, int z, int w) implements Addable<Position4D> {

        public static Position4D of(final int x, final int y, final int z, final int w) {
            return new Position4D(x, y, z, w);
        }

        @Override
        public Position4D add(final Position4D other) {
            return Position4D.of(x() + other.x(), y() + other.y(), z() + other.z(), w() + other.w());
        }
    }
}
