package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.util.*;

import static go.solve.it.util.math.Positions.Position2D;
import static go.solve.it.util.math.Positions.generateFor;
import static java.util.stream.Collectors.toSet;

public final class Day20 {

    private static final List<Position2D> DELTAS = List.of(
            Position2D.of(-1, -1),
            Position2D.of(+0, -1),
            Position2D.of(+1, -1),

            Position2D.of(-1, +0),
            Position2D.of(+0, +0),
            Position2D.of(+1, +0),

            Position2D.of(-1, +1),
            Position2D.of(+0, +1),
            Position2D.of(+1, +1)
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.scan("y2021/day20/input")));
        System.out.println(part2(Input.scan("y2021/day20/input")));
    }

    private static long part1(final Scanner scan) {
        return enhance(scan, 2);
    }

    private static long part2(final Scanner scan) {
        return enhance(scan, 50);
    }

    private static int enhance(final Scanner scan, final int iterations) {
        final var algorithm = scan.nextLine();
        var leastStates = litPixels(scan);
        for (var i = 0; i < iterations; i++) {
            leastStates = enhance((i & 1) == 0, leastStates, algorithm);
        }
        // even iterations, so we know it are the lit pixels
        return leastStates.size();
    }

    private static Set<Position2D> litPixels(final Scanner scan) {
        scan.nextLine();
        final var lines = new ArrayList<String>();
        while (scan.hasNextLine()) {
            lines.add(scan.nextLine());
        }
        final var grid = new char[lines.size()][];
        for (var i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return generateFor(grid).filter(pos -> pos.get(grid) == '#').collect(toSet());
    }

    private static Set<Position2D> enhance(final boolean litPixels, final Set<Position2D> pixels, final String algorithm) {
        return pixels.stream()
                .flatMap(pixel -> DELTAS.stream().map(pixel::add))
                .map(pixel -> {
                    var index = 0;
                    for (final var delta : DELTAS) {
                        index <<= 1;
                        if (litPixels == pixels.contains(pixel.add(delta))) {
                            index |= 1;
                        }
                    }
                    return algorithm.charAt(index) == '#' != litPixels ? pixel : null;
                })
                .filter(Objects::nonNull)
                .collect(toSet());
    }
}