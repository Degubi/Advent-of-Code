import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day1 {


    public static void main(String[] args) throws Exception {
        var input = Files.lines(Path.of("Day1.txt"))
                         .mapToInt(Integer::parseInt)
                         .toArray();

        var part1Result = Arrays.stream(input)
                                .map(Day1::calculateFuel)
                                .sum();

        var part2Result = Arrays.stream(input)
                                .map(Day1::sumFuelValues)
                                .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int calculateFuel(int mass) { return mass / 3 - 2; }

    static int sumFuelValues(int mass) {
        return IntStream.iterate(mass, Day1::calculateFuel)
                        .skip(1)
                        .takeWhile(k -> k > 0)
                        .sum();
    }
}