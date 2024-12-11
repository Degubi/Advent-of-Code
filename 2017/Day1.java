import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("input.txt"));
        var paddedInput = input + input;
        var part1Result = solve(input, paddedInput, 1);
        var part2Result = solve(input, paddedInput, input.length() / 2);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int solve(String input, String paddedInput, int checkedCharOffset) {
        return IntStream.range(0, input.length())
                        .filter(i -> paddedInput.charAt(i) == paddedInput.charAt(i + checkedCharOffset))
                        .map(i -> Character.getNumericValue(input.charAt(i)))
                        .sum();
    }
}
