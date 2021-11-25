package go.solve.it.y2020;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;

import java.util.HashSet;

import static java.lang.Integer.parseInt;

public final class Day8 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day8/input")));
        System.out.println(part2(Input.lines("y2020/day8/input")));
    }

    private static long part1(final String... instructions) {
        return execute(0, 0, instructions).tail();
    }

    private static long part2(final String... instructions) {
        var pc = 0;
        var acc = 0;
        while (true) {
            final var instruction = Instruction.parse(instructions[pc]);
            switch (instruction.name()) {
                case "nop" -> {
                    final var result = execute(pc + instruction.value(), acc, instructions);
                    if (result.head()) {
                        return result.tail();
                    }
                    pc++;
                }
                case "jmp" -> {
                    final var result = execute(pc + 1, acc, instructions);
                    if (result.head()) {
                        return result.tail();
                    }
                    pc += instruction.value();
                }
                case "acc" -> {
                    acc += instruction.value();
                    pc++;
                }
            }
        }
    }

    private static Tuple<Boolean, Integer> execute(final int fromPc, final int startAcc, final String... instructions) {
        final var seenPc = new HashSet<Integer>();

        var pc = fromPc;
        var acc = startAcc;

        while (seenPc.add(pc)) {
            if (pc >= instructions.length) {
                return Tuple.of(true, acc);
            }
            final var instruction = Instruction.parse(instructions[pc]);
            switch (instruction.name()) {
                case "nop" -> pc++;
                case "jmp" -> pc += instruction.value();
                case "acc" -> {
                    acc += instruction.value();
                    pc++;
                }
            }
        }
        return Tuple.of(false, acc);
    }

    static record Instruction(String name, int value) {

        static Instruction parse(final String instruction) {
            return new Instruction(instruction.split(" ")[0], parseInt(instruction.split(" ")[1]));
        }
    }
}