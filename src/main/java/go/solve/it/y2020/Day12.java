package go.solve.it.y2020;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position2D;

import static go.solve.it.util.container.Arraysβ.indexOf;
import static go.solve.it.util.container.Arraysβ.tail;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public final class Day12 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day12/input")));
        System.out.println(part2(Input.lines("y2020/day12/input")));
    }

    private static int part1(final String... instructions) {
        final var location = move('E', Position2D.of(0, 0), instructions);
        return abs(location.x()) + abs(location.y());
    }

    private static Position2D move(final char orientation, final Position2D location, final String... instructions) {
        if (instructions.length == 0) {
            return location;
        }
        final var instruction = instructions[0];
        final var action = instruction.charAt(0);
        final var value = parseInt(instruction.substring(1));
        return switch (action) {
            case 'N' -> move(orientation, location.translateX(+value), tail(instructions));
            case 'S' -> move(orientation, location.translateX(-value), tail(instructions));
            case 'E' -> move(orientation, location.translateY(+value), tail(instructions));
            case 'W' -> move(orientation, location.translateY(-value), tail(instructions));
            case 'L' -> move(rotate(orientation, -value), location, tail(instructions));
            case 'R' -> move(rotate(orientation, +value), location, tail(instructions));
            case 'F' -> move(orientation, move(orientation, location, orientation + "" + value), tail(instructions));
            default -> throw new AssertionError();
        };
    }

    private static int part2(final String... instructions) {
        final var location = move(Position2D.of(1, 10), Position2D.of(0, 0), instructions);
        return abs(location.x()) + abs(location.y());
    }

    private static Position2D move(final Position2D relativeWaypointLocation, final Position2D shipLocation, final String... instructions) {
        if (instructions.length == 0) {
            return shipLocation;
        }
        final var instruction = instructions[0];
        final var action = instruction.charAt(0);
        final var value = parseInt(instruction.substring(1));
        return switch (action) {
            case 'N' -> move(relativeWaypointLocation.translateX(+value), shipLocation, tail(instructions));
            case 'S' -> move(relativeWaypointLocation.translateX(-value), shipLocation, tail(instructions));
            case 'E' -> move(relativeWaypointLocation.translateY(+value), shipLocation, tail(instructions));
            case 'W' -> move(relativeWaypointLocation.translateY(-value), shipLocation, tail(instructions));
            case 'L' -> move(rotate(relativeWaypointLocation, -value), shipLocation, tail(instructions));
            case 'R' -> move(rotate(relativeWaypointLocation, +value), shipLocation, tail(instructions));
            case 'F' -> move(relativeWaypointLocation, shipLocation.add(relativeWaypointLocation.multiply(value)), tail(instructions));
            default -> throw new AssertionError();
        };
    }

    private static Position2D rotate(final Position2D waypointLocation, final int degrees) {
        final var rotations = ((degrees / 90) + 4) % 4;
        return switch (rotations) {
            case 1 ->  waypointLocation.swap().multiplyX(-1);
            case 2 ->  waypointLocation.multiply(-1);
            case 3 ->  waypointLocation.swap().multiplyY(-1);
            default -> throw new AssertionError();
        };
    }

    private static char rotate(final char orientation, final int degrees) {
        final var orientations = new char[]{'N', 'E', 'S', 'W'};
        return orientations[((degrees / 90) + 4 + indexOf(orientations, orientation)) % 4];
    }
}