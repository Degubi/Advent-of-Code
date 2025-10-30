static void main() throws Exception {
    var contraption = Files.lines(Path.of("input.txt"))
                           .map(String::toCharArray)
                           .toArray(char[][]::new);

    var part1Result = simulateBeam(0, 0, Direction.RIGHT, contraption);

    var topRowSimulations = IntStream.range(0, contraption[0].length)
                                     .mapToLong(x -> simulateBeam(x, 0, Direction.DOWN, contraption));

    var bottomRowSimulations = IntStream.range(0, contraption[0].length)
                                        .mapToLong(x -> simulateBeam(x, contraption.length - 1, Direction.UP, contraption));

    var leftColumnSimulations = IntStream.range(0, contraption.length)
                                         .mapToLong(y -> simulateBeam(0, y, Direction.RIGHT, contraption));

    var rightColumnSimulations = IntStream.range(0, contraption.length)
                                          .mapToLong(y -> simulateBeam(contraption[0].length - 1, y, Direction.LEFT, contraption));

    var part2Result = Stream.of(topRowSimulations, bottomRowSimulations, leftColumnSimulations, rightColumnSimulations)
                            .reduce(LongStream::concat)
                            .orElseThrow().parallel()
                            .max()
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long simulateBeam(int x, int y, Direction direction, char[][] contraption) {
    var visitedStates = new ArrayList<BeamState>();
    var stateBeforeStartingState = new BeamState(new Vector2(x - direction.motionX, y - direction.motionY), direction);

    return Stream.iterate(updateBeamState(stateBeforeStartingState, contraption, visitedStates).toArray(BeamState[]::new),
                          k -> Arrays.stream(k).flatMap(l -> updateBeamState(l, contraption, visitedStates)).toArray(BeamState[]::new))
                 .takeWhile(k -> k.length > 0)
                 .flatMap(Arrays::stream)
                 .map(BeamState::position)
                 .distinct()
                 .count();
}

static Stream<BeamState> updateBeamState(BeamState currentState, char[][] contraption, ArrayList<BeamState> visitedStates) {
    var currentPosition = currentState.position();
    var currentDirection = currentState.direction();
    var nextPosition = new Vector2(currentPosition.x() + currentDirection.motionX, currentPosition.y() + currentDirection.motionY);

    if(nextPosition.x() < 0 || nextPosition.x() >= contraption[0].length || nextPosition.y() < 0 || nextPosition.y() >= contraption.length) {
        return Stream.empty();
    };

    var nextStateDirections = switch(contraption[nextPosition.y()][nextPosition.x()]) {
        case '.'  -> Stream.of(currentDirection);
        case '|'  -> handleVerticalSplitter(currentDirection);
        case '-'  -> handleHorizontalSplitter(currentDirection);
        case '/'  -> handleLeftRightMirror(currentDirection);
        case '\\' -> handleRightLeftMirror(currentDirection);
        default   -> null;
    };

    return nextStateDirections.map(k -> new BeamState(nextPosition, k))
                              .filter(k -> !visitedStates.contains(k))
                              .peek(visitedStates::add);
}

static Stream<Direction> handleVerticalSplitter(Direction currentMotion) {
    return switch(currentMotion) {
        case UP, DOWN    -> Stream.of(currentMotion);
        case LEFT, RIGHT -> Stream.of(Direction.UP, Direction.DOWN);
    };
}

static Stream<Direction> handleHorizontalSplitter(Direction currentMotion) {
    return switch(currentMotion) {
        case LEFT, RIGHT -> Stream.of(currentMotion);
        case UP, DOWN    -> Stream.of(Direction.LEFT, Direction.RIGHT);
    };
}

static Stream<Direction> handleLeftRightMirror(Direction currentMotion) {
    return switch(currentMotion) {
        case RIGHT -> Stream.of(Direction.UP);
        case LEFT  -> Stream.of(Direction.DOWN);
        case UP    -> Stream.of(Direction.RIGHT);
        case DOWN  -> Stream.of(Direction.LEFT);
    };
}

static Stream<Direction> handleRightLeftMirror(Direction currentMotion) {
    return switch(currentMotion) {
        case RIGHT -> Stream.of(Direction.DOWN);
        case LEFT  -> Stream.of(Direction.UP);
        case UP    -> Stream.of(Direction.LEFT);
        case DOWN  -> Stream.of(Direction.RIGHT);
    };
}

record Vector2(int x, int y) {}
record BeamState(Vector2 position, Direction direction) {}

enum Direction {
    RIGHT(1, 0),
    LEFT(-1, 0),
    UP(0, -1),
    DOWN(0, 1);

    final int motionX;
    final int motionY;

    private Direction(int motionX, int motionY) {
        this.motionX = motionX;
        this.motionY = motionY;
    }
}