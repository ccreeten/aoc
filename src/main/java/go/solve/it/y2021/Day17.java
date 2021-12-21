package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position2D;
import go.solve.it.util.math.Range.IntRange;
import go.solve.it.util.string.Regex;

import static java.lang.Integer.max;

public final class Day17 {

    public static void main(final String... args) {
        System.out.println(part1(Input.line("y2021/day17/input")));
        System.out.println(part2(Input.line("y2021/day17/input")));
    }

    private static long part1(final String line) {
        final var numbers = Regex.findAll(line, "(-?\\d+)").stream().mapToInt(Integer::parseInt).toArray();
        final var xRange = IntRange.between(numbers[0], numbers[1]);
        final var yRange = IntRange.between(numbers[2], numbers[3]);

        var maxY = 0;
        for (var x = 1; x <= xRange.toInclusive() + 1; x++) {
            for (var y = 1; y <= xRange.toInclusive() + 1; y++) {
                maxY = max(maxY, maxY(xRange, yRange, Position2D.atOrigin(), x, y, y, false));
            }
        }
        return maxY;
    }

    private static long part2(final String line) {
        final var numbers = Regex.findAll(line, "(-?\\d+)").stream().mapToInt(Integer::parseInt).toArray();
        final var xRange = IntRange.between(numbers[0], numbers[1]);
        final var yRange = IntRange.between(numbers[2], numbers[3]);

        var count = 0;
        for (var x = 1; x <= xRange.toInclusive() + 1; x++) {
            for (var y = yRange.from(); y <= xRange.toInclusive() + 1; y++) {
                if (maxY(xRange, yRange, Position2D.atOrigin(), x, y, y, false) != Integer.MIN_VALUE) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int maxY(final IntRange xRange, final IntRange yRange, final Position2D start, final int x, final int y, final int maxY, final boolean found) {
        if (start.x() > xRange.toInclusive()) {
            return found ? maxY : Integer.MIN_VALUE;
        }
        if (start.y() < yRange.from()) {
            return found ? maxY : Integer.MIN_VALUE;
        }
        final var nextPos = start.translateX(x).translateY(y);
        return maxY(xRange, yRange, nextPos, x + (x > 0 ? -1 : x != 0 ? 1 : 0), y - 1, max(start.y(), maxY), found || xRange.contains(nextPos.x()) && yRange.contains(nextPos.y()));
    }
}