package go.solve.it.util.container;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

public final class Iterators {

    private Iterators() {
    }

    public static <T> Stream<T> stream(final Iterator<? extends T> iterator) {
        return StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
}
