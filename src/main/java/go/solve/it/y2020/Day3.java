package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import static go.solve.it.util.primitive.Ints.ints;
import static go.solve.it.util.stream.Streams.zip;
import static java.util.Arrays.stream;

public final class Day3 {

    public static void main(final String... args) {
        System.out.println(part1(asGrid(Input.lines("y2020/day3/input"))));
        System.out.println(part2(asGrid(Input.lines("y2020/day3/input"))));
    }

    private static long part1(final char[][] grid) {
        return countTrees(grid, 1, 3);
    }

    private static long part2(final char[][] grid) {
        return zip(
                ints(1, 3, 5, 7, 1),
                ints(1, 1, 1, 1, 2),
                (right, down) -> countTrees(grid, down, right)
        ).reduce(1, Math::multiplyExact);
    }

    private static long countTrees(final char[][] grid, final int rowDelta, final int colDelta) {
        var count = 0;
        var row = 0;
        var col = 0;
        while (inBounds(grid, row += rowDelta, col += colDelta)) {
            if (grid[row][col] == '#') {
                count++;
            }
        }
        return count;
    }

    private static boolean inBounds(final char[][] grid, final int row, final int col) {
        return row >= 0 && col >= 0 && row < grid.length && col < grid[0].length;
    }

    private static char[][] asGrid(final String... lines) {
        return stream(lines).map(line -> line.repeat(lines.length)).map(String::toCharArray).toArray(char[][]::new);
    }
}