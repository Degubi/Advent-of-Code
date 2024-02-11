import java.nio.file.*;

public class Day1 {


    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("Day1.txt"));
        var part1Result = input.chars()
                               .map(k -> k == '(' ? 1 : -1)
                               .sum();

        var part2Result = getBasementEnterPosition(0, 0, input);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int getBasementEnterPosition(int i, int sum, String input) {
        return sum == -1 ? i : getBasementEnterPosition(i + 1, sum + (input.charAt(i) == '(' ? 1 : -1), input);
    }
}