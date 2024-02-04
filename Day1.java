import java.nio.file.*;
import java.util.stream.*;

public class Day1 {

    interface NumericValueExtractor {
        int extract(String line, int currentIndex);
    }


    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of("Day1.txt"));

        var part1Result = lines.stream()
                               .mapToInt(k -> parseTwoDigitNumber(k, Day1::getDigitBasedNumericValue))
                               .sum();

        var part2Result = lines.stream()
                               .mapToInt(k -> parseTwoDigitNumber(k, Day1::getDigitOrTextBasedNumericValue))
                               .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int parseTwoDigitNumber(String line, NumericValueExtractor valueExtractor) {
        var firstValue  = findNumber(line, valueExtractor, IntStream.iterate(0, k -> k < line.length(), k -> k + 1));
        var secondValue = findNumber(line, valueExtractor, IntStream.iterate(line.length() - 1, k -> k >= 0, k -> k -1));

        return firstValue * 10 + secondValue;
    }

    static int findNumber(String line, NumericValueExtractor valueExtractor, IntStream indexGenerator) {
        return indexGenerator.map(i -> valueExtractor.extract(line, i))
                             .filter(k -> k != -1)
                             .findFirst()
                             .orElseThrow();
    }

    static int getDigitBasedNumericValue(String line, int currentIndex) {
        var currentChar = line.charAt(currentIndex);

        return Character.isDigit(currentChar) ? Character.getNumericValue(currentChar) : -1;
    }

    static int getDigitOrTextBasedNumericValue(String line, int currentIndex) {
        var digitBasedNumericValue = getDigitBasedNumericValue(line, currentIndex);

        return digitBasedNumericValue != -1 ? digitBasedNumericValue : findOptionalNumericValueFromString(line, currentIndex);
    }

    static int findOptionalNumericValueFromString(String line, int currentIndex) {
        return line.startsWith("one",   currentIndex) ? 1 :
               line.startsWith("two",   currentIndex) ? 2 :
               line.startsWith("three", currentIndex) ? 3 :
               line.startsWith("four",  currentIndex) ? 4 :
               line.startsWith("five",  currentIndex) ? 5 :
               line.startsWith("six",   currentIndex) ? 6 :
               line.startsWith("seven", currentIndex) ? 7 :
               line.startsWith("eight", currentIndex) ? 8 :
               line.startsWith("nine", currentIndex)  ? 9 : -1;
    }
}