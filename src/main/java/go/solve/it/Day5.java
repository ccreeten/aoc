package go.solve.it;

import go.solve.it.util.Input;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public final class Day5 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("day5/input")));
        System.out.println(part2(Input.lines("day5/input")));
    }

    private static int part1(final String... passes) {
        return stream(passes).mapToInt(pass -> toSeatID(pass)).max().orElse(-1);
    }

    private static int toSeatID(final String pass) {
        var row = 0;
        for (final var region : pass.substring(0, 7).toCharArray()) {
            row <<= 1;
            if (region == 'B') {
                row |= 1;
            }
        }
        var col = 0;
        for (final var region : pass.substring(7).toCharArray()) {
            col <<= 1;
            if (region == 'R') {
                col |= 1;
            }
        }
        return (row << 3) | col;
    }

    private static int part2(final String... passes) {
        final var seatIDs = stream(passes).map(pass -> toSeatID(pass)).sorted().collect(toList());
        for (var i = 0; i < seatIDs.size(); i++) {
            if (abs(seatIDs.get(i) - seatIDs.get(i + 1)) == 2) {
                return seatIDs.get(i) + 1;
            }
        }
        throw new AssertionError();
    }
}