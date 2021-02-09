package go.solve.it.util;

public sealed class Range<T extends Comparable<T>> permits Range.IntRange {

    protected final T from;
    protected final T toInclusive;

    Range(final T from, final T toInclusive) {
        this.from = from;
        this.toInclusive = toInclusive;
    }

    public static <T extends Comparable<T>> Range<T> between(final T from, final T toInclusive) {
        return new Range<>(from, toInclusive);
    }

    public static IntRange between(final int from, final int toInclusive) {
        return new IntRange(from, toInclusive);
    }

    public boolean contains(final T value) {
        return value.compareTo(from) >= 0 && value.compareTo(toInclusive) <= 0;
    }

    public static final class IntRange extends Range<Integer> {

        IntRange(final int from, final int toInclusive) {
            super(from, toInclusive);
        }

        int from() {
            return from;
        }

        int toInclusive() {
            return toInclusive;
        }
    }
}
