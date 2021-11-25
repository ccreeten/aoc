package go.solve.it.y2020;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public final class Day19 {

    public static void main(final String... args) {
        System.out.println(part1(Input.string("y2020/day19/input")));
        System.out.println(part2(Input.string("y2020/day19/input")));
    }

    private static long part1(final String input) {
        final var rules = input.split("\n\n")[0].split("\n");
        final var messages = input.split("\n\n")[1].split("\n");

        final var ruleSet = parseRules(rules);
        final var compiledRule = Pattern.compile(toRegex(ruleSet, ruleSet.get("0")));
        return stream(messages).filter(message -> compiledRule.matcher(message).matches()).count();
    }

    private static long part2(final String input) {
        final var rules = input.split("\n\n")[0].split("\n");
        final var messages = input.split("\n\n")[1].split("\n");

        final var ruleSet = new HashMap<>(parseRules(rules));
        ruleSet.put("8", range(1, 32).mapToObj(count -> join(" ", nCopies(count, "42"))).collect(joining(" | ")));
        ruleSet.put("11", range(1, 32).mapToObj(count -> join(" ", nCopies(count, "42")) + " " + join(" ", nCopies(count, "31"))).collect(joining(" | ")));

        final var compiledRule = Pattern.compile(toRegex(ruleSet, ruleSet.get("0")));
        return stream(messages).filter(message -> compiledRule.matcher(message).matches()).count();
    }

    private static Map<String, String> parseRules(final String... rules) {
        return stream(rules)
                .map(rule -> rule.replaceAll("\"", ""))
                .map(rule -> Tuple.ofPartition(rule, ": "))
                .collect(toMap(Tuple::head, Tuple::tail));
    }

    private static String toRegex(final Map<String, String> ruleSet, final String rule) {
        if (rule.matches("[ab]")) {
            return rule;
        }
        return stream(rule.split(" \\| "))
                .map(sequence -> stream(sequence.split(" ")).map(token -> toRegex(ruleSet, ruleSet.get(token))).collect(joining("")))
                .collect(joining("|", "(", ")"));
    }
}