package go.solve.it.y2020;

import go.solve.it.util.container.Arraysβ;
import go.solve.it.util.function.Functions.BinaryIntToCharFunction;
import go.solve.it.util.input.Input;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static go.solve.it.util.container.Arraysβ.slice;
import static go.solve.it.util.container.Collectionsβ.difference;
import static go.solve.it.util.stream.BiStream.streamPositions;
import static go.solve.it.util.stream.Streams.concat;
import static go.solve.it.util.stream.Streams.split;
import static go.solve.it.util.string.Regex.findNext;
import static go.solve.it.util.string.Strings.reverse;
import static java.lang.Long.parseLong;
import static java.lang.Math.sqrt;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.iterate;

public final class Day20 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day20/input")));
        System.out.println(part2(Input.lines("y2020/day20/input")));
    }

    private static long part1(final String... lines) {
        return findCornerTiles(parseTiles(lines)).stream().mapToLong(Tile::id).reduce(1, Math::multiplyExact);
    }

    private static List<Tile> findCornerTiles(final List<Tile> tiles) {
        return tiles.stream().filter(tile -> {
            final var sides = tiles.stream().filter(not(tile::equals)).flatMap(Tile::sides).collect(toSet());
            final var matchingSideCount = tile.sides()
                    .filter(side -> !sides.contains(side) && !sides.contains(reverse(side)))
                    .count();
            return matchingSideCount == 2;
        }).collect(toList());
    }

    private static long part2(final String... lines) {
        final var monster = Tile.create(0, Stream.of(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   ").map(String::toCharArray).toArray(char[][]::new)
        );

        final var tiles = parseTiles(lines);
        final var image = stitchTogether(tiles);
        final var monsterCount = image.orientations()
                .mapToLong(oriented -> streamPositions(oriented.grid()).filter((row, col) -> isMonsterAt(oriented, monster, row, col)).count())
                .sum();

        return image.count('#') - monsterCount * monster.count('#');
    }

    private static List<Tile> parseTiles(final String... lines) {
        return split(lines, String::isBlank)
                .map(tileLines -> tileLines.toArray(String[]::new))
                .map(tileLines -> Tile.parse(tileLines[0], slice(tileLines, 1, tileLines.length)))
                .collect(toList());
    }

    private static Tile stitchTogether(final List<Tile> tiles) {
        final var sideLength = (int) sqrt(tiles.size());
        final var image = new Tile[sideLength][sideLength];

        fitPieces(image, 0, 0, new HashSet<>(tiles));
        streamPositions(image).forEachOrdered((row, col) -> image[row][col] = image[row][col].borderless());

        final var imageGrid = stream(image).flatMap(rowOfTiles ->
                range(0, image[0][0].grid().length).mapToObj(tileRow -> range(0, image.length).mapToObj(tilesCol ->
                        rowOfTiles[tilesCol].grid()[tileRow]).reduce(new char[0], Arraysβ::concat)
                )
        ).toArray(char[][]::new);
        return Tile.create(0, imageGrid);
    }

    private static boolean fitPieces(final Tile[][] image, final int row, final int col, final Set<Tile> remaining) {
        if (remaining.isEmpty()) {
            return true;
        }
        if (row >= image.length) {
            return fitPieces(image, 0, col + 1, remaining);
        }
        for (final var tile : remaining) {
            for (final var oriented : tile.orientations().collect(toList())) {
                if ((row == 0 || image[row - 1][col].bottom().equals(oriented.top())) && (col == 0 || image[row][col - 1].right().equals(oriented.left()))) {
                    image[row][col] = oriented;
                    if (fitPieces(image, row + 1, col, difference(remaining, Set.of(tile)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isMonsterAt(final Tile image, final Tile monster, final int row, final int col) {
        final var imageGrid = image.grid();
        final var monsterLength = monster.grid().length;
        final var monsterHeight = monster.grid()[0].length;
        if ((row + monsterLength >= imageGrid.length) || (col + monsterHeight >= imageGrid[row].length)) {
            return false;
        }
        for (var rowDelta = 0; rowDelta < monsterLength; rowDelta++) {
            for (var colDelta = 0; colDelta < monsterHeight; colDelta++) {
                if (monster.grid()[rowDelta][colDelta] == '#' && imageGrid[row + rowDelta][col + colDelta] != '#') {
                    return false;
                }
            }
        }
        return true;
    }

    static record Tile(long id, char[][] grid, String top, String right, String bottom, String left) {

        static Tile create(final long id, final char[][] grid) {
            var right = "";
            var left = "";
            for (final var row : grid) {
                left += row[0];
                right += row[row.length - 1];
            }
            return new Tile(id, grid, new String(grid[0]), right, new String(grid[grid.length - 1]), left);
        }

        static Tile parse(final String idField, final String... rows) {
            final var id = parseLong(findNext(idField, "(\\d+)"));
            final var grid = new char[rows.length][];
            for (var index = 0; index < grid.length; index++) {
                grid[index] = rows[index].toCharArray();
            }
            return Tile.create(id, grid);
        }

        long count(final char element) {
            return streamPositions(grid).filter((row, col) -> grid[row][col] == element).count();
        }

        Stream<String> sides() {
            return Stream.of(top, right, bottom, left);
        }

        Stream<Tile> orientations() {
            return concat(iterate(this, Tile::rotate90Degrees).limit(4),
                    flipHorizontally(), flipHorizontally().rotate90Degrees(),
                    flipVertically(), flipVertically().rotate90Degrees());
        }

        Tile borderless() {
            return transform(grid.length - 2, (row, col) -> grid[row + 1][col + 1]);
        }

        private Tile rotate90Degrees() {
            return transform(grid.length, (row, col) -> grid[grid.length - col - 1][row]);
        }

        private Tile flipHorizontally() {
            return transform(grid.length, (row, col) -> grid[grid.length - row - 1][col]);
        }

        private Tile flipVertically() {
            return transform(grid.length, (row, col) -> grid[row][grid.length - col - 1]);
        }

        private Tile transform(final int sideLength, final BinaryIntToCharFunction update) {
            final var newGrid = new char[sideLength][sideLength];
            streamPositions(newGrid).forEachOrdered((row, col) -> newGrid[row][col] = update.apply(row, col));
            return Tile.create(id(), newGrid);
        }
    }
}