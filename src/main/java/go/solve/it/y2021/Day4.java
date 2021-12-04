package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import static go.solve.it.util.container.Arraysβ.columns;
import static go.solve.it.util.container.Arraysβ.rows;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.concat;

public final class Day4 {

    public static void main(final String... args) {
        System.out.println(part1(Input.scan("y2021/day4/input")));
        System.out.println(part2(Input.scan("y2021/day4/input")));
    }

    private static int part1(final Scanner scan) {
        final var input = parseInput(scan);
        final var draws = input.head();
        final var boards = input.tail();

        for (final var draw : draws) {
            boards.forEach(board -> Positions.generateFor(board).forEach(position -> {
                if (board[position.y()][position.x()] == draw) {
                    board[position.y()][position.x()] = -1;
                }
            }));
            final var winningBoard = boards.stream()
                    .filter(board -> concat(rows(board), columns(board)).anyMatch(line -> stream(line).allMatch(number -> number == -1)))
                    .findFirst();
            if (winningBoard.isPresent()) {
                return draw * stream(winningBoard.get()).flatMapToInt(IntStream::of)
                        .filter(number -> number != -1)
                        .sum();
            }
        }
        throw new AssertionError();
    }

    private static int part2(final Scanner scan) {
        final var input = parseInput(scan);
        final var draws = input.head();
        final var boards = input.tail();

        for (final var draw : draws) {
            final var losingBoard = boards.get(0);
            boards.forEach(board -> Positions.generateFor(board).forEach(position -> {
                if (board[position.y()][position.x()] == draw) {
                    board[position.y()][position.x()] = -1;
                }
            }));
            boards.removeAll(
                    boards.stream()
                            .filter(board -> concat(rows(board), columns(board)).anyMatch(line -> stream(line).allMatch(number -> number == -1)))
                            .toList()
            );
            if (boards.isEmpty()) {
                return draw * stream(losingBoard).flatMapToInt(IntStream::of)
                        .filter(number -> number != -1)
                        .sum();
            }
        }
        throw new AssertionError();
    }

    private static Tuple<int[], List<int[][]>> parseInput(final Scanner scan) {
        final var draws = stream(scan.nextLine().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        final var boards = new ArrayList<int[][]>();
        while (scan.hasNextLine()) {
            scan.nextLine();
            boards.add(range(0, 5)
                    .mapToObj(__ -> stream(scan.nextLine().split(" ")).filter(not(String::isBlank)).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new));
        }
        return Tuple.of(draws, boards);
    }
}