package go.solve.it.util.function;

public final class Functions {

    @FunctionalInterface
    public interface BinaryIntToLongFunction {
        long apply(int left, int right);
    }

    @FunctionalInterface
    public interface BinaryIntToCharFunction {
        char apply(int left, int right);
    }
}
