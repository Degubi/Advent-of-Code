static void main() throws Exception {
    var values = Files.lines(Path.of("input.txt"))
                      .map(k -> Arrays.stream(k.split(" ")).mapToInt(Integer::parseInt).toArray())
                      .toArray(int[][]::new);

    var part1Result = Arrays.stream(values)
                            .filter(k -> isReportValid(k))
                            .count();

    var part2Result = Arrays.stream(values)
                            .filter(k -> isReportValid(k) || IntStream.range(0, k.length).anyMatch(r -> isReportValid(removeElementAtIndex(k, r))))
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isReportValid(int[] values) {
    return IntStream.range(0, values.length -1)
                    .reduce((int) Math.signum(values[0] - values[1]), (lastSign, i) -> {
                        var diff = values[i] - values[i + 1];
                        var currentSign = (int) Math.signum(diff);
                        var absDiff = Math.abs(diff);

                        return lastSign != currentSign || absDiff < 1 || absDiff > 3 ? Integer.MIN_VALUE : currentSign;
                    }) != Integer.MIN_VALUE;
}

static int[] removeElementAtIndex(int[] values, int index) {
    return IntStream.range(0, values.length)
                    .filter(i -> i != index)
                    .map(i -> values[i])
                    .toArray();
}
