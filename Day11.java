import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day11 {
    record Position(int x, int y) {}


    public static void main(String[] args) throws Exception {
        var grid = Files.lines(Path.of("Day11.txt"))
                        .map(String::toCharArray)
                        .toArray(char[][]::new);

        var rowCount = grid.length;
        var columnCount = grid[0].length;

        var galaxyPositions = IntStream.range(0, rowCount)
                                       .mapToObj(x -> IntStream.range(0, columnCount).mapToObj(y -> new Position(x, y)))
                                       .flatMap(k -> k)
                                       .filter(k -> grid[k.x()][k.y()] == '#')
                                       .toArray(Position[]::new);

        var part1Result = Arrays.stream(galaxyPositions).parallel()
                                .mapToLong(k -> sumDistancesOfPosCombinations(k, galaxyPositions, 2, grid, columnCount))
                                .sum() / 2;

        var part2Result = Arrays.stream(galaxyPositions).parallel()
                                .mapToLong(k -> sumDistancesOfPosCombinations(k, galaxyPositions, 1000000, grid, columnCount))
                                .sum() / 2;

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static long sumDistancesOfPosCombinations(Position pos1, Position[] positions, int distanceMultiplier, char[][] grid, int columnCount) {
        return Arrays.stream(positions)
                     .mapToLong(pos2 -> calculateDistance(pos1, pos2, distanceMultiplier, grid, columnCount))
                     .sum();
    }

    static int calculateDistance(Position pos1, Position pos2, int emptySpaceExpansion, char[][] grid, int columnCount) {
        var emptySpaceMultiplier = emptySpaceExpansion - 1;

        return Math.abs(pos1.x() - pos2.x()) + Math.abs(pos1.y() - pos2.y()) +
               countEmptyRowsBetween(pos1.x(), pos2.x(), grid, columnCount) * emptySpaceMultiplier +
               countEmptyColumnsBetween(pos1.y(), pos2.y(), grid) * emptySpaceMultiplier;
    }

    static int countEmptyRowsBetween(int x1, int x2, char[][] grid, int columnCount) {
        return (int) Arrays.stream(grid, Math.min(x1, x2), Math.max(x1, x2))
                           .filter(row -> IntStream.range(0, columnCount).allMatch(i -> row[i] == '.'))
                           .count();
    }

    static int countEmptyColumnsBetween(int y1, int y2, char[][] grid) {
        return (int) IntStream.range(Math.min(y1, y2), Math.max(y1, y2))
                              .filter(y -> Arrays.stream(grid).allMatch(k -> k[y] == '.'))
                              .count();
    }
}
