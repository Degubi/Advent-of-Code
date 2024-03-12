import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day7 {


    public static void main(String[] args) throws Exception {
        var positions = Arrays.stream(Files.readString(Path.of("Day7.txt")).split(","))
                              .mapToInt(Integer::parseInt)
                              .toArray();

        var positionStats = Arrays.stream(positions).summaryStatistics();
        var part1Result = findFuelMin(positions, positionStats, k -> k);
        var part2Result = findFuelMin(positions, positionStats, k -> k * (k + 1) / 2);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int findFuelMin(int[] positions, IntSummaryStatistics positionStats, IntUnaryOperator moveCountToCostCalculator) {
        return IntStream.rangeClosed(positionStats.getMin(), positionStats.getMax())
                        .map(k -> Arrays.stream(positions).map(l -> moveCountToCostCalculator.applyAsInt(Math.abs(l - k))).sum())
                        .min()
                        .orElseThrow();
    }
}