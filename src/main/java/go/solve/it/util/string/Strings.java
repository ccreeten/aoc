package go.solve.it.util.string;

public final class Strings {

    public static String reverse(final String string) {
        return new StringBuilder(string).reverse().toString();
    }
}
