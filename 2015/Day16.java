import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day16 {
    record Sue(int ordinal, Map<String, Integer> presents) {}


    public static void main(String[] args) throws Exception {
        var sues = Files.lines(Path.of("Day16.txt"))
                        .map(k -> k.split(" ", 3))
                        .map(k -> new Sue(Integer.parseInt(k[1], 0, k[1].length() - 1, 10), parsePresents(k[2])))
                        .toArray(Sue[]::new);

        var allPresents = Map.of("children", 3, "cats", 7, "samoyeds", 2, "pomeranians", 3, "akitas", 0, "vizslas", 0, "goldfish", 5, "trees", 3, "cars", 2, "perfumes", 1);

        var part1Result = Arrays.stream(sues)
                                .filter(k -> checkPresents(allPresents, k, p -> Integer::equals))
                                .mapToInt(Sue::ordinal)
                                .findFirst()
                                .orElseThrow();

        var part2Result = Arrays.stream(sues)
                                .filter(k -> checkPresents(allPresents, k, Day16::createPart2ValueTester))
                                .mapToInt(Sue::ordinal)
                                .findFirst()
                                .orElseThrow();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static BiPredicate<Integer, Integer> createPart2ValueTester(String presentName) {
        return switch(presentName) {
            case "cats", "trees"           -> (v1, v2) -> v1 > v2;
            case "pomeranians", "goldfish" -> (v1, v2) -> v1 < v2;
            default                        -> Integer::equals;
        };
    }

    static boolean checkPresents(Map<String, Integer> allPresents, Sue sue, Function<String, BiPredicate<Integer, Integer>> valueTesterSupplier) {
        return allPresents.entrySet().stream()
                          .allMatch(e -> {
                              var presentName = e.getKey();
                              var read = sue.presents().getOrDefault(presentName, -1);

                              return read == -1 || valueTesterSupplier.apply(presentName).test(read, e.getValue());
                          });
    }

    static Map<String, Integer> parsePresents(String presents) {
        return Arrays.stream(presents.split(", "))
                     .map(k -> k.split(": ", 2))
                     .collect(Collectors.toMap(k -> k[0], k -> Integer.parseInt(k[1])));
    }
}
