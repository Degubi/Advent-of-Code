static void main() throws Exception {
    var whitespaceRegex = Pattern.compile("\\s");
    var rows = Files.lines(Path.of("input.txt"))
                    .map(k -> Arrays.stream(whitespaceRegex.split(k)).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);

    var part1Result = Arrays.stream(rows)
                            .map(k -> Arrays.stream(k).summaryStatistics())
                            .mapToInt(k -> k.getMax() - k.getMin())
                            .sum();

    var part2Result = Arrays.stream(rows)
                            .mapToInt(k -> findEvenlyDivisableResult(k))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int findEvenlyDivisableResult(int[] input) {
    return Arrays.stream(input)
                 .map(k -> Arrays.stream(input)
                                 .filter(l -> k != l && k % l == 0)
                                 .map(l -> k / l)
                                 .findFirst()
                                 .orElse(-1))
                 .filter(k -> k != -1)
                 .findFirst()
                 .orElseThrow();
}
