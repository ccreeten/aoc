package go.solve.it.util.container;

import java.util.*;

import static java.util.Arrays.asList;

public final class Collectionsβ {

    public static <T> Set<T> copy(final Set<? extends T> other) {
        return new HashSet<>(other);
    }

    public static <T> Set<T> setOf(final T... values) {
        return new HashSet<>(asList(values));
    }

    public static <T> Queue<T> queueOf(final T... values) {
        return new ArrayDeque<>(asList(values));
    }

    public static <T> List<T> reverse(final List<? extends T> other) {
        final var result = new ArrayList<T>(other);
        java.util.Collections.reverse(result);
        return result;
    }

    public static <T> Set<T> concat(final T head, final Set<? extends T> tail) {
        final var result = new LinkedHashSet<T>(tail);
        result.add(head);
        return result;
    }

    public static <T> Set<T> concat(final Set<? extends T> left, final Set<? extends T> right) {
        final var result = new LinkedHashSet<T>(right);
        result.addAll(left);
        result.addAll(right);
        return result;
    }

    public static <T> List<T> concat(final T head, final List<? extends T> tail) {
        final var result = new ArrayList<T>(tail);
        result.add(head);
        return result;
    }

    public static <T> List<T> concat(final List<? extends T> left, final List<? extends T> right) {
        final var result = new ArrayList<T>(left.size() + right.size());
        result.addAll(left);
        result.addAll(right);
        return result;
    }

    public static <T> Set<T> intersection(final Set<? extends T> left, final Set<? extends T> right) {
        final var result = new HashSet<T>(left);
        result.retainAll(right);
        return result;
    }

    public static <T> Set<T> difference(final Set<? extends T> left, T right) {
        final var result = new HashSet<T>(left);
        result.remove(right);
        return result;
    }

    public static <T> Set<T> difference(final Set<? extends T> left, final Set<? extends T> right) {
        final var result = new HashSet<T>(left);
        result.removeAll(right);
        return result;
    }
}
