package go.solve.it.y2019;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.node.GraphNode;
import go.solve.it.util.node.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static go.solve.it.util.container.Collectionsβ.concat;
import static java.lang.Integer.MIN_VALUE;
import static java.util.function.Predicate.not;

public final class Day6 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2019/day6/input")));
        System.out.println(part2(Input.lines("y2019/day6/input")));
    }

    private static long part1(final String... orbits) {
        final var nodes = parseOrbits(
                TreeNode::withName,
                (center, satellite) -> satellite.addChild(center),
                orbits
        );
        return nodes.values().stream().mapToInt(TreeNode::size).sum() - nodes.size();
    }

    private static long part2(final String... orbits) {
        final var nodes = parseOrbits(
                GraphNode::withName,
                (center, satellite) -> center.addNeighbour(satellite.addNeighbour(center)),
                orbits
        );
        return countOrbitalTransfersBetween(nodes.get("YOU"), nodes.get("SAN"));
    }

    private static <T> Map<String, T> parseOrbits(final Function<String, T> creator, final BiConsumer<T, T> combiner, final String... orbits) {
        final var nodes = new HashMap<String, T>();
        for (final var orbit : orbits) {
            final var objects = Tuple.ofPartition(orbit, "\\)");
            final var center = nodes.computeIfAbsent(objects.head(), creator);
            final var satellite = nodes.computeIfAbsent(objects.tail(), creator);
            combiner.accept(center, satellite);
        }
        return nodes;
    }

    private static int countOrbitalTransfersBetween(final GraphNode<?> from, final GraphNode<?> to) {
        return shortestPathBetween(from, to, Set.of(from)) - 2;
    }

    private static int shortestPathBetween(final GraphNode<?> from, final GraphNode<?> to, final Set<GraphNode<?>> seen) {
        if (from.name().equals(to.name())) {
            return 0;
        }
        return from.neighbours().stream()
                .filter(not(seen::contains))
                .mapToInt(neighbour -> 1 + shortestPathBetween(neighbour, to, concat(neighbour, seen)))
                .filter(length -> length >= 0)
                .findFirst()
                .orElse(MIN_VALUE);
    }
}