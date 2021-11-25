package go.solve.it.util.math;

import java.util.Optional;

import static java.util.Arrays.stream;

public sealed class Range<T extends Comparable<T>> permits Range.IntRange {

    protected final T from;
    protected final T toInclusive;

    Range(final T from, final T toInclusive) {
        this.from = from;
        this.toInclusive = toInclusive;
    }

    public static IntRange between(final int from, final int toInclusive) {
        return new IntRange(from, toInclusive);
    }

    public boolean contains(final T value) {
        return value.compareTo(from) >= 0 && value.compareTo(toInclusive) <= 0;
    }

    public boolean containsAny(final T... values) {
        return stream(values).anyMatch(this::contains);
    }

    public Optional<T> firstContained(final T... values) {
        return stream(values).filter(this::contains).findFirst();
    }

    public static final class IntRange extends Range<Integer> {

        IntRange(final int from, final int toInclusive) {
            super(from, toInclusive);
        }

        public int from() {
            return from;
        }

        public  int toInclusive() {
            return toInclusive;
        }

        public  int size() {
            return toInclusive() - from();
        }
    }
}
