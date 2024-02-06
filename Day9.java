import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day9 {

    public static void main(String[] args) throws Exception {
        var inputNumbers = Files.lines(Path.of("Day9.txt"))
                                .map(k -> Arrays.stream(k.split(" ")).mapToInt(Integer::parseInt).toArray())
                                .toArray(int[][]::new);

        var part1Result = Arrays.stream(inputNumbers)
                                .mapToInt(Day9::calculateNextSequenceValue)
                                .sum();

        var part2Result = Arrays.stream(inputNumbers)
                                .mapToInt(Day9::calculatePreviousSequenceValue)
                                .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int calculatePreviousSequenceValue(int[] sequence) {
        var differences = Stream.iterate(sequence, Day9::contineGeneratingDifferences, Day9::generateDifferences)
                                .toArray(int[][]::new);

        return IntStream.range(0, differences.length)
                        .map(i -> i % 2 == 0 ? differences[i][0] : -differences[i][0])
                        .sum();
    }

    static int calculateNextSequenceValue(int[] sequence) {
        return Stream.iterate(sequence, Day9::contineGeneratingDifferences, Day9::generateDifferences)
                     .mapToInt(k -> k[k.length - 1])
                     .sum();
    }

    static boolean contineGeneratingDifferences(int[] sequence) {
        return !Arrays.stream(sequence).allMatch(k -> k == 0);
    }

    static int[] generateDifferences(int[] sequence) {
        return IntStream.range(1, sequence.length)
                        .map(i -> sequence[i] - sequence[i - 1])
                        .toArray();
    }
}