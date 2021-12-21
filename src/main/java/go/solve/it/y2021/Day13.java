package go.solve.it.y2021;

import go.solve.it.util.container.Tuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions;

import java.util.ArrayList;
import java.util.List;

import static go.solve.it.util.math.Positions.Position2D;
import static java.util.Arrays.stream;

public final class Day13 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2021/day13/input")));
        System.out.println(part2());
    }

    private static long part1(final String[] lines) {
        List<Position2D> f = new ArrayList<>();
        List<Tuple<String, Integer>> folds = new ArrayList<>();
        for (final String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.contains("=")) {
                var split = Tuple.ofPartition(line.replace("fold along ", ""), "=");
                folds.add(split.mapTail(Integer::parseInt));
                continue;
            }
            var split = Tuple.ofPartition(line, ",");
            Position2D pos = Position2D.of(split.mapHead(Integer::parseInt).head(), split.mapTail(Integer::parseInt).tail());
            f.add(pos);
        }
        var maxX = f.stream().mapToInt(Position2D::x).max().getAsInt();
        var maxY = f.stream().mapToInt(Position2D::y).max().getAsInt();
        int[][] grid = new int[maxY + 1][maxX + 1];
        f.forEach(p -> p.set(grid, 1));
        folds.forEach(fo -> fold(grid, fo));

        final char[][] xx = new char[grid.length][grid[0].length];
        Positions.generateFor(grid)
                .forEach(pos -> xx[pos.y()][pos.x()] = pos.get(grid) == 1 ? '#' : ' ');
//        fold(grid, folds.get(0));
        for (int i = 0; i < 16; i++) {
            System.out.println(new String(xx[i]));
        }
        return stream(grid).flatMapToInt(x -> stream(x)).sum();
    }

    static int divx = 1;
    static int divy = 1;

    private static void fold(final int[][] grid, final Tuple<String, Integer> fold) {
        System.out.println(fold.tail());
        int mirr = 2;
        if (fold.head().equals("x")) {
            int x = fold.tail() + 1;
            int toX = (grid[0].length) / divx;
            divx *= 2;
            for (; x < toX; x++) {
                for (int y = 0; y < grid.length; y++) {
                    grid[y][x - mirr] |= grid[y][x];
                    grid[y][x] = 0;
//                    grid[y - mirr][x] |= grid[y][x];
//                    grid[y][x] = 0;
                }
                mirr+=2;
            }
            return;
        }
        int y = fold.tail() + 1;
        int toY = (grid.length) / divy;
        divy *= 2;
        for (; y < toY; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y - mirr][x] |= grid[y][x];
                grid[y][x] = 0;
            }
            mirr+=2;
        }
    }

    private static long part2() {
        throw new AssertionError();
    }
}