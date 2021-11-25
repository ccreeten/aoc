package go.solve.it.y2020;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static go.solve.it.util.stream.BiStream.biStream;
import static go.solve.it.util.string.Regex.findAll;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

public final class Day24 {

    private static final Position3D CENTER = Position3D.of(+0, +0, +0);
    private static final Map<String, Position3D> DELTAS = Map.of(
            "ne", Position3D.of(+0, +1, -1),
            "e",  Position3D.of(+1, +0, -1),
            "se", Position3D.of(+1, -1, +0),
            "sw", Position3D.of(+0, -1, +1),
            "w",  Position3D.of(-1, +0, +1),
            "nw", Position3D.of(-1, +1, +0),
            "it", Position3D.of(+0, +0, +0)
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day24/input")));
        System.out.println(part2(Input.lines("y2020/day24/input")));
    }

    private static long part1(final String... stepList) {
        return findBlackTiles(stepList).size();
    }

    private static long part2(final String... stepList) {
        return executeFlips(findBlackTiles(stepList), 100).size();
    }

    private static Set<Position3D> findBlackTiles(final String... stepList) {
        final var tileCounts = stream(stepList)
                .map(steps -> findAll(steps, "(ne|se|sw|nw|e|w)").stream().map(DELTAS::get).reduce(CENTER, Position3D::add))
                .collect(groupingBy(identity(), counting()));
        return biStream(tileCounts).filter((tile, count) -> count % 2 == 1).lefts().collect(toSet());
    }

    private static Set<Position3D> executeFlips(final Set<Position3D> blackTiles, final int iterations) {
        final var tiles = new HashSet<>(blackTiles);
        for (var iteration = 0; iteration < iterations; iteration++) {
            final var positionsToCheck = tiles.stream()
                    .flatMap(position -> DELTAS.values().stream().map(position::add))
                    .collect(toSet());

            final var tilesToFlip = new ArrayList<Position3D>();
            positionsToCheck.forEach(position -> {
                final var blackNeighbours = DELTAS.values().stream()
                        .map(position::add)
                        .filter(not(position::equals).and(tiles::contains))
                        .count();

                if (tiles.contains(position)) {
                    if (blackNeighbours == 0 || blackNeighbours > 2) {
                        tilesToFlip.add(position);
                    }
                } else if (blackNeighbours == 2) {
                    tilesToFlip.add(position);
                }
            });
            tilesToFlip.forEach(tile -> {
                if (!tiles.remove(tile)) {
                    tiles.add(tile);
                }
            });
        }
        return tiles;
    }
}