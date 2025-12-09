static void main() throws Exception {
    var inputLines = Files.readAllLines(Path.of("input.txt"));
    var valueStringLines = inputLines.subList(0, inputLines.size() - 1);
    var operatorsLine = inputLines.getLast();
    var operatorColumnIndices = IntStream.range(0, operatorsLine.length())
                                         .filter(i -> operatorsLine.charAt(i) != ' ')
                                         .toArray();

    var columnValueStrings = IntStream.range(0, operatorColumnIndices.length)
                                      .mapToObj(c -> valueStringLines.stream()
                                                                     .map(k -> k.substring(operatorColumnIndices[c], c == operatorColumnIndices.length - 1 ? k.length() : operatorColumnIndices[c + 1]))
                                                                     .toArray(String[]::new))
                                      .toArray(String[][]::new);

    var operators = Arrays.stream(operatorColumnIndices)
                          .mapToObj(operatorsLine::charAt)
                          .toArray(Character[]::new);

    var columnCount = operators.length;

    var part1Result = IntStream.range(0, columnCount)
                               .mapToLong(c -> calculateResult(producingPart1Values(columnValueStrings[c]), operators[c]))
                               .sum();

    var part2Result = IntStream.range(0, columnCount)
                               .mapToLong(c -> calculateResult(producingPart2Values(columnValueStrings[c]), operators[c]))
                               .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long calculateResult(LongStream stream, char operator) {
    return stream.reduce(operator == '+' ? 0 : 1, operator == '+' ? (k, l) -> k + l : (k, l) -> k * l);
}

static LongStream producingPart1Values(String[] valueStrings) {
    return Arrays.stream(valueStrings)
                 .map(String::trim)
                 .mapToLong(Long::parseLong);
}

static LongStream producingPart2Values(String[] valueStrings) {
    var valueColumnCount = Arrays.stream(valueStrings)
                                 .mapToInt(String::length)
                                 .max()
                                 .orElseThrow();

    return IntStream.range(0, valueColumnCount)
                    .mapToObj(valueColI -> Arrays.stream(valueStrings)
                                                 .map(k -> k.charAt(valueColI))
                                                 .filter(k -> k != ' ')
                                                 .map(String::valueOf)
                                                 .collect(Collectors.joining()))
                    .filter(k -> !k.isEmpty())
                    .mapToLong(Long::parseLong);
}