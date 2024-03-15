import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day9 {
    record Position(int x, int y) {}


    public static void main(String[] args) throws Exception {
        var input = Files.lines(Path.of("Day9.txt"))
                         .map(String::toCharArray)
                         .toArray(char[][]::new);

        var basinCenters = IntStream.range(0, input.length)
                                    .mapToObj(x -> IntStream.range(0, input[x].length).filter(y -> isBasinCenter(input, x, y)).mapToObj(y -> new Position(x, y)))
                                    .flatMap(k -> k)
                                    .toArray(Position[]::new);

        var part1Result = Arrays.stream(basinCenters)
                                .mapToInt(k -> Character.getNumericValue(input[k.x()][k.y()]) + 1)
                                .sum();

        var basinSizes = Arrays.stream(basinCenters)
                               .mapToInt(k -> getBasinSize(input, k))
                               .sorted()
                               .toArray();

        var part2Result = basinSizes[basinSizes.length - 1] * basinSizes[basinSizes.length - 2] * basinSizes[basinSizes.length - 3];

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int getBasinSize(char[][] input, Position basinCenter) {
        var positionsToVisit = new ArrayList<Position>();
        var visitedPositions = new HashSet<Position>();
        positionsToVisit.add(basinCenter);

        while(!positionsToVisit.isEmpty()) {
            var currentPos = positionsToVisit.removeLast();
            var x = currentPos.x();
            var y = currentPos.y();
            var value = input[x][y];
            var above = new Position(x - 1, y);
            var below = new Position(x + 1, y);
            var left  = new Position(x, y - 1);
            var right = new Position(x, y + 1);

            visitedPositions.add(currentPos);

            if(isBasinNeighbour(input, above, value) && !visitedPositions.contains(above)) positionsToVisit.add(above);
            if(isBasinNeighbour(input, below, value) && !visitedPositions.contains(below)) positionsToVisit.add(below);
            if(isBasinNeighbour(input, left,  value) && !visitedPositions.contains(left))  positionsToVisit.add(left);
            if(isBasinNeighbour(input, right, value) && !visitedPositions.contains(right)) positionsToVisit.add(right);
        }

        return visitedPositions.size();
    }

    static boolean isBasinNeighbour(char[][] input, Position position, int value) {
        var neighbourValue = getNeighbour(input, position.x(), position.y());

        return neighbourValue != '9' && neighbourValue > value;
    }

    static boolean isBasinCenter(char[][] input, int x, int y) {
        var current = input[x][y];

        return getNeighbour(input, x - 1, y) > current && getNeighbour(input, x + 1, y) > current &&
               getNeighbour(input, x, y + 1) > current && getNeighbour(input, x, y - 1) > current;
    }

    static int getNeighbour(char[][] input, int x, int y) {
        return x < 0 || x >= input.length || y < 0 || y >= input[0].length ? '9' : input[x][y];
    }
}