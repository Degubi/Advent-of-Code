import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    static final int TILE_WIDTH = 101;
    static final int TILE_HEIGHT = 103;

    record Position(int x, int y) {}
    record Robot(int posY, int posX, int motionY, int motionX) {}

    public static void main(String[] args) throws Exception {
        var robots = Files.lines(Path.of("input.txt"))
                          .map(Main::parseRobot)
                          .toArray(Robot[]::new);

        var part1Result = Arrays.stream(robots)
                                .map(k -> updatePosition(k, 100))
                                .filter(k -> k.posX != TILE_HEIGHT / 2 && k.posY != TILE_WIDTH / 2)
                                .collect(Collectors.groupingBy(k -> new Position(k.posY / (TILE_WIDTH / 2 + 1), k.posX / (TILE_HEIGHT / 2 + 1)), Collectors.counting()))
                                .values().stream()
                                .reduce(1L, (k, l) -> k * l, (k, l) -> l);

        var part2Result = IntStream.range(1, Integer.MAX_VALUE)
                                   .filter(i -> doNumberOfRobotsEqualToTotal(robots, i))
                                   .findFirst()
                                   .orElseThrow();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Robot parseRobot(String line) {
        var spaceIndex = line.indexOf(' ');
        var firstCommaIndex = line.indexOf(',');
        var lastCommaIndex = line.lastIndexOf(',');

        return new Robot(
            Integer.parseInt(line, 2, firstCommaIndex, 10),
            Integer.parseInt(line, firstCommaIndex + 1, spaceIndex, 10),
            Integer.parseInt(line, spaceIndex + 3, lastCommaIndex, 10),
            Integer.parseInt(line, lastCommaIndex + 1, line.length(), 10)
        );
    }

    static Robot updatePosition(Robot robot, int seconds) {
        return new Robot(
            ((robot.posY + robot.motionY * seconds) + TILE_WIDTH * seconds) % TILE_WIDTH,
            ((robot.posX + robot.motionX * seconds) + TILE_HEIGHT * seconds) % TILE_HEIGHT,
            robot.motionY, robot.motionX
        );
    }

    static boolean doNumberOfRobotsEqualToTotal(Robot[] robots, int seconds) {
        return Arrays.stream(robots)
                     .map(k -> updatePosition(k, seconds))
                     .map(k -> new Position(k.posX, k.posY))
                     .distinct()
                     .count() == robots.length;
    }
}
