package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions;
import go.solve.it.util.math.Positions.Position2D;

import java.util.List;

import static java.util.Comparator.reverseOrder;

public final class Day9 {

    private static final List<Position2D> DELTAS = List.of(
            Position2D.of(-1, +0),
            Position2D.of(+1, +0),
            Position2D.of(+0, -1),
            Position2D.of(+0, +1)
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.intGrid("y2021/day9/input")));
        System.out.println(part2(Input.intGrid("y2021/day9/input")));
    }

    private static long part1(final int[][] grid) {
        return Positions.generateFor(grid)
                .filter(position -> DELTAS.stream()
                        .map(position::add)
                        .filter(neighbour -> neighbour.exists(grid))
                        .allMatch(neighbour -> position.get(grid) < neighbour.get(grid))
                )
                .mapToInt(position -> position.get(grid) + 1)
                .sum();
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