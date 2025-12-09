static void main() throws Exception {
    var splitterRegex = Pattern.compile("\\s+");
    var inputLines = Files.lines(Path.of("input.txt"))
                          .map(String::trim)
                          .map(splitterRegex::split)
                          .toArray(String[][]::new);

    var operators = inputLines[inputLines.length - 1];

    var columnCount = inputLines[0].length;
    var numbers = Arrays.stream(inputLines, 0, inputLines.length - 1)
                        .map(k -> Arrays.stream(k).mapToInt(Integer::parseInt).toArray())
                        .toArray(int[][]::new);


    var part1Result = IntStream.range(0, columnCount)
                               .mapToLong(c -> calculatePart1ColumnValue(c, numbers, operators[c].charAt(0)))
                               .sum();

    var part2Result = IntStream.range(0, columnCount)
                               .mapToLong(c -> calculatePart2ColumnValue(c, numbers, operators[c].charAt(0)))
                               .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long calculatePart1ColumnValue(int columnIndex, int[][] numbers, char operator) {
    return IntStream.range(0, numbers.length)
                    .mapToLong(i -> numbers[i][columnIndex])
                    .reduce(operator == '+' ? 0 : 1, operator == '+' ? (k, l) -> k + l : (k, l) -> k * l);
}

static long calculatePart2ColumnValue(int columnIndex, int[][] numbers, char operator) {
    var columnValues = IntStream.range(0, numbers.length)
                                .mapToObj(i -> Integer.toString(numbers[i][columnIndex]))
                                .toArray(String[]::new);

    var innerColumnCount = Arrays.stream(columnValues)
                                 .mapToInt(String::length)
                                 .max()
                                 .orElseThrow();

    // FIXME part2

    return 0;
}