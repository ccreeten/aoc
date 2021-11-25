package go.solve.it.y2019;

import go.solve.it.util.input.Input;

public final class Day2 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2019/day2/input", ",")));
        System.out.println(part2(Input.ints("y2019/day2/input", ",")));
    }

    private static long part1(final int[] memory) {
        memory[1] = 12;
        memory[2] = 2;

        return emulate(memory);
    }

    private static long part2(final int[] memory) {
        for (var noun = 0; noun < 100; noun++) {
            for (var verb = 0; verb < 100; verb++) {
                memory[1] = noun;
                memory[2] = verb;

                final var result = emulate(memory.clone());
                if (result == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        throw new AssertionError();
    }

    private static int emulate(final int[] memory) {
        for (var pc = 0; pc < memory.length;) {
            final var opcode = memory[pc++];
            if (opcode == 99) {
                return memory[0];
            }
            final var result = opcode == 1
                    ? memory[memory[pc++]] + memory[memory[pc++]]
                    : memory[memory[pc++]] * memory[memory[pc++]];
            memory[memory[pc++]] = result;
        }
        return memory[0];
    }
}