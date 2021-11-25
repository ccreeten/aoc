package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import java.util.LinkedList;
import java.util.stream.LongStream;

import static go.solve.it.util.primitive.Longs.toArray;
import static java.util.stream.Collectors.toCollection;

public final class Day10 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("y2020/day10/input")));
        System.out.println(part2(Input.longs("y2020/day10/input")));
    }

    private static long part1(final long... adapters) {
        final var sorted = LongStream.of(adapters).sorted().mapToInt(Math::toIntExact).boxed().collect(toCollection(LinkedList::new));
        sorted.addFirst(0);
        sorted.addLast(sorted.getLast() + 3);

        var counts = new long[4];
        for (var i = 0; i < sorted.size() - 1; i++) {
            final var difference = sorted.get(i + 1) - sorted.get(i);
            counts[difference]++;
        }
        return counts[1] * counts[3];
    }

    private static long part2(final long... adapters) {
        final var sorted = LongStream.of(adapters).sorted().boxed().collect(toCollection(LinkedList::new));
        sorted.addFirst(0L);
        sorted.addLast(sorted.getLast() + 3);
        return countArrangements(toArray(sorted));
    }

    private static long countArrangements(final long... adapters) {
        return countArrangements(adapters, 1, new long[adapters.length]);
    }

    private static long countArrangements(final long[] adapters, final int index, final long[] cache) {
        if (index == adapters.length - 2) {
            return 1;
        }
        if (cache[index] != 0) {
            return cache[index];
        }

        final var prev = adapters[index - 1];
        final var cur = adapters[index];
        final var next = adapters[index + 1];

        final var tailCount = countArrangements(adapters, index + 1, cache);
        // if deltas before and after are not one of 1n1, 1n2, 2n1, we can't remove the number
        if ((cur - prev) + (next - cur) > 3) {
            return cache[index] = tailCount;
        }
        adapters[index] = prev;
        return tailCount + countArrangements(adapters, index + 1, cache);
    }
}