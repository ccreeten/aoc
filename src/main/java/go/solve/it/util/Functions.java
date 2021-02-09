package go.solve.it.util;

public class Functions {

    @FunctionalInterface
    public interface IntToLongBinaryOperator {
        long apply(int left, int right);
    }
}
