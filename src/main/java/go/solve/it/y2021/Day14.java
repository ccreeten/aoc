package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;

public final class Day14 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day14/input")));
        System.out.println(part2(Input.lines("y2021/day14/input")));
    }

    private static long part1(final String[] lines) {
        String template = lines[0];
        Map<String, String> rules = IntStream.range(2, lines.length)
                .mapToObj(index -> Tuple.ofPartition(lines[index], " -> "))
                .map(t -> t.mapTail(tail -> insert(t.head(), tail)))
                .collect(Collectors.toMap(Tuple::head, Tuple::tail));
        String result = template;
        for (int i = 0; i < 10; i++) {
//            for (final Tuple<String, String> rule : rules) {
            result = apply(result, rules);
//            }
//            System.out.println(result);
        }
        var counts = stream(result.split(""))
                .collect(Collectors.groupingBy(Function.identity(), counting()));

        return counts.values().stream().mapToLong(x -> x).max().getAsLong()
                - counts.values().stream().mapToLong(x -> x).min().getAsLong();
    }

    private static String apply(final String result, final Map<String, String> rules) {
        String[] in = result.split("");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length - 1; i++) {
            String key = in[i] + in[i + 1];
            String value = rules.get(key);
            if (i != in.length - 2)
                out.append((value == null ? key : value).substring(0, 2));
            else
                out.append(value == null ? key : value);

        }
        return out.toString();
    }

    private static String insert(final String head, final String tail) {
        final String[] split = head.split("");
        return split[0] + tail + split[1];
    }

//    private static String apply(final String before, final Tuple<String, String> rule) {
//        return before.replaceAll(rule.head(), rule.tail());
//    }

    private static long part2(final String[] lines) {
        String template = lines[0];
        Map<String, String> rules = IntStream.range(2, lines.length)
                .mapToObj(index -> Tuple.ofPartition(lines[index], " -> "))
//                .map(t -> t.mapTail(tail -> insert(t.head(), tail)))
                .collect(Collectors.toMap(Tuple::head, Tuple::tail));
        Map<String, Long> counts = new HashMap<>();
        rules.forEach((from, to) -> counts.put(from, 0L));
        String[] in = template.split("");
//        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length - 1; i++) {
            String key = in[i] + in[i + 1];
            counts.merge(key, 1L, Long::sum);
        }
        String start = in[0] + in[ + 1];
        String end = in[in.length - 2] + in[in.length - 1];
        for (int round = 0; round < 40; round++) {
            new HashMap<>(counts).forEach((pair, count) -> {
                final String s = rules.get(pair);
                String next1 = pair.charAt(0) + s;
                String next2 = s + pair.charAt(1);
//                for (int i = 0; i < count; i++) {
                counts.merge(next1, count, Long::sum);
                counts.merge(next2, count, Long::sum);
                counts.merge(pair, count, (a, b) -> a - b);
//                }
            });
//            System.out.println(round);
            System.out.println(counts.values().stream().mapToLong(x ->x).sum());
        }
//        String result = template;
//        for (int i = 0; i < 40; i++) {
//            System.out.println(i);
////            for (final Tuple<String, String> rule : rules) {
//            result = apply2(result, rules);
////            }
////            System.out.println(result);
//        }
//        var counts = stream(result.split(""))
//                .collect(Collectors.groupingBy(Function.identity(), counting()));
//
        Map<Character, Long> elements = new HashMap<>();
        counts.forEach((pair, count) -> {
            elements.merge(pair.charAt(0), count, Long::sum);
            elements.merge(pair.charAt(1), count, Long::sum);
        });
        elements.merge(start.charAt(0), 1L, Long::sum);
        elements.merge(end.charAt(1), 1L, Long::sum);
        for (final Map.Entry<Character, Long> x : elements.entrySet()) {
            elements.put(x.getKey(), elements.get(x.getKey()) / 2);
        }
        System.out.println(elements);
        return elements.values().stream().mapToLong(x -> x).max().getAsLong()
                - elements.values().stream().mapToLong(x -> x).min().getAsLong();
    }

    private static String apply2(final String result, final Map<String, String> rules) {
        String[] in = result.split("");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length - 1; i++) {
            String key = in[i] + in[i + 1];
            String value = rules.get(key);
            if (i != in.length - 2)
                out.append((value == null ? key : value).substring(0, 2));
            else
                out.append(value == null ? key : value);

        }
        return out.toString();
    }
}