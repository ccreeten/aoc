package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import static go.solve.it.util.container.Arraysβ.contains;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;

public final class Day23 {

    public static void main(final String... args) {
        System.out.println(part1(Input.line("y2020/day23/input")));
        System.out.println(part2(Input.line("y2020/day23/input")));
    }

    private static String part1(final String labeling) {
        final var cups = stream(labeling.split("")).mapToInt(Integer::parseInt).toArray();
        final var links = playGame(cups, 100);
        var next = 1;
        var result = "";
        for (var i = 0; i < cups.length - 1; i++) {
            result += (next = links[next]);
        }
        return result;
    }

    private static long part2(final String labeling) {
        final var startingCups = stream(labeling.split("")).mapToInt(Integer::parseInt).toArray();
        final var cups = copyOfRange(startingCups, 0, 1_000_000);
        for (var label = startingCups.length; label < 1_000_000; label++) {
            cups[label] = label + 1;
        }
        final var links = playGame(cups, 10_000_000);
        return (long) links[1] * links[links[1]];
    }

    private static int[] playGame(final int[] cups, final int rounds) {
        final var links = new int[cups.length + 1];
        for (var label = 0; label < cups.length; label++) {
            links[cups[label]] = cups[wrappedIndex(label + 1, cups)];
        }
        var cup = cups[0];
        for (var round = 0; round < rounds; round++) {
            final var nextThree = new int[]{links[cup], links[links[cup]], links[links[links[cup]]]};
            var destination = wrappedIndex(cup - 2, cups) + 1;
            while (contains(nextThree, destination)) {
                destination = wrappedIndex(destination - 2, cups) + 1;
            }
            final var afterThree = links[destination];
            links[destination] = nextThree[0];
            links[cup] = links[nextThree[2]];
            links[nextThree[2]] = afterThree;
            cup = links[cup];
        }
        return links;
    }

    private static int wrappedIndex(final int index, final int[] cups) {
        return (index + cups.length) % cups.length;
    }
}