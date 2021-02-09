package go.solve.it;

import go.solve.it.util.Input;
import go.solve.it.util.Tuple;
import go.solve.it.util.Tuple.IntTuple;

import java.util.HashMap;

import static go.solve.it.util.Arrays.lastIndexOf;
import static java.util.Arrays.copyOfRange;
import static java.util.stream.IntStream.range;

public final class Day15 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("day15/input")));
        System.out.println(part2(Input.ints("day15/input")));
    }

    private static long part1(final int... startingNumbers) {
        final var numbers = copyOfRange(startingNumbers, 0, 2020);
        for (var i = startingNumbers.length; i < numbers.length; i++) {
            final var number = numbers[i - 1];
            final var previousIndex = lastIndexOf(numbers, number, i - 2);
            numbers[i] = previousIndex == -1 ? 0 : i - 1 - previousIndex;
        }
        return numbers[2019];
    }

    private static long part2(final int... startingNumbers) {
        final var noIndices = Tuple.ofInts(-1, -1);
        final var occurrences = new HashMap<Integer, IntTuple>();
        range(0, startingNumbers.length).forEach(index -> occurrences.put(startingNumbers[index], noIndices.tail(index)));

        var number = startingNumbers[startingNumbers.length - 1];
        for (var i = startingNumbers.length; i < 30000000; i++) {
            final var index = i;
            final var indices = occurrences.computeIfAbsent(number, __ -> noIndices);
            final var nextNumber = number = indices.head() == -1 ? 0 : indices.tail() - indices.head();
            occurrences.putIfAbsent(nextNumber, noIndices);
            occurrences.compute(nextNumber, (__, _indices) -> _indices.swap().tail(index));
        }
        return number;
    }
}