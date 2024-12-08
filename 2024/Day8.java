import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    record Position(int x, int y) {}

    public static void main(String[] args) throws Exception {
        var map = Files.lines(Path.of("input.txt"))
                       .map(String::toCharArray)
                       .toArray(char[][]::new);

        var rowCount = map.length;
        var colCount = map[0].length;
        var antennas = IntStream.range(0, rowCount)
                                .mapToObj(x -> IntStream.range(0, colCount)
                                                        .filter(y -> map[x][y] != '.')
                                                        .mapToObj(y -> new Position(x, y)))
                                .flatMap(k -> k)
                                .collect(Collectors.groupingBy(k -> map[k.x][k.y], Collectors.mapping(k -> k, Collectors.toList())));

        var part1Result = antennas.entrySet().stream()
                                  .flatMap(e -> generateAntinodePositions(1, false, e.getValue(), rowCount, colCount))
                                  .distinct()
                                  .count();

        var part2Result = antennas.entrySet().stream()
                                  .flatMap(e -> generateAntinodePositions(100, true, e.getValue(), rowCount, colCount))
                                  .distinct()
                                  .count();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Stream<Position> generateAntinodePositions(int limit, boolean countAntennaAsAntinode, List<Position> antennaPositions, int rowCount, int colCount) {
        return IntStream.range(0, antennaPositions.size())
                        .mapToObj(i -> antennaPositions.subList(i + 1, antennaPositions.size()).stream()
                                                       .flatMap(k -> generateAntinodePositions(antennaPositions.get(i), k, limit, countAntennaAsAntinode)))
                        .flatMap(k -> k)
                        .filter(k -> k.x >= 0 && k.y >= 0 && k.x < rowCount && k.y < colCount);
    }

    static Stream<Position> generateAntinodePositions(Position pos1, Position pos2, int limit, boolean countAntennaAsAntinode) {
        var xOffset = pos1.x - pos2.x;
        var yOffset = pos1.y - pos2.y;

        return IntStream.range(1, limit + 1)
                        .mapToObj(i -> Stream.of(
                            new Position(pos1.x + xOffset * i, pos1.y + yOffset * i), new Position(pos1.x - xOffset * i, pos1.y - yOffset * i),
                            new Position(pos2.x + xOffset * i, pos2.y + yOffset * i), new Position(pos2.x - xOffset * i, pos2.y - yOffset * i))
                        )
                        .flatMap(k -> k)
                        .filter(countAntennaAsAntinode ? _ -> true : k -> !k.equals(pos1) && !k.equals(pos2));
    }
}
