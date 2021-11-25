package go.solve.it.y2019;

import go.solve.it.util.input.Input;

public final class Day5 {

    public static void main(final String... args) {
        System.out.println(part1(Input.ints("y2019/day5/input", ",")));
        System.out.println(part2(Input.ints("y2019/day5/input", ",")));
    }

    private static long part1(final int[] memory) {
        return emulate(memory, 1);
    }

    private static long part2(final int[] memory) {
        return emulate(memory, 5);
    }

    private static int emulate(final int[] memory, final int input) {
        for (var pc = 0; pc < memory.length;) {
            final var instruction = Instruction.read(memory[pc++]);
            final var opcode = instruction.popOp();
            if (opcode == 99) {
                return memory[0];
            }
            if (opcode == 4) {
                System.out.println(instruction.popRead(memory, pc++));
                continue;
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
                default -> input;
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