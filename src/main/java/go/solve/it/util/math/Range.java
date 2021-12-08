package go.solve.it.util.math;

import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.iterate;

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

    public boolean isIncreasing() {
        return from.compareTo(toInclusive) < 0;
    }

    public boolean isDecreasing() {
        return from.compareTo(toInclusive) > 0;
    }

    public boolean contains(final T value) {
        return isIncreasing()
                ? value.compareTo(from) >= 0 && value.compareTo(toInclusive) <= 0
                : value.compareTo(toInclusive) >= 0 && value.compareTo(from) <= 0;
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

        public int toInclusive() {
            return toInclusive;
        }

        public int size() {
            return abs(toInclusive() - from());
        }

        public Stream<Integer> values() {
            return (isIncreasing()
                    ? iterate(from(), value -> value <= toInclusive(), value -> value + 1)
                    : iterate(from(), value -> value >= toInclusive(), value -> value - 1))
                    .boxed();
        }
    }
}
