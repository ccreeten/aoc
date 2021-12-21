package go.solve.it.y2021;

import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position2DL;

import java.util.HashMap;
import java.util.Map;

import static go.solve.it.util.string.Regex.findNext;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

public final class Day21 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day21/input")));
        System.out.println(part2(Input.lines("y2021/day21/input")));
    }

    private static long part1(final String... lines) {
        final var positions = stream(lines).map(line -> findNext(line, "(\\d)$")).mapToInt(Integer::parseInt).toArray();
        return play1(positions[0] - 1, 0, positions[1] - 1, 0, 1, 0);
    }

    private static long part2(final String... lines) {
        final var positions = stream(lines).map(line -> findNext(line, "(\\d)$")).mapToInt(Integer::parseInt).toArray();
        final var result = play2(positions[0] - 1, 0, positions[1] - 1, 0, true, new HashMap<>());
        return max(result.x(), result.y());
    }

    private static long play1(final long rp, final long srp, final long op, final long sop, final long die, final long rolls) {
        if (sop >= 1000) {
            return rolls * srp;
        }
        final var nrp = (rp + die * 3 + 3) % 10;
        final var nsrp = srp + nrp + 1;
        return play1(op, sop, nrp, nsrp, die + 3, rolls + 3);
    }

    private static Position2DL play2(final long rp, final long srp, final long op, final long sop, final boolean p1ToMove, final Map<Long, Position2DL> memo) {
        final var key = (rp << 40) | (srp << 30) | (op << 20) | (sop << 10) | (p1ToMove ? 0 : 1);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        if (sop >= 21) {
            return p1ToMove ? Position2DL.of(1, 0) : Position2DL.of(0, 1);
        }
        var start = Position2DL.atOrigin();
        for (var r1 = 1; r1 <= 3; r1++) {
            for (var r2 = 1; r2 <= 3; r2++) {
                for (var r3 = 1; r3 <= 3; r3++) {
                    final var nrp = (rp + (r1 + r2 + r3)) % 10;
                    final var nsrp = srp + nrp + 1;
                    start = start.add(play2(op, sop, nrp, nsrp, !p1ToMove, memo));
                }
            }
        }
        memo.put(key, start);
        return start;
    }
}
