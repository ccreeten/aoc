package go.solve.it.y2020;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static go.solve.it.util.string.Regex.findAll;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public final class Day7 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day7/input")));
        System.out.println(part2(Input.lines("y2020/day7/input")));
    }

    private static long part1(final String... rules) {
        final var ruleSet = stream(rules).collect(toMap(
                rule -> findAll(rule, "^(.*?) bags").get(0),
                rule -> findAll(rule, "\\d (.*?) bags?").stream().collect(toSet())
        ));
        return findContainers(ruleSet, "shiny gold").size();
    }

    private static Set<String> findContainers(final Map<String, Set<String>> ruleSet, final String bag) {
        final var containers = new HashSet<String>();
        ruleSet.forEach((container, contained) -> {
            if (contained.contains(bag) && containers.add(container)) {
                containers.addAll(findContainers(ruleSet, container));
            }
        });
        return containers;
    }

    private static long part2(final String... rules) {
        final var ruleSet = stream(rules).collect(toMap(
                rule -> findAll(rule, "^(.*?) bags").get(0),
                rule -> findAll(rule, "(\\d .*?) bags?").stream().map(bagCount -> parseBagCount(bagCount)).collect(toSet())
        ));
        return countNestedContainers(ruleSet, "shiny gold");
    }

    private static long countNestedContainers(final Map<String, Set<Tuple<Long, String>>> ruleSet, final String bag) {
        return ruleSet.get(bag).stream()
                .mapToLong(bagSet -> bagSet.head() + bagSet.head() * countNestedContainers(ruleSet, bagSet.tail()))
                .sum();
    }

    private static Tuple<Long, String> parseBagCount(final String bagSet) {
        return Tuple.ofPartition(bagSet, " ").mapHead(Long::parseLong);
    }
}