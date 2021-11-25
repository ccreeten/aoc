package go.solve.it.y2019;

import go.solve.it.util.input.Input;
import go.solve.it.util.node.TreeNode;

import java.util.Optional;
import java.util.Set;

import static go.solve.it.util.container.Collectionsβ.difference;
import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.rangeClosed;

public final class Day7 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2019/day7/input", ",")));
        System.out.println(part2(Input.ints("y2019/day7/input", ",")));
    }

    private static long part1(final int[] memory) {
        return calculateHighestSignal(Set.of(0, 1, 2, 3, 4), 0, memory);
    }

    private static long part2(final int[] memory) {
        final var head = rangeClosed('A', 'E').mapToObj(__ -> TreeNode.<int[]>empty().name(Character.toString(__)))
                .peek(amplifier -> amplifier.value(memory.clone()))
                .reduce((left, right) -> right.addChild(left))
                .get();
        head.find(not(TreeNode::hasChildren)).ifPresent(last -> last.addChild(head));

        return calculateHighestLoopbackSignal(
                Set.of(5, 6, 7, 8, 9), 0,
                head
        );
    }

    private static int calculateHighestSignal(final Set<Integer> phases, final int input, final int[] memory) {
        if (phases.isEmpty()) {
            return input;
        }
        return phases.stream()
                .mapToInt(phase -> calculateHighestSignal(difference(phases, phase), emulate(memory.clone(), phase, input).get(), memory))
                .max()
                .orElseThrow();
    }

    private static int calculateHighestLoopbackSignal(final Set<Integer> phases, final int input, final TreeNode<int[]> amplifier) {
        System.out.println(phases + " " + input + " " + amplifier.name());
//        if (phases.isEmpty()) {
//            return input;
//        }
        final var inputs = phases.isEmpty() ? Set.of(input) : phases;
        return inputs.stream()
                .map(phase -> {
                    final var result = emulate(amplifier.value(), phase, input)
                            .map(output -> calculateHighestLoopbackSignal(difference(phases, phase), output, amplifier.children().get(0)));
                    return result;
                })
                .filter(x -> x.isPresent())
                .mapToInt(integer -> integer.get())
//                .map(output -> )
                .max()
                .orElse(input);
    }

    private static Optional<Integer> emulate(final int[] memory, final int... input) {
        var inputIndex = 0;
        for (var pc = 0; pc < memory.length;) {
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

    static final class Instruction {

        private int digits;

        public Instruction(final int digits) {
            this.digits = digits;
        }

        public static Instruction read(final int digits) {
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