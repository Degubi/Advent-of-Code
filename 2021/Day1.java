import java.nio.file.*;
import java.util.stream.*;

public class Day1 {


    public static void main(String[] args) throws Exception {
        var input = Files.lines(Path.of("Day1.txt"))
                         .mapToInt(Integer::parseInt)
                         .toArray();

        var part1Result = IntStream.range(0, input.length - 1)
                                   .filter(i -> input[i + 1] > input[i])
                                   .count();

        var sliding3Sums = IntStream.range(0, input.length - 2)
                                    .map(i -> input[i] + input[i + 1] + input[i + 2])
                                    .toArray();

        var part2Result = IntStream.range(0, sliding3Sums.length - 1)
                                   .filter(i -> sliding3Sums[i + 1] > sliding3Sums[i])
                                   .count();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }
}