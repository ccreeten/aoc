package go.solve.it.util.math;

public final class Constraints {

    public static IsLong is(final long value) {
        return new IsLong(value);
    }

    public static final class IsLong {

        private final long value;

        private IsLong(final long value) {
            this.value = value;
        }

        public boolean inRangeClosed(final long lowerBound, final long upperBound) {
            return lowerBound <= value && upperBound >= value;
        }
    }
}
