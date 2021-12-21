package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;

public final class Day10 {

    private static final Set<Character> OPEN = Set.of('{', '[', '(', '<');
    private static final Map<Character, Character> PAIRS = Map.of(
            '}', '{',
            ']', '[',
            ')', '(',
            '>', '<'
    );
    private static final Map<Character, Character> REVERSE_PAIRS = Map.of(
            '{', '}',
            '[', ']',
            '(', ')',
            '<', '>'
    );

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day10/input")));
        System.out.println(part2(Input.lines("y2021/day10/input")));
    }

    private static long part1(final String... lines) {
        final var points = Map.of(
                ')', 3L,
                ']', 57L,
                '}', 1197L,
                '>', 25137L
        );
        return stream(lines).mapToLong(line -> {
            final var stack = new ArrayDeque<Character>();
            for (final var character : line.toCharArray()) {
                if (OPEN.contains(character)) {
                    stack.push(character);
                } else {
                    if (stack.isEmpty() || PAIRS.get(character) != stack.pop()) {
                        return points.get(character);
                    }
                }
            }
            return 0;
        }).sum();
    }


    private static long part2(final String... lines) {
        final var points = Map.of(
                ')', 1L,
                ']', 2L,
                '}', 3L,
                '>', 4L
        );
        final var scores = stream(lines).map(line -> {
            final var stack = new ArrayDeque<Character>();
            for (final var character : line.toCharArray()) {
                if (OPEN.contains(character)) {
                    stack.push(character);
                    continue;
                }
                if (stack.isEmpty() || PAIRS.get(character) != stack.pop()) {
                    return new ArrayDeque<Character>();
                }
            }
            return stack;
        })
        .filter(not(Deque::isEmpty))
        .mapToLong(stack -> stack.stream().reduce(0L, (score, character) -> score * 5L + points.get(REVERSE_PAIRS.get(character)), Long::sum))
        .sorted()
        .toArray();

        return scores[scores.length / 2];
    }
}