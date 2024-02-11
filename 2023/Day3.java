import java.nio.file.*;
import java.util.*;
import java.util.Map.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day3 {
    static final Pattern NUMBER_REGEX = Pattern.compile("\\d+");

    record Symbol(char symbol, int rowIndex, int columnIndex) {}
    record Digit(int value, Symbol validator) {}


    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of("Day3.txt"));
        var symbols = IntStream.range(0, lines.size())
                               .mapToObj(i -> extractingSymbolPositions(lines.get(i), i))
                               .flatMap(k -> k)
                               .toArray(Symbol[]::new);

        var columnCount = lines.get(0).length();
        var digits = IntStream.range(0, lines.size())
                              .mapToObj(i -> extractingDigits(lines.get(i), i, symbols, columnCount))
                              .flatMap(k -> k)
                              .toArray(Digit[]::new);

        var part1Result = Arrays.stream(digits)
                                .filter(k -> k.validator() != null)
                                .mapToInt(Digit::value)
                                .sum();

        var part2Result = Arrays.stream(digits)
                                .filter(k -> k.validator() != null && k.validator().symbol() == '*')
                                .collect(Collectors.groupingBy(Digit::validator))
                                .entrySet().stream()
                                .filter(e -> e.getValue().size() == 2)
                                .map(Entry::getValue)
                                .mapToInt(k -> k.get(0).value() * k.get(1).value())
                                .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Stream<Symbol> extractingSymbolPositions(String line, int rowIndex) {
        return IntStream.range(0, line.length())
                        .filter(i -> isCharacterSymbol(line.charAt(i)))
                        .mapToObj(i -> new Symbol(line.charAt(i), rowIndex, i));
    }

    static Stream<Digit> extractingDigits(String line, int rowIndex, Symbol[] symbols, int columnCount) {
        return NUMBER_REGEX.matcher(line).results()
                                         .map(k -> new Digit(Integer.parseInt(k.group()), findSymbolForPosition(rowIndex, k.start(), k.end() - 1, symbols, columnCount)));
    }

    static Symbol findSymbolForPosition(int rowIndex, int columnStartIndex, int columnEndIndex, Symbol[] symbols, int columnCount) {
        return Arrays.stream(symbols)
                     .filter(s -> doesSymbolValidatePosition(rowIndex, columnStartIndex, columnEndIndex, s, columnCount))
                     .findFirst()
                     .orElse(null);
    }

    static boolean isCharacterSymbol(char character) {
        return !Character.isDigit(character) && character != '.';
    }

    static boolean doesSymbolValidatePosition(int rowIndex, int columnStartIndex, int columnEndIndex, Symbol symbolPosition, int columnCount) {
        return Math.abs(rowIndex - symbolPosition.rowIndex()) <= 1 &&
               symbolPosition.columnIndex() >= columnStartIndex - 1 &&
               symbolPosition.columnIndex() <= columnEndIndex + 1;
    }
}