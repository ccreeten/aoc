package go.solve.it.util;

import go.solve.it.util.Functions.BinaryIntToLongFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.IntStream.range;

public final class Streams {

    public static <T> Stream<T> stream(final T... elements) {
        return Stream.of(elements);
    }

    public static <T> Stream<Stream<T>> split(final T[] elements, final Predicate<? super T> splitOn) {
        return split(asList(elements), splitOn);
    }

    public static <T> Stream<Stream<T>> split(final Iterable<? extends T> elements, final Predicate<? super T> splitOn) {
        final List<List<T>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        elements.forEach(element -> {
            if (splitOn.test(element)) {
                result.add(new ArrayList<>());
                return;
            }
            result.get(result.size() - 1).add(element);
        });
        return result.stream().map(Collection::stream);
    }

    // pairwise operation except for self
    public static LongStream pairWise(final long[] values, final LongBinaryOperator mapping) {
        return range(0, values.length)
                .mapToObj(
                        left -> range(0, values.length)
                                .filter(right -> left != right)
                                .mapToLong(right -> mapping.applyAsLong(values[left], values[right]))
                )
                .flatMapToLong(identity());
    }

    public static LongStream zip(final int[] left, final int[] right, final BinaryIntToLongFunction function) {
        return range(0, min(left.length, right.length)).mapToLong(index -> function.apply(left[index], right[index]));
    }

    @SafeVarargs
    public static <T> Stream<T> concat(final Stream<T> left, final T... right) {
        return Stream.concat(left, Stream.of(right));
    }
}
