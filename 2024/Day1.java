static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(k -> k.split("   ", 2))
                     .toArray(String[][]::new);

    var firstColumnNumbers = parseInputColumn(input, 0);
    var secondColumnNumbers = parseInputColumn(input, 1);

    var secondColumnNumberFrequencies = Arrays.stream(secondColumnNumbers)
                                              .boxed()
                                              .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

    var part1Result = IntStream.range(0, input.length)
                               .map(i -> Math.abs(firstColumnNumbers[i] - secondColumnNumbers[i]))
                               .sum();

    var part2Result = Arrays.stream(firstColumnNumbers)
                            .map(k -> k * secondColumnNumberFrequencies.getOrDefault(k, 0L).intValue())
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int[] parseInputColumn(String[][] input, int columnI) {
    return Arrays.stream(input)
                 .mapToInt(k -> Integer.parseInt(k[columnI]))
                 .sorted()
                 .toArray();
}
