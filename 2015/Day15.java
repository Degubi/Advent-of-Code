import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day15 {
    static final Pattern INGREDIENT_REGEX = Pattern.compile("capacity (-?\\d), durability (-?\\d), flavor (-?\\d), texture (-?\\d), calories (-?\\d)");


    public static void main(String[] args) throws Exception {
        var ingredientParts = Files.lines(Path.of("Day15.txt"))
                                   .map(INGREDIENT_REGEX::matcher)
                                   .peek(Matcher::find)
                                   .map(k -> IntStream.rangeClosed(1, 5).map(i -> Integer.parseInt(k.group(i))).toArray())
                                   .toArray(int[][]::new);

        var part1Result = calculateScore(ingredientParts, 100, mixture -> true);
        var part2Result = calculateScore(ingredientParts, 100, mixture -> calculatePartScore(mixture, 4, ingredientParts) == 500);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int calculateScore(int[][] ingredientParts, int totalAmount, Predicate<List<Integer>> mixturePrecondition) {
        return Day15.generateMixtures(ingredientParts.length, totalAmount)
                    .filter(mixturePrecondition)
                    .mapToInt(k -> calculateMixtureScore(k, ingredientParts))
                    .max()
                    .orElseThrow();
    }

    static Stream<List<Integer>> generateMixtures(int ingredientCount, int totalAmount) {
        return IntStream.rangeClosed(ingredientCount == 1 ? totalAmount : 0, totalAmount)
                        .mapToObj(i -> generateMixturePart(i, ingredientCount, totalAmount))
                        .flatMap(k -> k);
    }

    static Stream<List<Integer>> generateMixturePart(int currentAmount, int ingredientCount, int totalAmount) {
        return ingredientCount == 1 ? Stream.of(List.of(currentAmount))
                                    : Day15.generateMixtures(ingredientCount - 1, totalAmount - currentAmount)
                                           .map(list -> {
                                               var newList = new ArrayList<Integer>(list);
                                               newList.add(0, currentAmount);
                                               return newList;
                                           });
    }

    static int calculateMixtureScore(List<Integer> mixture, int[][] ingredientParts) {
        return IntStream.rangeClosed(0, 3)
                        .map(i -> calculatePartScore(mixture, i, ingredientParts))
                        .reduce(1, (result, k) -> result * k);
    }

    static int calculatePartScore(List<Integer> mixture, int partIndex, int[][] ingredientParts) {
        var partScore = IntStream.range(0, mixture.size())
                                 .map(i -> mixture.get(i) * ingredientParts[i][partIndex])
                                 .sum();

        return Math.max(0, partScore);
    }
}