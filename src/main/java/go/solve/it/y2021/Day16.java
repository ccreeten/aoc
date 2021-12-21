package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;

public final class Day16 {

    public static void main(final String... args) {
        System.out.println(part1(Input.line("y2021/day16/input")));
        System.out.println(part2(Input.line("y2021/day16/input")));
    }

    private static long part1(final String message) {
        return sumVersions(BITS.decode(message));
    }

    private static long part2(final String message) {
        return evaluate(BITS.decode(message));
    }

    private static long sumVersions(final BITS bits) {
        final var version = bits.next(3);
        final var type = bits.next(3);

        if (type == 4) {
            while ((bits.next(5) >>> 4) != 0);
            return version;
        }

        if (bits.next(1) == 0) {
            final var length = bits.next(15);
            final var packet = bits.slice(length);
            var sum = 0L;
            while (packet.remaining()) {
                sum += sumVersions(packet);
            }
            return version + sum;
        }
        final var count = bits.next(11);
        var sum = 0;
        for (var i = 0; i < count; i++) {
            sum += sumVersions(bits);
        }
        return version + sum;
    }

    private static long evaluate(final BITS bits) {
        bits.next(3);
        final var type = bits.next(3);

        if (type == 4) {
            var literal = 0L;
            while (true) {
                final var chunk = bits.next(5);
                literal <<= 4;
                literal |= chunk & 0b1111;
                if ((chunk >>> 4) == 0) {
                    return literal;
                }
            }
        }
        if (bits.next(1) == 0) {
            final var length = bits.next(15);
            final var packet = bits.slice(length);
            return evaluateMulti(packet, type, packet::remaining);
        }
        final var count = new AtomicInteger(bits.next(11));
        return evaluateMulti(bits, type, () -> count.decrementAndGet() > 0);
    }

    private static long evaluateMulti(final BITS packet, final int type, final Supplier<Boolean> remaining) {
        var sum = evaluate(packet);
        while (remaining.get()) {
            if (type == 0) {
                sum += evaluate(packet);
            }
            if (type == 1) {
                sum *= evaluate(packet);
            }
            if (type == 2) {
                sum = min(sum, evaluate(packet));
            }
            if (type == 3) {
                sum = max(sum, evaluate(packet));
            }
            if (type == 5) {
                return sum > evaluate(packet) ? 1 : 0;
            }
            if (type == 6) {
                return sum < evaluate(packet) ? 1 : 0;
            }
            if (type == 7) {
                return sum == evaluate(packet) ? 1 : 0;
            }
        }
        return sum;
    }

    static final class BITS {

        private final String bits;
        private int offset;

        private BITS(final String bits) {
            this.bits = bits;
        }

        static BITS decode(final String message) {
            var bits = new BigInteger(message, 16).toString(2);
            while (bits.length() % 4 != 0) {
                bits = "0" + bits;
            }
            return new BITS(bits);
        }

        int next(final int count) {
            final var chunk = bits.substring(offset, offset + count);
            offset += count;
            return parseInt(chunk, 2);
        }

        BITS slice(final int count) {
            final var chunk = bits.substring(offset, offset + count);
            offset += count;
            return new BITS(chunk);
        }

        boolean remaining() {
            return offset < bits.length() - 1;
        }
    }
}