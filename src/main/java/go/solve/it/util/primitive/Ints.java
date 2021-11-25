package go.solve.it.util.primitive;

import static java.lang.Math.*;

public final class Ints {

    private Ints() {
    }

    public static int[] split(final int value) {
        final var digits = new int[(int) ceil(log10(value))];
        var remainder = value;
        for (var i = digits.length - 1; i >= 0; i--) {
            digits[i] = remainder % 10;
            remainder /= 10;
        }
        return digits;
    }

    public static int digit(final int value, final int index) {
        return (int) (value / pow(10, index)) % 10;
    }

    public static int[] ints(final int... ints) {
        return ints;
    }
}
