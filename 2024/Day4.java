import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) throws Exception {
        var input = Files.lines(Path.of("input.txt"))
                         .map(String::toCharArray)
                         .toArray(char[][]::new);

        var rowCount = input.length;
        var columnCount = input[0].length;

        var part1Result = IntStream.range(0, rowCount)
                                   .map(rowI -> IntStream.range(0, columnCount)
                                                         .map(columnI -> countXMASAtPosition(rowI, columnI, input))
                                                         .sum())
                                   .sum();

        var part2Result = IntStream.range(0, rowCount)
                                   .map(rowI -> IntStream.range(0, columnCount)
                                                         .map(columnI -> countCrossMASAtPosition(rowI, columnI, input))
                                                         .sum())
                                   .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int countXMASAtPosition(int rowI, int columnI, char[][] input) {
        return getCharAt(rowI, columnI, input) != 'X' ? 0 :
                  (getCharAt(rowI, columnI + 1, input) == 'M' && getCharAt(rowI, columnI + 2, input) == 'A' && getCharAt(rowI, columnI + 3, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI, columnI - 1, input) == 'M' && getCharAt(rowI, columnI - 2, input) == 'A' && getCharAt(rowI, columnI - 3, input) == 'S' ? 1 : 0) +

                  (getCharAt(rowI + 1, columnI, input) == 'M' && getCharAt(rowI + 2, columnI, input) == 'A' && getCharAt(rowI + 3, columnI, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI - 1, columnI, input) == 'M' && getCharAt(rowI - 2, columnI, input) == 'A' && getCharAt(rowI - 3, columnI, input) == 'S' ? 1 : 0) +

                  (getCharAt(rowI + 1, columnI + 1, input) == 'M' && getCharAt(rowI + 2, columnI + 2, input) == 'A' && getCharAt(rowI + 3, columnI + 3, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI + 1, columnI - 1, input) == 'M' && getCharAt(rowI + 2, columnI - 2, input) == 'A' && getCharAt(rowI + 3, columnI - 3, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI - 1, columnI + 1, input) == 'M' && getCharAt(rowI - 2, columnI + 2, input) == 'A' && getCharAt(rowI - 3, columnI + 3, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI - 1, columnI - 1, input) == 'M' && getCharAt(rowI - 2, columnI - 2, input) == 'A' && getCharAt(rowI - 3, columnI - 3, input) == 'S' ? 1 : 0);
    }

    static int countCrossMASAtPosition(int rowI, int columnI, char[][] input) {
        return getCharAt(rowI, columnI, input) != 'A' ? 0 : (
                  (getCharAt(rowI - 1, columnI - 1, input) == 'M' && getCharAt(rowI + 1, columnI + 1, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI + 1, columnI + 1, input) == 'M' && getCharAt(rowI - 1, columnI - 1, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI - 1, columnI + 1, input) == 'M' && getCharAt(rowI + 1, columnI - 1, input) == 'S' ? 1 : 0) +
                  (getCharAt(rowI + 1, columnI - 1, input) == 'M' && getCharAt(rowI - 1, columnI + 1, input) == 'S' ? 1 : 0)
               ) / 2;
    }

    static char getCharAt(int rowI, int columnI, char[][] input) {
        var rowCount = input.length;
        var columnCount = input[0].length;

        return rowI < 0 || columnI < 0 || rowI >= rowCount || columnI >= columnCount ? '0' : input[rowI][columnI];
    }
}
