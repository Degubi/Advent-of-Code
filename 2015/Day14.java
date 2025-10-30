static void main() throws Exception {
    var reindeers = Files.lines(Path.of("input.txt"))
                         .map(k -> k.split(" "))
                         .map(k -> new Reindeer(k[0], Integer.parseInt(k[3]), Integer.parseInt(k[6]), Integer.parseInt(k[13])))
                         .toArray(Reindeer[]::new);

    var part1Result = Arrays.stream(reindeers)
                            .mapToInt(k -> calculateTraveledRoad(2503, k))
                            .max()
                            .orElseThrow();

    var part2Result = Stream.iterate(createDefaultStates(reindeers), k -> updateTravelStates(k))
                            .limit(2503)
                            .map(k -> findCurrentLeadingDeerNames(k))
                            .flatMap(k -> k)
                            .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                            .values().stream()
                            .mapToLong(k -> k)
                            .max()
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateTraveledRoad(int time, Reindeer deer) {
    var moveCount = time / (deer.moveSeconds() + deer.sleepSeconds()) + 1;

    return moveCount * deer.kmPerSeconds() * deer.moveSeconds();
}

static Stream<String> findCurrentLeadingDeerNames(ReindeerTravelState[] states) {
    var maxTraveledDistance = Arrays.stream(states)
                                    .max(Comparator.comparingInt(ReindeerTravelState::distanceTraveled))
                                    .orElseThrow()
                                    .distanceTraveled();
    return Arrays.stream(states)
                 .filter(k -> k.distanceTraveled() == maxTraveledDistance)
                 .map(ReindeerTravelState::deer)
                 .map(Reindeer::name);
}

static ReindeerTravelState[] createDefaultStates(Reindeer[] reindeers) {
    return Arrays.stream(reindeers)
                 .map(k -> new ReindeerTravelState(k, TravelState.TRAVELING, k.moveSeconds() - 1, k.kmPerSeconds()))
                 .toArray(ReindeerTravelState[]::new);
}

static ReindeerTravelState[] updateTravelStates(ReindeerTravelState[] previousStates) {
    return Arrays.stream(previousStates)
                 .map(k -> updateTravelState(k))
                 .toArray(ReindeerTravelState[]::new);
}

static ReindeerTravelState updateTravelState(ReindeerTravelState previouState) {
    var deer = previouState.deer();
    var distanceTraveled = previouState.distanceTraveled();
    var timerSeconds = previouState.timerSeconds();

    return switch(previouState.state()) {
        case TRAVELING -> timerSeconds > 0 ? new ReindeerTravelState(deer, TravelState.TRAVELING, timerSeconds - 1, distanceTraveled + deer.kmPerSeconds())
                                           : new ReindeerTravelState(deer, TravelState.SLEEPING, deer.sleepSeconds() - 1, distanceTraveled);
        case SLEEPING  -> timerSeconds > 0 ? new ReindeerTravelState(deer, TravelState.SLEEPING, timerSeconds - 1, distanceTraveled)
                                           : new ReindeerTravelState(deer, TravelState.TRAVELING, deer.moveSeconds() - 1, distanceTraveled + deer.kmPerSeconds());
    };
}

record Reindeer(String name, int kmPerSeconds, int moveSeconds, int sleepSeconds) {}
record ReindeerTravelState(Reindeer deer, TravelState state, int timerSeconds, int distanceTraveled) {}
enum TravelState { TRAVELING, SLEEPING }