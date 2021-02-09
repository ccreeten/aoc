package go.solve.it.util;

import java.util.List;
import java.util.regex.Matcher;

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
}
