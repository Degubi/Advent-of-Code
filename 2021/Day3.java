static void main() throws Exception {
    var input = Files.readAllLines(Path.of("input.txt"));
    var allBitAverages = IntStream.range(0, input.getFirst().length())
                                  .mapToDouble(i -> calculateBitAverage(i, input))
                                  .toArray();

    var gammaRate = Arrays.stream(allBitAverages)
                          .mapToInt(k -> k >= 0.5 ? 1 : 0)
                          .reduce(0, (result, k) -> (result << 1) | k);

    var epsilonRate = Arrays.stream(allBitAverages)
                            .mapToInt(k -> k >= 0.5 ? 0 : 1)
                            .reduce(0, (result, k) -> (result << 1) | k);

    var o2GeneratorRating  = calculateGeneratorRating(input, average -> average >= 0.5 ? 1 : 0);
    var co2GeneratorRating = calculateGeneratorRating(input, average -> average >= 0.5 ? 0 : 1);

    var part1Result = gammaRate * epsilonRate;
    var part2Result = o2GeneratorRating * co2GeneratorRating;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateGeneratorRating(List<String> values, DoubleToIntFunction expectedValueCalculator) {
    return Integer.parseInt(doGeneratorRatingIteration(0, values, expectedValueCalculator).input().getFirst(), 2);
}

static GeneratorRatingIterationState doGeneratorRatingIteration(int bitIndex, List<String> values, DoubleToIntFunction expectedValueCalculator) {
    if(values.size() == 1) return new GeneratorRatingIterationState(bitIndex, values);

    var filteredValues = values.stream()
                               .filter(k -> Character.getNumericValue(k.charAt(bitIndex)) ==
                                            expectedValueCalculator.applyAsInt(calculateBitAverage(bitIndex, values)))
                               .collect(Collectors.toList());

    return doGeneratorRatingIteration(bitIndex + 1, filteredValues, expectedValueCalculator);
}

static double calculateBitAverage(int index, List<String> values) {
    return values.stream()
                 .mapToInt(k -> Character.getNumericValue(k.charAt(index)))
                 .average()
                 .orElseThrow();
}

record GeneratorRatingIterationState(int iterationCount, List<String> input) {}