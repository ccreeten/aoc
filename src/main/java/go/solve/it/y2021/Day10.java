package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.util.*;

public final class Day10 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day10/input")));
        System.out.println(part2(Input.lines("y2021/day10/input")));
    }

    private static long part1(final String[] lines) {
        long sum = 0;
        Set<Character> open = Set.of('{', '[', '(', '<');
        Map<Character, Integer> p = Map.of(
                ')', 3,
                ']', 57,
                '}', 1197,
                '>',25137);
        Map<Character, Character> cs = Map.of(
                '}', '{',
                ']', '[',
                ')', '(',
                '>','<');
        l: for (final String line : lines) {
            Deque<Character> stack = new ArrayDeque<>();
            for (final char c : line.toCharArray()) {
                if (open.contains(c)) {
                    stack.push(c);
                } else {
                    if (stack.isEmpty() || cs.get(c) != stack.pop()) {
                        sum += p.get(c);
                        continue l;
                    }
                }
            }
        }
        return sum;
    }

    private static long part2(final String[] lines) {
        long sum = 0;
        Set<Character> open = Set.of('{', '[', '(', '<');
        Map<Character, Integer> p = Map.of(
                ')', 1,
                ']', 2,
                '}', 3,
                '>',4);
        Map<Character, Character> cs = Map.of(
                '}', '{',
                ']', '[',
                ')', '(',
                '>','<');
        Map<Character, Character> css = Map.of(
                '{', '}',
                '[', ']',
                '(', ')',
                '<','>');
        List<Long> scores= new ArrayList<>();
        l: for (final String line : lines) {
            long score = 0;
            Deque<Character> stack = new ArrayDeque<>();
            for (final char c : line.toCharArray()) {
                if (open.contains(c)) {
                    stack.push(c);
                } else {
                    if (stack.isEmpty() || cs.get(c) != stack.pop()) {
//                        sum += p.get(c);
                        continue l;
                    }
                }
            }
            while(!stack.isEmpty()) {
                score *= 5;
                score += p.get(css.get(stack.pop()));
            }
            scores.add(score);
        }
        Collections.sort(scores);

        return scores.get(scores.size() / 2);
    }

}