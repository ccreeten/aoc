package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;

public final class Day18 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day18/input")));
        System.out.println(part2(Input.lines("y2021/day18/input")));
    }

    private static long part1(final String... lines) {
        return stream(lines).map(Day18::parse)
                .reduce((left, right) -> add(left, right))
                .map(number -> magnitude(number))
                .get();
    }

    private static long part2(final String... lines) {
        return stream(lines).map(Day18::parse)
                .flatMapToLong(left ->
                        stream(lines).map(Day18::parse).mapToLong(right -> magnitude(add(left.copy(), right.copy())))
                )
                .max()
                .getAsLong();
    }

    private static long magnitude(final SN number) {
        if (number.isValue()) {
            return number.value();
        }
        return 3 * magnitude(number.l()) + 2 * magnitude(number.r());
    }

    private static SN add(final SN left, final SN right) {
        return reduce(SN.ofPair(left, right));
    }

    private static SN reduce(final SN sn) {
        while (true) {
            if (!explode(sn) && !split(sn)) {
                return sn;
            }
        }
    }

    private static boolean split(final SN sn) {
        if (sn.isValue()) {
            final var value = sn.value();
            if (value >= 10) {
                sn.parent().replace(sn, SN.ofPair(value / 2, value - value / 2));
                return true;
            }
            return false;
        }
        return split(sn.l()) || split(sn.r());
    }

    private static boolean explode(SN sn) {
        return explode(sn, 0);
    }

    private static boolean explode(SN sn, int depth) {
        if (sn.isValue()) {
            return false;
        }
        if (depth >= 4 && sn.l().isValue() && sn.r().isValue()) {
            var l = sn.l().value();
            var r = sn.r().value();

            SN child = sn;
            SN parent = sn.parent();
            while (parent != null && parent.l() == child) {
                child = parent;
                parent = child.parent();
            }
            if (parent != null) {
                child = parent.l();
                while (!child.isValue()) {
                    child = child.r();
                }
                child.value(child.value() + l);
            }
            child = sn;
            parent = sn.parent();
            while (parent != null && parent.r() == child) {
                child = parent;
                parent = child.parent();
            }
            if (parent != null) {
                child = parent.r();
                while (!child.isValue()) {
                    child = child.l();
                }
                child.value(child.value() + r);
            }
            sn.parent().replace(sn, 0);
            return true;
        }
        return explode(sn.l(), depth + 1) || explode(sn.r(), depth + 1);
    }

    static SN parse(final String line) {
        final var c = line.charAt(0);
        if (c != '[') {
            return SN.ofValue(parseInt(line));
        }
        var closingPointer = 1;
        var balanceCount = 1;
        while (true) {
            final var t = line.charAt(closingPointer);
            if (t == '[') {
                balanceCount++;
            }
            if (t == ']') {
                balanceCount--;
            }
            if (balanceCount == 1) {
                return SN.ofPair(parse(line.substring(1, closingPointer + 1)), parse(line.substring(closingPointer + 2, line.length() - 1)));
            }
            closingPointer++;
        }
    }

    static final class SN {

        private final boolean pair;

        private SN parent;
        private int value;
        private SN l;
        private SN r;

        private SN(final SN l, final SN r, final boolean b) {
            this.l = l.parent(this);
            this.r = r.parent(this);
            this.pair = b;
        }

        private SN(final int value, final boolean b) {
            this.value = value;
            this.pair = b;
        }

        static SN ofValue(final int value) {
            return new SN(value, false);
        }

        static SN ofPair(final SN left, final SN right) {
            return new SN(left, right, true);
        }

        static SN ofPair(final int left, final int right) {
            return new SN(ofValue(left), ofValue(right), true);
        }

        public SN parent() {
            return parent;
        }

        public int value() {
            return value;
        }

        public SN l() {
            return l;
        }

        public SN r() {
            return r;
        }

        public boolean isPair() {
            return pair;
        }

        public boolean isValue() {
            return !isPair();
        }

        public SN parent(final SN parent) {
            this.parent = parent;
            return this;
        }

        public SN value(final int value) {
            this.value = value;
            return this;
        }

        public SN replace(final SN sn, final int value) {
            return replace(sn, ofValue(value));
        }

        public SN replace(final SN sn, final SN value) {
            if (sn == l) {
                l = value.parent(this);
                return this;
            }
            if (sn == r) {
                r = value.parent(this);
                return this;
            }
            throw new AssertionError();
        }

        SN copy() {
            if (isValue()) {
                return SN.ofValue(value);
            }
            return ofPair(l.copy(), r.copy());
        }
    }
}