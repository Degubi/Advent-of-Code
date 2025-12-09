static void main() throws Exception {
    var ranges = Arrays.stream(Files.readString(Path.of("input.txt")).split(","))
                       .map(k -> k.split("-"))
                       .map(k -> new long[] { Long.parseLong(k[0]), Long.parseLong(k[1]) })
                       .toArray(long[][]::new);

    var part1Result = Arrays.stream(ranges)
                            .flatMapToLong(k -> LongStream.rangeClosed(k[0], k[1]).filter(l -> doesFirstHalfRepeat(l)))
                            .sum();

    var part2Result = Arrays.stream(ranges)
                            .flatMapToLong(k -> LongStream.rangeClosed(k[0], k[1]).filter(l -> hasRepeatingSubsequence(l)))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean doesFirstHalfRepeat(long value) {
    var digitCount = ((int) Math.log10(value)) + 1;
    var divisor = (int) Math.pow(10, digitCount / 2);

    return value / divisor == value % divisor;
}

static boolean hasRepeatingSubsequence(long value) {
    var digitCount = ((int) Math.log10(value)) + 1;
    var digits = Long.toString(value).chars()
                     .map(Character::getNumericValue)
                     .boxed()
                     .toArray(Integer[]::new);

    return IntStream.rangeClosed(1, digitCount / 2)
                    .anyMatch(c -> isRepeatingSubsequence(digits, c));
}

private static boolean isRepeatingSubsequence(Integer[] digits, int count) {
    var subsequences = Arrays.stream(digits)
                             .gather(Gatherers.windowFixed(count))
                             .collect(Collectors.toList());

    return subsequences.stream()
                       .allMatch(subsequences.getFirst()::equals);
}