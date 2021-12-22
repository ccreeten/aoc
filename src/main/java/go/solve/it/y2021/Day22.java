package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions;
import go.solve.it.util.math.Positions.Position3D;
import go.solve.it.util.math.Range.IntRange;
import go.solve.it.util.string.Regex;

import java.util.HashSet;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;

public final class Day22 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day22/input")));
        System.out.println(part2(Input.lines("y2021/day22/input")));
    }

    private static long part1(final String... lines) {
        final var cubes = new HashSet<Position3D>();
        stream(lines).forEach(line -> {
                    final var numbers = Regex.findAll(line, "(-?\\d+)").stream().mapToInt(Integer::parseInt).toArray();
                    final var xRange = IntRange.between(max(numbers[0], -50), min(numbers[1], 50));
                    final var yRange = IntRange.between(max(numbers[2], -50), min(numbers[3], 50));
                    final var zRange = IntRange.between(max(numbers[4], -50), min(numbers[5], 50));
                    final var on = line.startsWith("on");
                    Positions.generate3D(xRange, yRange, zRange).forEach(cube -> {
                        if (on) {
                            cubes.add(cube);
                        } else {
                            cubes.remove(cube);
                        }
                    });
                }
        );
        return cubes.size();
    }


    private static long part2(final String... lines) {
        return 0;
    }
}
