package go.solve.it.util.math;

public final class Maths {

    private Maths() {
    }

    public static long factorial(final int value) {
        if (value <= 1) {
            return 1;
        }
        return value * factorial(value - 1);
    }
}
