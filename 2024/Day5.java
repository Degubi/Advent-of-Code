import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    record Rule(int left, int right) {}

    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("input.txt"));
        var ruleUpdateSeparator = input.indexOf("\n\n");

        var rules = Arrays.stream(input.substring(0, ruleUpdateSeparator).split("\n"))
                          .map(k -> new Rule(Integer.parseInt(k, 0, 2, 10), Integer.parseInt(k, 3, 5, 10)))
                          .toArray(Rule[]::new);

        var correctIncorrectUpdatesPartitioned = Arrays.stream(input.substring(ruleUpdateSeparator + 2).split("\n"))
                                                       .map(k -> Arrays.stream(k.split(",")).mapToInt(Integer::parseInt).toArray())
                                                       .collect(Collectors.partitioningBy(k -> isUpdateValid(k, rules)));

        var part1Result = correctIncorrectUpdatesPartitioned.get(true).stream()
                                                            .mapToInt(k -> k[k.length / 2])
                                                            .sum();

        var part2Result = correctIncorrectUpdatesPartitioned.get(false).stream()
                                                            .map(k -> fixUpdate(k, rules))
                                                            .mapToInt(k -> k[k.length / 2])
                                                            .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static boolean isUpdateValid(int[] update, Rule[] rules) {
        return IntStream.range(0, update.length - 1)
                        .allMatch(i -> findRule(update[i], update[i + 1], rules).isPresent());
    }

    static int[] fixUpdate(int[] update, Rule[] rules) {
        return Arrays.stream(update)
                     .boxed()
                     .sorted((v1, v2) -> findRule(v1, v2, rules).isPresent() ? 1 : findRule(v2, v1, rules).isPresent() ? -1 : 0)
                     .mapToInt(Integer::intValue)
                     .toArray();
    }

    static Optional<Rule> findRule(int leftValue, int rightValue, Rule[] rules) {
        return Arrays.stream(rules)
                     .filter(k -> k.left == leftValue && k.right == rightValue)
                     .findFirst();
    }
}
