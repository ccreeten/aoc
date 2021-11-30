package go.solve.it.y2019;

import go.solve.it.util.input.Input;
import go.solve.it.util.node.TreeNode;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static go.solve.it.util.container.Collectionsβ.difference;
import static go.solve.it.util.container.Collectionsβ.permutations;
import static go.solve.it.util.primitive.Ints.ints;
import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.rangeClosed;

public final class Day7 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2019/day7/input", ",")));
        System.out.println(part2(Input.ints("y2019/day7/input", ",")));
    }

    private static long part1(final int[] memory) {
        return calculateHighestSignal(List.of(0, 1, 2, 3, 4), 0, memory);
    }

    private static long part2(final int[] memory) {
        final var permutations = permutations(List.of(5, 6, 7, 8, 9));
        return permutations.stream()
                .mapToInt(phases -> calculateHighestLoopbackSignal(new ArrayDeque<>(phases), 0, createAmplifiers(memory)))
                .max()
                .getAsInt();
    }

    private static int calculateHighestSignal(final List<Integer> phases, final int input, final int[] memory) {
        if (phases.isEmpty()) {
            return input;
        }
        return phases.stream()
                .mapToInt(phase -> calculateHighestSignal(difference(phases, phase), Program.load(memory).emulate(phase, input).get(), memory))
                .max()
                .orElseThrow();
    }

    private static TreeNode<Program> createAmplifiers(final int[] memory) {
        final var head = rangeClosed('A', 'E').mapToObj(__ -> TreeNode.<Program>empty().name(Character.toString(__)))
                .peek(amplifier -> amplifier.value(Program.load(memory)))
                .reduce((left, right) -> right.addChild(left))
                .get();
        return head.get(not(TreeNode::hasChildren)).addChild(head);
    }

    private static int calculateHighestLoopbackSignal(final Queue<Integer> phases, final int input, final TreeNode<Program> amplifier) {
        final var inputs = phases.isEmpty() ? ints(input) : ints(phases.poll(), input);
        return amplifier.value().emulate(inputs)
                .map(output -> calculateHighestLoopbackSignal(phases, output, amplifier.parent()))
                .orElse(input);
    }

    static final class Program {

        private int pc;
        private int[] memory;

        private Program(final int pc, final int[] memory) {
            this.pc = pc;
            this.memory = memory;
        }

        static Program load(final int[] memory) {
            return new Program(0, memory.clone());
        }

        Optional<Integer> emulate(final int... input) {
            var inputIndex = 0;
            while (pc < memory.length) {
                final var instruction = Instruction.read(memory[pc++]);
                final var opcode = instruction.popOp();
                if (opcode == 99) {
                    return Optional.empty();
                }
                if (opcode == 4) {
                    return Optional.of(instruction.popRead(memory, pc++));
                }
                if (opcode == 5 || opcode == 6) {
                    pc = (opcode == 5) == (instruction.popRead(memory, pc++) != 0)
                            ? instruction.popRead(memory, pc++)
                            : pc + 1;
                    continue;
                }
                final var result = switch (opcode) {
                    case 1 -> instruction.popRead(memory, pc++) + instruction.popRead(memory, pc++);
                    case 2 -> instruction.popRead(memory, pc++) * instruction.popRead(memory, pc++);
                    case 7 -> instruction.popRead(memory, pc++) < instruction.popRead(memory, pc++) ? 1 : 0;
                    case 8 -> instruction.popRead(memory, pc++) == instruction.popRead(memory, pc++) ? 1 : 0;
                    default -> input[inputIndex++];
                };
                instruction.popWrite(memory, pc++, result);
            }
            throw new AssertionError();
        }

    }

    static final class Instruction {

        private int digits;

        private Instruction(final int digits) {
            this.digits = digits;
        }

        static Instruction read(final int digits) {
            return new Instruction(digits);
        }

        int popOp() {
            final var op = digits % 100;
            digits /= 100;
            return op;
        }

        int popRead(final int[] memory, final int pc) {
            final var mode = digits % 10;
            digits /= 10;
            return mode == 0 ? memory[memory[pc]] : memory[pc];
        }

        int popWrite(final int[] memory, final int pc, final int value) {
            final var mode = digits % 10;
            digits /= 10;
            return mode == 0 ? (memory[memory[pc]] = value) : (memory[pc] = value);
        }
    }
}