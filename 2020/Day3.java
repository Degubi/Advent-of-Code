static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(String::toCharArray)
                     .toArray(char[][]::new);

    var part1Result = countTreesForSlope(1, 3, input);
    var part2Result = countTreesForSlope(1, 1, input) * part1Result * countTreesForSlope(1, 5, input) * countTreesForSlope(1, 7, input) * countTreesForSlope(2, 1, input);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long countTreesForSlope(int row, int column, char[][] input) {
    var rowCount = input.length;
    var columnCount = input[0].length;

    return Stream.iterate(new Position(row, column), k -> new Position(k.rowI() + row, k.colI() + column))
                 .takeWhile(k -> k.rowI() < rowCount)
                 .filter(k -> input[k.rowI()][k.colI() % columnCount] == '#')
                 .count();
}

record Position(int rowI, int colI) {}