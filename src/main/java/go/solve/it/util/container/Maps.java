package go.solve.it.util.container;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.stream.Collectors.toMap;

public final class Maps {

    public static <T> Map<T, T> deepCopy(final Map<? extends T, ? extends T> other, final UnaryOperator<T> copy) {
        return other.entrySet().stream().collect(toMap(e -> copy.apply(e.getKey()), e -> copy.apply(e.getValue())));
    }

    public static <K, V> K findKeyByValue(final Map<? extends K, ? extends V> map, final Predicate<? super V> predicate) {
        return map.entrySet().stream().filter(entry -> predicate.test(entry.getValue())).map(Entry::getKey).findAny().get();
    }
}
