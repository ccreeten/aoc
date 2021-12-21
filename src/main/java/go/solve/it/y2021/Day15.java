package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position2D;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.System.arraycopy;
import static java.util.Comparator.comparingLong;

public final class Day15 {

    private static final List<Position2D> DELTAS = List.of(
            Position2D.of(-1, +0),
            Position2D.of(+0, -1),
            Position2D.of(+0, +1),
            Position2D.of(+1, +0)
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.intGrid("y2021/day15/input")));
        System.out.println(part2(Input.intGrid("y2021/day15/input")));
    }

    private static long part1(final int[][] grid) {
        return lowestRisk(grid, Position2D.atOrigin(), Position2D.of(grid[0].length - 1, grid.length - 1));
    }

    private static long part2(final int[][] grid) {
        final var expanded = expand(grid);
        return lowestRisk(expanded, Position2D.atOrigin(), Position2D.of(expanded[0].length - 1, expanded.length - 1));
    }

    private static long lowestRisk(final int[][] grid, final Position2D start, final Position2D end) {
        final var next = new PriorityQueue<Tuple<Position2D, Long>>(comparingLong(Tuple::tail));
        next.add(Tuple.of(start, 0L));

        final var risk = new HashMap<Position2D, Long>();
        risk.put(start, 0L);

        while (!next.isEmpty()) {
            final var current = next.poll();
            if (current.head().equals(end)) {
                return risk.get(current.head());
            }
            for (final var delta : DELTAS) {
                final var neighbour = delta.add(current.head());
                if (neighbour.exists(grid)) {
                    final var newRisk = risk.getOrDefault(current.head(), 1000000000L) + neighbour.get(grid);
                    if (newRisk < risk.getOrDefault(neighbour, 1000000000L)) {
                        risk.put(neighbour, newRisk);
                        next.add(Tuple.of(neighbour, newRisk));
                    }
                }
            }
        }
        throw new AssertionError();
    }

    private static int[][] expand(final int[][] lines) {
        final var xl = lines[0].length;
        final var yl = lines.length;

        final var grid = new int[yl * 5][xl * 5];
        for (var x = 0; x < xl; x++) {
            arraycopy(lines[x], 0, grid[x], 0, yl);
        }
        for (var y = 0; y < 5; y++) {
            for (var x = 0; x < 5; x++) {
                if (x != 0 || y != 0) {
                    final var bx = x * xl;
                    final var by = y * yl;
                    for (var nx = bx; nx < bx + xl; nx++) {
                        for (var ny = by; ny < by + yl; ny++) {
                            final var i = (y == 0) ? grid[ny][nx - xl] : grid[ny - yl][nx];
                            grid[ny][nx] = i > 8 ? 1 : i + 1;
                        }
                    }
                }
            }
        }
        return grid;
    }
}
