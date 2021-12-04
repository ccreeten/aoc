package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import java.util.Map;
import java.util.function.Predicate;

import static go.solve.it.util.container.Arraysβ.split;
import static go.solve.it.util.math.Constraints.is;
import static go.solve.it.util.stream.Streams.stream;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.joining;

public final class Day4 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day4/input")));
        System.out.println(part2(Input.lines("y2020/day4/input")));
    }

    private static long part1(final String... lines) {
        return split(lines, String::isBlank)
                .map(entries -> entries.collect(joining(" ")))
                .filter(passport -> containsNecessaryFields(passport))
                .count();
    }

    private static boolean containsNecessaryFields(final String passport) {
        return stream("byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:").allMatch(passport::contains);
    }

    private static long part2(final String... lines) {
        return split(lines, String::isBlank)
                .map(entries -> entries.collect(joining(" ")))
                .filter(passport -> containsNecessaryFields(passport))
                .filter(passport -> allFieldsValid(passport))
                .count();
    }

    private static boolean allFieldsValid(final String passport) {
        final var rules = Map.<String, Predicate<String>>of(
                "cid:", __ -> true,
                "byr:", value -> is(parseLong(value)).inRangeClosed(1920, 2002),
                "iyr:", value -> is(parseLong(value)).inRangeClosed(2010, 2020),
                "eyr:", value -> is(parseLong(value)).inRangeClosed(2020, 2030),
                "hcl:", value -> value.matches("#[0-9a-f]{6}"),
                "ecl:", value -> value.matches("amb|blu|brn|gry|grn|hzl|oth"),
                "pid:", value -> value.matches("\\d{9}"),
                "hgt:", value ->
                        value.endsWith("cm") && is(parseLong(value.replace("cm", ""))).inRangeClosed(150, 193) ||
                        value.endsWith("in") && is(parseLong(value.replace("in", ""))).inRangeClosed(59, 76)
        );
        return stream(passport.split(" ")).allMatch(field -> rules.get(field.substring(0, 4)).test(field.substring(4)));
    }
}