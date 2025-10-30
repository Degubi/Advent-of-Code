static void main() throws Exception {
    var originalPlatform = Files.lines(Path.of("input.txt"))
                                .map(String::toCharArray)
                                .toArray(char[][]::new);

    var platformDimension = originalPlatform.length;
    var rotatedPlatform = IntStream.range(0, platformDimension)
                                   .mapToObj(y -> IntStream.range(0, platformDimension).map(x -> originalPlatform[x][y]).toArray())
                                   .toArray(int[][]::new);

    var columnIndexToIntervals = IntStream.range(0, platformDimension)
                                          .mapToObj(i -> findIntervalsForColumn(i, rotatedPlatform, platformDimension))
                                          .toArray(ColumnInterval[][]::new);

    var part1Result = Arrays.stream(columnIndexToIntervals)
                            .flatMap(Arrays::stream)
                            .filter(k -> k.roundRockCount() != 0)
                            .mapToInt(k -> calculateColumnIntervalLoad(k, platformDimension))
                            .sum();

    System.out.println("Result 1: " + part1Result);
}

static int calculateColumnIntervalLoad(ColumnInterval interval, int platformDimension) {
    var sumTo = platformDimension - interval.begin();
    return sumFromKToN(sumTo - interval.roundRockCount() + 1, sumTo);
}

static ColumnInterval[] findIntervalsForColumn(int columnIndex, int[][] rotatedPlatform, int platformDimension) {
    var begin = IntStream.range(0, platformDimension)
                         .filter(i -> rotatedPlatform[columnIndex][i] != '#')
                         .findFirst()
                         .orElseThrow() - 1;

    var end = IntStream.range(0, platformDimension)
                       .map(i -> -i)
                       .sorted()
                       .map(i -> -i)
                       .filter(i -> rotatedPlatform[columnIndex][i] != '#')
                       .findFirst()
                       .orElseThrow() + 1;

    var columnIntervals = IntStream.rangeClosed(begin, end)
                                   .filter(i -> i == begin || i == end || (rotatedPlatform[columnIndex][i] == '#' && (rotatedPlatform[columnIndex][i - 1] != '#' || rotatedPlatform[columnIndex][i + 1] != '#')))
                                   .toArray();

    return IntStream.range(0, columnIntervals.length - 1)
                    .mapToObj(i -> new ColumnInterval(columnIntervals[i] + 1, columnIntervals[i + 1], countRoundedRocksForInterval(rotatedPlatform[columnIndex], columnIntervals[i] + 1, columnIntervals[i + 1])))
                    .toArray(ColumnInterval[]::new);
}

static int countRoundedRocksForInterval(int[] column, int begin, int end) {
    return (int) Arrays.stream(column, begin, end)
                       .filter(k -> k == 'O')
                       .count();
}

static int sumFromKToN(int a, int b) { return a == b ? a : (b - a + 1) * (a + b) / 2; }

record ColumnInterval(int begin, int end, int roundRockCount) {}