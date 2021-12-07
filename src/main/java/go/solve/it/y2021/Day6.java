package go.solve.it.y2021;

import go.solve.it.util.input.Input;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public final class Day6 {

    public static void main(final String... args) {
        System.out.println(part1(Input.longs("y2021/day6/input", ",")));
        System.out.println(part2(Input.longs("y2021/day6/input", ",")));
    }

    private static long part1(final long... timers) {
        return stream(timers).map(timer -> countFishAfterDays(timer, 80)).sum();
    }

    private static long part2(final long... timers) {
        return stream(timers).map(timer -> countFishAfterDays(timer, 256)).sum();
    }

    private static final Map<Long, Long> CACHE = new HashMap<>();

    private static long countFishAfterDays(final long timer, final long days) {
        if (days == 0) {
            return 1;
        }
        final var key = timer << 32 | days;
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        final var count = timer > 0
                ? countFishAfterDays(timer - 1, days - 1)
                : countFishAfterDays(6, days - 1) + countFishAfterDays(8, days - 1);
        CACHE.put(key, count);
        return count;
    }
}