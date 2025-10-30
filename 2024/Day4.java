static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(String::toCharArray)
                     .toArray(char[][]::new);

    var rowCount = input.length;
    var columnCount = input[0].length;

    var part1Result = IntStream.range(0, rowCount)
                               .map(rowI -> IntStream.range(0, columnCount)
                                                     .map(colI -> countXMASAtPosition(rowI, colI, input))
                                                     .sum())
                               .sum();

    var part2Result = IntStream.range(0, rowCount)
                               .map(rowI -> IntStream.range(0, columnCount)
                                                     .map(colI -> countCrossMASAtPosition(rowI, colI, input))
                                                     .sum())
                               .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int countXMASAtPosition(int rowI, int colI, char[][] input) {
    return getCharAt(rowI, colI, input) != 'X' ? 0 :
              (getCharAt(rowI, colI + 1, input) == 'M' && getCharAt(rowI, colI + 2, input) == 'A' && getCharAt(rowI, colI + 3, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI, colI - 1, input) == 'M' && getCharAt(rowI, colI - 2, input) == 'A' && getCharAt(rowI, colI - 3, input) == 'S' ? 1 : 0) +

              (getCharAt(rowI + 1, colI, input) == 'M' && getCharAt(rowI + 2, colI, input) == 'A' && getCharAt(rowI + 3, colI, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI - 1, colI, input) == 'M' && getCharAt(rowI - 2, colI, input) == 'A' && getCharAt(rowI - 3, colI, input) == 'S' ? 1 : 0) +

              (getCharAt(rowI + 1, colI + 1, input) == 'M' && getCharAt(rowI + 2, colI + 2, input) == 'A' && getCharAt(rowI + 3, colI + 3, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI + 1, colI - 1, input) == 'M' && getCharAt(rowI + 2, colI - 2, input) == 'A' && getCharAt(rowI + 3, colI - 3, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI - 1, colI + 1, input) == 'M' && getCharAt(rowI - 2, colI + 2, input) == 'A' && getCharAt(rowI - 3, colI + 3, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI - 1, colI - 1, input) == 'M' && getCharAt(rowI - 2, colI - 2, input) == 'A' && getCharAt(rowI - 3, colI - 3, input) == 'S' ? 1 : 0);
}

static int countCrossMASAtPosition(int rowI, int colI, char[][] input) {
    return getCharAt(rowI, colI, input) != 'A' ? 0 : (
              (getCharAt(rowI - 1, colI - 1, input) == 'M' && getCharAt(rowI + 1, colI + 1, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI + 1, colI + 1, input) == 'M' && getCharAt(rowI - 1, colI - 1, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI - 1, colI + 1, input) == 'M' && getCharAt(rowI + 1, colI - 1, input) == 'S' ? 1 : 0) +
              (getCharAt(rowI + 1, colI - 1, input) == 'M' && getCharAt(rowI - 1, colI + 1, input) == 'S' ? 1 : 0)
           ) / 2;
}

static char getCharAt(int rowI, int colI, char[][] input) {
    return rowI < 0 || colI < 0 || rowI >= input.length || colI >= input[0].length ? '0' : input[rowI][colI];
}
