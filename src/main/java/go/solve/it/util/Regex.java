package go.solve.it.util;

import java.util.List;
import java.util.regex.Matcher;

import static java.lang.String.format;
import static java.util.Locale.ROOT;
import static java.util.function.UnaryOperator.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.iterate;

public final class Regex {

    public static Matcher match(final String entry, final String regex) {
        final var matcher = compile(regex).matcher(entry);
        if (!matcher.matches()) {
            throw new AssertionError();
        }
        return matcher;
    }

    // assume single capture group contained in regex
    public static List<String> findAll(final String entry, final String regex) {
        final var matcher = compile(regex).matcher(entry);
        return iterate(matcher, Matcher::find, identity()).map(result -> result.group(1)).collect(toList());
    }

    // assume single capture group contained in regex and that result exists
    public static String findNext(final String entry, final String regex, final int fromIndex) {
        final var results = findAll(entry.substring(fromIndex), regex);
        if (results.isEmpty()) {
            throw new IllegalArgumentException(format(ROOT, "could not match '%s' in '%s' starting from index %d", regex, entry, fromIndex));
        }
        return results.get(0);
    }
}
