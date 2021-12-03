package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import static go.solve.it.util.container.Arraysβ.reverse;
import static java.util.Arrays.stream;

public final class Day3 {

    public static void main(final String... args) {
        System.out.println(part1(Input.intsInBase("y2021/day3/input", 2)));
        System.out.println(part2(Input.intsInBase("y2021/day3/input", 2)));
    }

    private static int part1(final int... report) {
        final var codeLength = 12;
        final var setCounts = countSetBits(report, codeLength);
        var gammaRate = 0;
        for (final var count : reverse(setCounts)) {
            gammaRate <<= 1;
            gammaRate |= count * 2 > report.length ? 1 : 0;
        }
        return gammaRate * (~gammaRate & ((1 << codeLength) - 1));
    }

    private static int part2(final int... report) {
        final var codeLength = 12;
        return find(report, codeLength, codeLength - 1, true)
             * find(report, codeLength, codeLength - 1, false);
    }

    private static int find(final int[] report, final int codeLength, final int bitIndex, final boolean most) {
        if (report.length == 1) {
            return report[0];
        }
        final var setCounts = countSetBits(report, codeLength);
        final var remaining = stream(report)
                .filter(code -> (setCounts[bitIndex] * 2 >= report.length == most) == (((code >>> bitIndex) & 1) == 1))
                .toArray();
        return find(remaining, codeLength, bitIndex - 1, most);
    }

    private static int[] countSetBits(final int[] report, final int codeLength) {
        final var setCounts = new int[codeLength];
        for (var code : report) {
            var index = 0;
            while (code > 0) {
                setCounts[index++] += code & 1;
                code >>>= 1;
            }
        }
        return setCounts;
    }
}