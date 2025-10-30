static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(k -> k.chars().toArray())
                     .toArray(int[][]::new);

    var part1Result = Stream.iterate(input, k -> generateNextIteration(k, (i, j, s) -> getLampStateBasedOnNeighbours(i, j, s)))
                            .skip(100)
                            .findFirst()
                            .map(k -> countActiveLamps(k))
                            .orElseThrow();

    var part2Result = Stream.iterate(input, k -> generateNextIteration(k, (i, j, s) -> getLampStateWithCornersAlwaysOn(i, j, s)))
                            .skip(100)
                            .findFirst()
                            .map(k -> countActiveLamps(k))
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int countActiveLamps(int[][] currentStates) {
    return (int) Arrays.stream(currentStates)
                       .flatMapToInt(Arrays::stream)
                       .filter(k -> k == '#')
                       .count();
}

static int[][] generateNextIteration(int[][] currentStates, LampStateCalculator stateCalculator) {
    return IntStream.range(0, currentStates.length)
                    .mapToObj(i -> IntStream.range(0, currentStates.length)
                                            .map(j -> stateCalculator.get(i, j, currentStates))
                                            .toArray())
                    .toArray(int[][]::new);
}

static int getLampStateBasedOnNeighbours(int i, int j, int[][] currentStates) {
    var currentState = currentStates[i][j];
    var farIndex = currentStates.length - 1;
    var activeNeighbourCount = IntStream.rangeClosed(Math.max(0, i - 1), Math.min(farIndex, i + 1))
                                        .mapToLong(ii -> IntStream.rangeClosed(Math.max(0, j - 1), Math.min(farIndex, j + 1))
                                                                  .filter(jj -> currentStates[ii][jj] == '#' )
                                                                  .count())
                                        .sum();

    var activeNeighbourCountWithoutSelf = activeNeighbourCount - (currentState == '#' ? 1 : 0);

    return currentState == '#' ? (activeNeighbourCountWithoutSelf == 2 || activeNeighbourCountWithoutSelf == 3 ? '#' : '.')
                               : (activeNeighbourCountWithoutSelf == 3 ? '#' : '.');
}

static int getLampStateWithCornersAlwaysOn(int i, int j, int[][] currentStates) {
    var farIndex = currentStates.length - 1;

    return (i == 0 && j == 0) || (i == 0 && j == farIndex) ||
           (i == farIndex && j == 0) || (i == farIndex && j == farIndex) ? '#'
         : getLampStateBasedOnNeighbours(i, j, currentStates);
}

@FunctionalInterface
interface LampStateCalculator {
    int get(int i, int j, int[][] state);
}