import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) throws Exception {
        var input = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
                          .map(k -> k.lines().map(String::toCharArray).toArray(char[][]::new))
                          .collect(Collectors.partitioningBy(k -> k[0][0] == '#'));

        var locks = calculateHeights(input.get(Boolean.TRUE));
        var keys = calculateHeights(input.get(Boolean.FALSE));

        var result = Arrays.stream(locks)
                           .mapToInt(k -> (int) Arrays.stream(keys)
                                                      .filter(l -> doesKeyFitLock(k, l))
                                                      .count())
                           .sum();

        System.out.println("Result: " + result);
    }

    static boolean doesKeyFitLock(int[] lock, int[] key) {
        return IntStream.range(0, 5)
                        .allMatch(i -> (lock[i] + key[i]) <= 5);
    }

    static int[][] calculateHeights(List<char[][]> schematics) {
        return schematics.stream()
                         .map(k -> IntStream.range(0, 5)
                                            .map(colI -> (int) IntStream.range(1, 6)
                                                                        .filter(rowI -> k[rowI][colI] == '#')
                                                                        .count())
                                            .toArray())
                         .toArray(int[][]::new);
    }
}
