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
    return false; // FIXME Part2
}