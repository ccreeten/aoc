package go.solve.it;

import go.solve.it.util.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static go.solve.it.util.Arrays.sum;
import static go.solve.it.util.Arrays.tail;
import static go.solve.it.util.Collections.concat;
import static go.solve.it.util.Regex.match;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public final class Day14 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("day14/input")));
        System.out.println(part2(Input.lines("day14/input")));
    }

    private static long part1(final String... instructions) {
        return execute(new long[64 * 1024], new char[36], instructions);
    }

    private static long execute(final long[] memory, final char[] mask, final String... instructions) {
        if (instructions.length == 0) {
            return sum(memory);
        }
        final var instruction = instructions[0];
        if (instruction.startsWith("mask")) {
            return execute(memory, match(instruction, "mask = (.*)").group(1).toCharArray(), tail(instructions));
        }
        final var address = parseInt(match(instruction, "mem\\[(\\d*)\\] = (\\d*)").group(1));
        final var value = parseLong(match(instruction, "mem\\[(\\d*)\\] = (\\d*)").group(2));
        memory[address] = maskValue(value, mask);
        return execute(memory, mask, tail(instructions));
    }

    private static long maskValue(final long value, final char[] mask) {
        var result = value;
        for (var i = 0; i < mask.length; i++) {
            result = switch (mask[mask.length - i - 1]) {
                case '0' -> result & ~(1L << i);
                case '1' -> result | 1L << i;
                case 'X' -> result;
                default -> throw new AssertionError();
            };
        }
        return result;
    }

    private static long part2(final String... instructions) {
        return execute(new HashMap<>(), new char[36], instructions);
    }

    private static long execute(final Map<Long, Long> memory, final char[] mask, final String... instructions) {
        if (instructions.length == 0) {
            return memory.values().stream().mapToLong(Long::valueOf).sum();
        }
        final var instruction = instructions[0];
        if (instruction.startsWith("mask")) {
            return execute(memory, match(instruction, "mask = (.*)").group(1).toCharArray(), tail(instructions));
        }
        final var memoryInstruction = match(instruction, "mem\\[(\\d*)\\] = (\\d*)");
        final var address = parseLong(memoryInstruction.group(1));
        final var value = parseLong(memoryInstruction.group(2));
        maskedAddresses(address, mask).forEach(_address -> memory.put(_address, value));
        return execute(memory, mask, tail(instructions));
    }

    private static List<Long> maskedAddresses(final long address, final char[] mask) {
        return maskedAddresses(address, mask, 0);
    }

    private static List<Long> maskedAddresses(final long address, final char[] mask, final int maskIndex) {
        if (maskIndex == mask.length) {
            return List.of(address);
        }
        return switch (mask[mask.length - maskIndex - 1]) {
            case '0' -> maskedAddresses(address, mask, maskIndex + 1);
            case '1' -> maskedAddresses(address | 1L << maskIndex, mask, maskIndex + 1);
            case 'X' -> concat(
                    maskedAddresses(address & ~(1L << maskIndex), mask, maskIndex + 1),
                    maskedAddresses(address | 1L << maskIndex, mask, maskIndex + 1)
            );
            default -> throw new AssertionError();
        };
    }
}