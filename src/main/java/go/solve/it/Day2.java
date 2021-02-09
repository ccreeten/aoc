package go.solve.it;

import go.solve.it.util.Input;

import static go.solve.it.util.Constraints.is;
import static go.solve.it.util.Regex.match;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;

public final class Day2 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("day2/input")));
        System.out.println(part2(Input.lines("day2/input")));
    }

    private static long part1(final String... entries) {
        return stream(entries).filter(entry -> hasCorrectLetterCounts(entry)).count();
    }

    private static boolean hasCorrectLetterCounts(final String entry) {
        final var matcher = match(entry, "(\\d+)-(\\d+) (\\w): (\\w+)");
        final var lowerBound = parseLong(matcher.group(1));
        final var upperBound = parseLong(matcher.group(2));
        final var letter = matcher.group(3);
        final var password = matcher.group(4);
        final var count = stream(password.split("")).filter(letter::equals).count();
        return is(count).inRangeClosed(lowerBound, upperBound);
    }

    private static long part2(final String... entries) {
        return stream(entries).filter(entry -> hasCorrectLetterIndices(entry)).count();
    }

    private static boolean hasCorrectLetterIndices(final String entry) {
        final var matcher = match(entry, "(\\d+)-(\\d+) (\\w): (\\w+)");
        final var firstIndex = parseInt(matcher.group(1));
        final var secondIndex = parseInt(matcher.group(2));
        final var letter = matcher.group(3).charAt(0);
        final var password = matcher.group(4);
        return (password.charAt(firstIndex - 1) == letter) != (password.charAt(secondIndex - 1) == letter);
    }
}