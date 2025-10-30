static final Pattern NUMBER_REGEX = Pattern.compile("\\d+");

static void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    var durations = parseNumbersFromLine(lines.get(0));
    var distances = parseNumbersFromLine(lines.get(1));

    var part1Result = IntStream.range(0, durations.length)
                               .mapToLong(i -> calculateNumberOfWays(durations[i], distances[i]))
                               .reduce(1, (k, l) -> k * l);

    var part2Duration = Long.parseLong(lines.get(0).substring("Time:".length()).replace(" ", ""));
    var part2Distance = Long.parseLong(lines.get(1).substring("Distance:".length()).replace(" ", ""));
    var part2Result = calculateNumberOfWays(part2Duration, part2Distance);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long calculateNumberOfWays(long raceDuration, long raceDistance) {
    return LongStream.range(1, raceDuration)
                     .filter(k -> calculateDistance(k, raceDuration) > raceDistance)
                     .count();
}

static long calculateDistance(long secondsWaited, long raceDuration) {
    return (raceDuration - secondsWaited) * secondsWaited;
}

static long[] parseNumbersFromLine(String line) {
    return NUMBER_REGEX.matcher(line).results()
                                     .map(MatchResult::group)
                                     .mapToLong(Long::parseLong)
                                     .toArray();
}

record Race(int duration, int distance) {}