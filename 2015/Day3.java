static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var part1Result = getUniqueVisitedPointCount(input, (c, i) -> updatePart1State(c, i));
    var part2Result = getUniqueVisitedPointCount(input, (c, i) -> updatePart2State(c, i));

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int getUniqueVisitedPointCount(String input, BiFunction<ResultState, Integer, ResultState> stateUpdater) {
    return input.chars()
                .boxed()
                .reduce(new ResultState(new Point(0, 0), new Point(0, 0), true, new HashSet<>(List.of(new Point(0, 0)))), stateUpdater, (_, l) -> l)
                .visitedLocations()
                .size();
}

static ResultState updatePart1State(ResultState currentState, int instruction) {
    var newSantaLocation = updateLocation(currentState.santaLocation(), instruction);

    currentState.visitedLocations().add(newSantaLocation);
    return new ResultState(newSantaLocation, null, true, currentState.visitedLocations());
}

static ResultState updatePart2State(ResultState currentState, int instruction) {
    var santaTurn = currentState.santaTurn();
    var newSantaLocation = santaTurn  ? updateLocation(currentState.santaLocation(), instruction) : currentState.santaLocation();
    var newRobotLocation = !santaTurn ? updateLocation(currentState.robotLocation(), instruction) : currentState.robotLocation();

    currentState.visitedLocations().add(newSantaLocation);
    currentState.visitedLocations().add(newRobotLocation);
    return new ResultState(newSantaLocation, newRobotLocation, !santaTurn, currentState.visitedLocations());
}

static Point updateLocation(Point location, int instruction) {
    var newX = location.x() + (instruction == '>' ? 1 : instruction == '<' ? -1 : 0);
    var newY = location.y() + (instruction == '^' ? 1 : instruction == 'v' ? -1 : 0);

    return new Point(newX, newY);
}

record Point(int x, int y) {}
record ResultState(Point santaLocation, Point robotLocation, boolean santaTurn, HashSet<Point> visitedLocations) {}