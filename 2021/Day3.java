import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day3 {
    record GeneratorRatingIterationState(int iterationCount, String[] input) {}


    public static void main(String[] args) throws Exception {
        var input = Files.readAllLines(Path.of("Day3.txt")).toArray(String[]::new);
        var allBitAverages = IntStream.range(0, input[0].length())
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

    static int calculateGeneratorRating(String[] values, DoubleToIntFunction expectedValueCalculator) {
        return Integer.parseInt(doGeneratorRatingIteration(0, values, expectedValueCalculator).input()[0], 2);
    }

    static GeneratorRatingIterationState doGeneratorRatingIteration(int bitIndex, String[] values, DoubleToIntFunction expectedValueCalculator) {
        if(values.length == 1) return new GeneratorRatingIterationState(bitIndex, values);

        var filteredValues = Arrays.stream(values)
                                   .filter(k -> Character.getNumericValue(k.charAt(bitIndex)) ==
                                                expectedValueCalculator.applyAsInt(calculateBitAverage(bitIndex, values)))
                                   .toArray(String[]::new);

        return doGeneratorRatingIteration(bitIndex + 1, filteredValues, expectedValueCalculator);
    }

    static double calculateBitAverage(int index, String[] values) {
        return Arrays.stream(values)
                     .mapToInt(k -> Character.getNumericValue(k.charAt(index)))
                     .average()
                     .orElseThrow();
    }
}