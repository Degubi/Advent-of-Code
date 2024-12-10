import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    record Position(int x, int y) {}
    record GuardState(Position position, int facingAngle) {}
    record State(GuardState guardState, List<GuardState> visitedGuardStates) {}

    public static void main(String[] args) throws Exception {
        var map = Files.lines(Path.of("input.txt"))
                       .map(String::toCharArray)
                       .toArray(char[][]::new);

        var rowCount = map.length;
        var colCount = map[0].length;
        var initialGuardState = IntStream.range(0, rowCount)
                                         .mapToObj(x -> IntStream.range(0, colCount).filter(y -> map[x][y] == '^').boxed().findFirst().map(y -> new Position(x, y)))
                                         .flatMap(Optional::stream)
                                         .findFirst()
                                         .map(k -> new GuardState(k, 0))
                                         .orElseThrow();

        var part1VisitedPositions = Stream.iterate(new State(initialGuardState, null), k -> generateNextState(k, map, rowCount, colCount, -100, -100))
                                          .takeWhile(k -> k.guardState.position.x >= 0 && k.guardState.position.x < rowCount && k.guardState.position.y >= 0 && k.guardState.position.y < colCount)
                                          .map(State::guardState)
                                          .map(GuardState::position)
                                          .distinct()
                                          .collect(Collectors.toList());

        var allModificationPositions = IntStream.range(0, rowCount)
                                                .mapToObj(x -> IntStream.range(0, colCount).filter(y -> map[x][y] != '^').mapToObj(y -> new Position(x, y)))
                                                .flatMap(k -> k)
                                                .filter(k -> part1VisitedPositions.contains(k))
                                                .toArray(Position[]::new);

        var part1Result = part1VisitedPositions.size();
        var part2Result = Arrays.stream(allModificationPositions).parallel()
                                .filter(k -> doesMapModificationCauseLoop(k.x, k.y, initialGuardState, map, rowCount, colCount))
                                .count();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static boolean doesMapModificationCauseLoop(int extraBlockX, int extraBlockY, GuardState initialGuardState, char[][] map, int rowCount, int colCount) {
        return Stream.iterate(new State(initialGuardState, new ArrayList<>()), k -> generateNextState(k, map, rowCount, colCount, extraBlockX, extraBlockY))
                     .takeWhile(k -> k.guardState.position.x >= 0 && k.guardState.position.x < rowCount && k.guardState.position.y >= 0 && k.guardState.position.y < colCount)
                     .anyMatch(k -> {
                         var indexOfCurrentState = k.visitedGuardStates.indexOf(k.guardState);

                         // Use reference equality too so we filter out the current position
                         return indexOfCurrentState != -1 && k.guardState != k.visitedGuardStates.get(indexOfCurrentState);
                     });
    }

    static State generateNextState(State currentState, char[][] map, int rowCount, int colCount, int optionalExtraBlockX, int optionalExtraBlockY) {
        var currentAngle = currentState.guardState.facingAngle;
        var currentPosition = currentState.guardState.position;
        var nextX = currentPosition.x + (currentAngle == 0 ? -1 : currentAngle == 180 ? 1 : 0);
        var nextY = currentPosition.y + (currentAngle == 90 ? 1 : currentAngle == 270 ? -1 : 0);
        var nextPositionIsObstacle = nextX >= 0 && nextX < rowCount && nextY >= 0 && nextY < colCount && (map[nextX][nextY] == '#' || (nextX == optionalExtraBlockX && nextY == optionalExtraBlockY));
        var visitedGuardStates = currentState.visitedGuardStates;
        var nextGuardState = new GuardState(nextPositionIsObstacle ? currentPosition : new Position(nextX, nextY), nextPositionIsObstacle ? (currentAngle + 90) % 360 : currentAngle);

        if(visitedGuardStates != null) {
            visitedGuardStates.add(nextGuardState);
        }

        return new State(nextGuardState, visitedGuardStates);
    }
}
