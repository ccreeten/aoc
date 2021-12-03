package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.Map;
import java.util.function.Function;

import static go.solve.it.util.math.Positions.Position2D;
import static go.solve.it.util.math.Positions.Position3D;
import static go.solve.it.util.stream.Streams.stream;

public final class Day2 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day2/input")));
        System.out.println(part2(Input.lines("y2021/day2/input")));
    }

    private static int part1(final String... commands) {
        final var deltas = Map.of(
                "forward", Position2D.of(1, 0),
                "down", Position2D.of(0, 1),
                "up", Position2D.of(0, -1)
        );
        return stream(commands)
                .map(command -> Tuple.ofPartition(command, " ").mapTail(Integer::parseInt))
                .map(command -> deltas.get(command.head()).multiply(command.tail()))
                .reduce(Position2D::add)
                .map(position -> position.x() * position.y())
                .get();
    }

    private static int part2(final String... commands) {
        final var deltas = Map.<String, Function<Integer, Position3D>>of(
                "forward", aim -> Position3D.of(1, aim, 0),
                "down", __ -> Position3D.of(0, 0, 1),
                "up", __ -> Position3D.of(0, 0, -1)
        );
        final var result = stream(commands)
                .map(command -> Tuple.ofPartition(command, " ").mapTail(Integer::parseInt))
                .reduce(
                        Position3D.atOrigin(),
                        (previous, command) -> previous.add(deltas.get(command.head()).apply(previous.z()).multiply(command.tail())),
                        Position3D::add
                );
        return result.x() * result.y();
    }
}