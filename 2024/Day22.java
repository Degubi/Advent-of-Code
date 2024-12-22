import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) throws Exception {
        var input = Files.lines(Path.of("input.txt"))
                         .mapToLong(Long::parseLong)
                         .toArray();

        var part1Result = Arrays.stream(input)
                                .map(k -> LongStream.iterate(k, Main::calculateNextValue).skip(2000).findFirst().orElseThrow())
                                .sum();

        System.out.println("Result 1: " + part1Result); // FIXME: Part 2
    }

    static long calculateNextValue(long value) {
        var step1 = ((value * 64) ^ value) % 16777216;
        var step2 = ((step1 / 32) ^ step1) % 16777216;

        return ((step2 * 2048) ^ step2) % 16777216;
    }
}
