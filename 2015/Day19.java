import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day19 {
    record MedicineState(String medicine, int modificationCount) {}
    record PatternToReplacement(Pattern original, String replacement) {}


    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of("Day19.txt"));
        var mappingLines = lines.subList(0, lines.size() - 2).stream()
                                .map(k -> k.split(" ", 3))
                                .toArray(String[][]::new);

        var part1Mappings = Arrays.stream(mappingLines).collect(Collectors.groupingBy(k -> Pattern.compile(k[0]), Collectors.mapping(k -> k[2], Collectors.toList())));
        var medicine = lines.getLast();

        var part1Result = part1Mappings.entrySet().stream()
                                       .flatMap(k -> generateRemappings(medicine, k.getKey(), k.getValue()))
                                       .distinct()
                                       .count();

        var part2Mappings = Arrays.stream(mappingLines)
                                  .map(k -> new PatternToReplacement(Pattern.compile(k[2]), k[0]))
                                  .sorted(Comparator.comparingInt((PatternToReplacement k) -> k.original().pattern().length()).reversed())
                                  .toArray(PatternToReplacement[]::new);

        var part2Result = Stream.iterate(new MedicineState(medicine, 0), k -> iterateMedicineReplacements(k, part2Mappings))
                                .dropWhile(k -> !k.medicine().equals("e"))
                                .findFirst()
                                .orElseThrow()
                                .modificationCount();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Stream<String> generateRemappings(String medicine, Pattern originalPattern, List<String> replacements) {
        return originalPattern.matcher(medicine).results()
                              .mapToInt(MatchResult::start)
                              .mapToObj(k -> replacements.stream()
                                                         .map(r -> new StringBuilder(medicine).replace(k, k + originalPattern.pattern().length(), r).toString()))
                              .flatMap(k -> k);
    }

    static MedicineState iterateMedicineReplacements(MedicineState state, PatternToReplacement[] mappings) {
        return Arrays.stream(mappings)
                     .reduce(state, (result, k) -> doReplacementIteration(k.original(), k.replacement(), result), (k, l) -> l);
    }

    static MedicineState doReplacementIteration(Pattern originalPattern, String replacement, MedicineState state) {
        var originalLength = originalPattern.pattern().length();
        var matchIndices = originalPattern.matcher(state.medicine()).results()
                                          .mapToInt(MatchResult::start)
                                          .map(k -> -k)
                                          .sorted()
                                          .map(k -> -k)
                                          .toArray();

        var newMedicine = Arrays.stream(matchIndices)
                                .boxed()
                                .reduce(new StringBuilder(state.medicine()), (result, i) -> result.replace(i, i + originalLength, replacement), (k, l) -> l);

        return new MedicineState(newMedicine.toString(), state.modificationCount() + matchIndices.length);
    }
}