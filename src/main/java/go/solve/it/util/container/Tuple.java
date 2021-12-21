package go.solve.it.util.container;

import java.util.Objects;
import java.util.function.Function;

public sealed class Tuple<L, R> permits Tuple.IntTuple, Tuple.LongTuple {

    private final L head;
    private final R tail;

    Tuple(final L head, final R tail) {
        this.head = head;
        this.tail = tail;
    }

    public static Tuple<String, String> ofPartition(final String string, final String pattern) {
        final var split = string.split(pattern, 2);
        return Tuple.of(split[0], split[1]);
    }

    public static <L, R> Tuple<L, R> of(final L head, final R tail) {
        return new Tuple<>(head, tail);
    }

    public static IntTuple ofInts(final int head, final int tail) {
        return new IntTuple(head, tail);
    }

    public static LongTuple ofLongs(final long head, final long tail) {
        return new LongTuple(head, tail);
    }

    public L head() {
        return head;
    }

    public R tail() {
        return tail;
    }

    public <T> Tuple<T, R> mapHead(final Function<? super L, ? extends T> mapping) {
        return new Tuple<>(mapping.apply(head), tail);
    }

    public <T> Tuple<L, T> mapTail(final Function<? super R, ? extends T> mapping) {
        return new Tuple<>(head, mapping.apply(tail));
    }

    public static final class IntTuple extends Tuple<Integer, Integer> {

        IntTuple(final int head, final int tail) {
            super(head, tail);
        }

        public IntTuple add(final IntTuple other) {
            return new IntTuple(head() + other.head(), tail() + other.tail());
        }

        public IntTuple swap() {
            return new IntTuple(tail(), head());
        }

        public IntTuple tail(final int value) {
            return new IntTuple(head(), value);
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof IntTuple && (Objects.equals(head(), ((IntTuple) obj).head()) && Objects.equals(tail(), ((IntTuple) obj).tail()));
        }

        @Override
        public int hashCode() {
            return head() * 31  + tail();
        }

        @Override
        public String toString() {
            return "("+head()+","+tail()+")";
        }
    }

    public static final class LongTuple extends Tuple<Long, Long> {

        LongTuple(final long head, final long tail) {
            super(head, tail);
        }
    }
}
