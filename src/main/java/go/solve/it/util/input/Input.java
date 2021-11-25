package go.solve.it.util.input;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

public final class Input {

    public static char[][] grid(final String resourceName) {
        return stream(lines(resourceName)).map(String::toCharArray).toArray(char[][]::new);
    }

    public static String line(final String resourceName) {
        return lines(resourceName)[0];
    }

    public static String[] lines(final String resourceName) {
        try (var lines = new BufferedReader(new InputStreamReader(read(resourceName))).lines()) {
            return lines.toArray(String[]::new);
        }
    }

    public static String[] lines(final String resourceName, final int count) {
        try (var lines = new BufferedReader(new InputStreamReader(read(resourceName))).lines()) {
            return lines.limit(count).toArray(String[]::new);
        }
    }

   public static int[] ints(final String resourceName) {
        return stream(lines(resourceName)[0].split(",")).mapToInt(Integer::parseInt).toArray();
   }

    public static int[] ints(final String resourceName, final String delimiter) {
        return stream(line(resourceName).split(delimiter)).mapToInt(Integer::parseInt).toArray();
    }

    public static long[] longs(final String resourceName) {
        return stream(lines(resourceName)).mapToLong(Long::parseLong).toArray();
    }

    public static long[] longs(final String resourceName, final String delimiter) {
        return stream(line(resourceName).split(delimiter)).mapToLong(Long::parseLong).toArray();
    }

    public static Scanner scan(final String resourceName) {
        return new Scanner(read(resourceName));
    }

    public static String string(final String resourceName) {
        return String.join("\n", lines(resourceName));
    }

    private static InputStream read(final String resourceName) {
        return currentThread().getContextClassLoader().getResourceAsStream(resourceName);
    }
}
