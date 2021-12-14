package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.node.GraphNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Day12 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day12/input")));
        System.out.println(part2(Input.lines("y2021/day12/input")));
    }

    private static long part1(final String... lines) {
        final HashMap<String, GraphNode<Void>> nodes = new HashMap<>();
        for (final String line : lines) {
            var ns = line.split("-");
            var left = nodes.computeIfAbsent(ns[0], name -> GraphNode.withName(name));
            var right = nodes.computeIfAbsent(ns[1], name -> GraphNode.withName(name));
            left.addNeighbour(right);
            right.addNeighbour(left);
        }
        var start = nodes.get("start");
        var end = nodes.get("end");
        return calculateDifferentPathsBetween(start, end);
    }

    private static long calculateDifferentPathsBetween(final GraphNode<Void> start, final GraphNode<Void> end) {
        final HashSet<String> seen = new HashSet<>();
        seen.add(start.name());
        return calculateDifferentPathsBetween(start, end, seen, start.name());
    }

    private static long calculateDifferentPathsBetween(final GraphNode<Void> start, final GraphNode<Void> end, final Set<String> seen, final String path) {
        if (start.name().equals(end.name())) {
            System.out.println(path);
            return 1;
        }
        return start.neighbours().stream()
                .filter(neighbour -> {
                    if (neighbour.name().toLowerCase().equals(neighbour.name())) {
                        return !seen.contains(neighbour.name());
                    }
                    return true;
                })
                .mapToLong(neighbour -> {
                    var n = new HashSet<>(seen);
                    n.add(neighbour.name());
                    return calculateDifferentPathsBetween(neighbour, end, n, path + "-" + neighbour.name());
                })
                .sum();

    }

    private static long part2(final String... lines) {
        final HashMap<String, GraphNode<Void>> nodes = new HashMap<>();
        for (final String line : lines) {
            var ns = line.split("-");
            var left = nodes.computeIfAbsent(ns[0], name -> GraphNode.withName(name));
            var right = nodes.computeIfAbsent(ns[1], name -> GraphNode.withName(name));
            left.addNeighbour(right);
            right.addNeighbour(left);
        }
        var start = nodes.get("start");
        var end = nodes.get("end");
        return calculateDifferentPathsBetween2(start, end);
    }

    private static long calculateDifferentPathsBetween2(final GraphNode<Void> start, final GraphNode<Void> end) {
        final HashSet<String> seen = new HashSet<>();
        seen.add(start.name());
        return calculateDifferentPathsBetween2(start, end, seen, start.name(), null);
    }

    private static long calculateDifferentPathsBetween2(final GraphNode<Void> start, final GraphNode<Void> end, final Set<String> seen, final String path, final String twice) {
        if (start.name().equals(end.name())) {
//            System.out.println(path);
            return 1;
        }
        return start.neighbours().stream()
//                .filter(neighbour -> {
//
//                })
                .mapToLong(neighbour -> {
                    if (neighbour.name().equals("start")) {
                        return 0;
                    }
                    final boolean smallCave = neighbour.name().toLowerCase().equals(neighbour.name());
                    if (smallCave) {
                        final boolean once = seen.contains(neighbour.name());
                        if (!once) {
                            var n = new HashSet<>(seen);
                            n.add(neighbour.name());
                            return calculateDifferentPathsBetween2(neighbour, end, n, path + "-" + neighbour.name(), twice);
                        }
                        if (twice == null) {
                            var n = new HashSet<>(seen);
//                            n.add(neighbour.name());
                            return calculateDifferentPathsBetween2(neighbour, end, n, path + "-" + neighbour.name(), neighbour.name());
                        }
                        return 0;
                    }
                    var n = new HashSet<>(seen);
                    n.add(neighbour.name());
                    return calculateDifferentPathsBetween2(neighbour, end, n, path + "-" + neighbour.name(), twice);
                })
                .sum();

    }
}