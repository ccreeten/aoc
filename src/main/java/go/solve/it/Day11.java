package go.solve.it;

import go.solve.it.util.Input;
import go.solve.it.util.Positions;
import go.solve.it.util.Positions.Position2D;
import go.solve.it.util.Range;

import java.util.HashMap;
import java.util.List;

import static go.solve.it.util.Arrays.count;
import static go.solve.it.util.BiStream.streamPositions;
import static go.solve.it.util.StreamOps.single;
import static go.solve.it.util.Streams.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.iterate;

public final class Day11 {

    private static final List<Position2D> DELTAS = Positions.generate2D(Range.between(-1, 1), Range.between(-1, 1))
            .filter(position -> !(position.x() == 0 && position.y() == 0))
            .collect(toList());

    public static void main(final String... args) {
        System.out.println(part1(Input.grid("day11/input")));
        System.out.println(part2(Input.grid("day11/input")));
    }

    private static long part1(final char[][] layout) {
        final var updates = new HashMap<Position2D, Character>();
        streamPositions(layout).forEachOrdered((row, col) -> {
            final var occupiedNeighbourCount = DELTAS.stream()
                    .filter(delta -> inBounds(layout, row, col, delta))
                    .filter(delta -> layout[row + delta.x()][col + delta.y()] == '#')
                    .count();

            final var oldState = layout[row][col];
            final var newState = newStateForLocal(oldState, occupiedNeighbourCount);
            if (oldState != newState) {
                updates.put(Position2D.of(row, col), newState);
            }
        });
        if (updates.isEmpty()) {
            return stream(layout).mapToLong(row -> count(row, '#')).sum();
        }
        updates.forEach((position, state) -> layout[position.x()][position.y()] = state);
        return part1(layout);
    }

    private static char newStateForLocal(final char current, final long occupiedNeighbourCount) {
        return current == 'L' && occupiedNeighbourCount == 0 ? '#' :
               current == '#' && occupiedNeighbourCount >= 4 ? 'L' :
               current;
    }

    private static long part2(final char[][] layout) {
        final var updates = new HashMap<Position2D, Character>();
        streamPositions(layout).forEachOrdered((row, col) -> {
            final var occupiedInViewCount = DELTAS.stream().flatMap(delta ->
                    iterate(delta, nextDelta -> inBounds(layout, row, col, nextDelta), delta::add)
                            .map(nextDelta -> layout[row + nextDelta.x()][col + nextDelta.y()])
                            .takeWhile(state -> state != 'L')
                            .filter(single('#'))
            ).count();

            final var oldState = layout[row][col];
            final var newState = newStateForGlobal(oldState, occupiedInViewCount);
            if (oldState != newState) {
                updates.put(Position2D.of(row, col), newState);
            }
        });
        if (updates.isEmpty()) {
            return stream(layout).mapToLong(row -> count(row, '#')).sum();
        }
        updates.forEach((position, state) -> layout[position.x()][position.y()] = state);
        return part2(layout);
    }

    private static char newStateForGlobal(final char current, final long occupiedNeighbourCount) {
        return current == 'L' && occupiedNeighbourCount == 0 ? '#' :
               current == '#' && occupiedNeighbourCount >= 5 ? 'L' :
               current;
    }

    private static boolean inBounds(final char[][] layout, final int row, final int col, final Position2D delta) {
        return row + delta.x() >= 0 && row + delta.x()  < layout.length &&
               col + delta.y() >= 0 && col + delta.y() < layout[0].length;
    }
}