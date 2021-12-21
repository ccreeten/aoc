package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions;
import go.solve.it.util.math.Positions.Position2D;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;

public final class Day11 {

    private static final List<Position2D> DELTAS = List.of(
            Position2D.of(-1, +0),
            Position2D.of(+1, +0),
            Position2D.of(+0, -1),
            Position2D.of(+0, +1),

            Position2D.of(-1, -1),
            Position2D.of(+1, +1),
            Position2D.of(-1, +1),
            Position2D.of(+1, -1)
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.intGrid("y2021/day11/input")));
        System.out.println(part2(Input.intGrid("y2021/day11/input")));
    }

    private static long part1(final int[][] grid) {
        final var steps = 9999999;
        final var flashes = new AtomicLong();
        for (var step = 0; step < steps; step++) {
            final var flashed = new AtomicBoolean(true);
            final var seen = new HashSet<Position2D>();
            Positions.generateFor(grid).forEach(position -> {
                final int value = position.get(grid) + 1;
                position.set(grid, value);
            });
            while (flashed.get()) {
                flashed.set(false);
                Positions.generateFor(grid).filter(not(seen::contains)).forEach(position -> {
//                    if (DELTAS.stream().map(position::add).filter(neighbour -> neighbour.exists(grid)).anyMatch(neighbour -> neighbour.get(grid) > 9)) {
                        final int value = position.get(grid);
//                        position.set(grid, value);
                        if (value > 9) {
                            seen.add(position);
                            flashed.set(true);
                            flashes.getAndIncrement();
                            DELTAS.stream().map(position::add).filter(neighbour -> neighbour.exists(grid)).forEach(neighbour -> {
                                neighbour.set(grid, neighbour.get(grid) + 1);
                            });
                        }
//                    }
                });
            }
            Positions.generateFor(grid).forEach(position -> {
                final int value = position.get(grid);
                if (value > 9)
                    position.set(grid, 0);
            });
            final long count = Positions.generateFor(grid).filter(p -> p.get(grid) == Position2D.atOrigin().get(grid))
                    .count();
            final int i = grid.length * grid[0].length;
            if (count == i) {
                return step;
            }
        }
        return flashes.get();
    }

    private static long part2(final int[][] grid) {
        return Positions.generateFor(grid)
                .map(position -> basinSize(position, grid))
                .sorted(reverseOrder())
                .limit(3)
                .reduce(1, (left, right) -> left * right);
    }

    private static int basinSize(final Position2D position, final int[][] grid) {
        if (!position.exists(grid) || position.get(grid) == 9) {
            return 0;
        }
        position.set(grid, 9);
        return 1 + DELTAS.stream()
                .map(position::add)
                .mapToInt(neighbour -> basinSize(neighbour, grid))
                .sum();
    }
}