package go.solve.it;

import go.solve.it.util.Addable;
import go.solve.it.util.Input;
import go.solve.it.util.Positions;
import go.solve.it.util.Positions.Position3D;
import go.solve.it.util.Positions.Position4D;
import go.solve.it.util.Range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

public final class Day17 {

    public static void main(final String... args) {
        System.out.println(part1(Input.grid("day17/input")));
        System.out.println(part2(Input.grid("day17/input")));
    }

    private static long part1(final char[][] grid) {
        final var deltas = Positions.generate3D(
                Range.between(-1, 1),
                Range.between(-1, 1),
                Range.between(-1, 1)
        ).collect(toSet());

        final var activeCubes = new HashSet<Position3D>();
        for (var x = 0; x < grid.length; x++) {
            for (var y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == '#') {
                    activeCubes.add(Position3D.of(x, y, 0));
                }
            }
        }

        return solve(deltas, activeCubes);
    }

    private static long part2(final char[][] grid) {
        final var deltas = Positions.generate4D(
                Range.between(-1, 1),
                Range.between(-1, 1),
                Range.between(-1, 1),
                Range.between(-1, 1)
        ).collect(toSet());

        final var activeCubes = new HashSet<Position4D>();
        for (var x = 0; x < grid.length; x++) {
            for (var y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == '#') {
                    activeCubes.add(Position4D.of(x, y, 0, 0));
                }
            }
        }

        return solve(deltas, activeCubes);
    }

    private static <P extends Addable<P>> long solve(final Set<P> deltas, final Set<P> activeCubes) {
        for (var i = 0; i < 6; i++) {
            final var positionsToCheck = activeCubes.stream()
                    .flatMap(position -> deltas.stream().map(position::add))
                    .collect(toSet());

            final var cubesToActivate = new ArrayList<P>();
            final var cubesToDeactivate = new ArrayList<P>();
            positionsToCheck.forEach(position -> {
                final var activeNeighbours = deltas.stream()
                        .map(position::add)
                        .filter(not(position::equals).and(activeCubes::contains))
                        .count();

                if (activeCubes.contains(position)) {
                    if (activeNeighbours != 2 && activeNeighbours != 3) {
                        cubesToDeactivate.add(position);
                    }
                } else if (activeNeighbours == 3) {
                    cubesToActivate.add(position);
                }
            });
            cubesToDeactivate.forEach(activeCubes::remove);
            activeCubes.addAll(cubesToActivate);
        }
        return activeCubes.size();
    }
}