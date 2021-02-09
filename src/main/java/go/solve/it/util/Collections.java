package go.solve.it.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public final class Collections {

    public static <T> Set<T> setOf(final T... values) {
        return new HashSet<>(asList(values));
    }

    public static <T> List<T> concat(final List<? extends T> left, final List<? extends T> right) {
        final var result = new ArrayList<T>(left.size() + right.size());
        result.addAll(left);
        result.addAll(right);
        return result;
    }

    public static <T> Set<T> intersection(final Set<T> left, final Set<T> right) {
        final var result = new HashSet<>(left);
        result.retainAll(right);
        return result;
    }

    public static <T> Set<T> difference(final Set<T> left, final Set<T> right) {
        final var result = new HashSet<>(left);
        result.removeAll(right);
        return result;
    }
}
