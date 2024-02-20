import java.nio.file.*;
import java.util.*;

public class Day2 {
    record Round(char opponents, char mine) {}


    public static void main(String[] args) throws Exception {
        var rounds = Files.lines(Path.of("Day2.txt"))
                          .map(k -> new Round(k.charAt(0), k.charAt(2)))
                          .toArray(Round[]::new);

        var part1Result = Arrays.stream(rounds)
                                .mapToInt(Day2::calculateRoundScore)
                                .sum();

        var part2Result = Arrays.stream(rounds)
                                .map(Day2::selectMineCorrectly)
                                .mapToInt(Day2::calculateRoundScore)
                                .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int calculateRoundScore(Round round) {
        var selectionScore = round.mine() - 'W';
        var scoreMultiplier = getWinningSelectionFor(round.opponents()) == round.mine() ? 2
                            : getTyingSelectionFor(round.opponents()) == round.mine() ? 1
                            : 0;

        return selectionScore + scoreMultiplier * 3;
    }

    static Round selectMineCorrectly(Round round) {
        var opponents = round.opponents();
        var selection = switch(round.mine()) {
            case 'X' -> getLosingSelectionFor(opponents);
            case 'Y' -> getTyingSelectionFor(opponents);
            case 'Z' -> getWinningSelectionFor(opponents);
            default  -> 0;
        };

        return new Round(opponents, selection);
    }

    static char getWinningSelectionFor(char opponents) {
        return switch(opponents) {
            case 'A' -> 'Y';
            case 'B' -> 'Z';
            case 'C' -> 'X';
            default  -> 0;
        };
    }

    static char getTyingSelectionFor(char opponents) {
        return switch(opponents) {
            case 'A' -> 'X';
            case 'B' -> 'Y';
            case 'C' -> 'Z';
            default  -> 0;
        };
    }

    static char getLosingSelectionFor(char opponents) {
        return switch(opponents) {
            case 'A' -> 'Z';
            case 'B' -> 'X';
            case 'C' -> 'Y';
            default  -> 0;
        };
    }
}