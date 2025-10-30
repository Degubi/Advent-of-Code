static void main() throws Exception {
    var rangePairs = Files.lines(Path.of("input.txt"))
                          .map(k -> parseRangePair(k))
                          .toArray(Range[][]::new);

    var part1Result = Arrays.stream(rangePairs)
                            .filter(k -> doesRangeInclude(k[0], k[1]) || doesRangeInclude(k[1], k[0]))
                            .count();

    var part2Result = Arrays.stream(rangePairs)
                            .filter(k -> doRangesOverlap(k[0], k[1]))
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Range[] parseRangePair(String line) {
    var pairSplit = line.split(",");
    var range1Split = pairSplit[0].split("-");
    var range2Split = pairSplit[1].split("-");

    return new Range[] {
        new Range(Integer.parseInt(range1Split[0]), Integer.parseInt(range1Split[1])),
        new Range(Integer.parseInt(range2Split[0]), Integer.parseInt(range2Split[1]))
    };
}

static boolean doesRangeInclude(Range range, Range other) { return range.begin() >= other.begin() && range.end() <= other.end(); }
static boolean doRangesOverlap(Range r1, Range r2) { return r1.begin() <= r2.end() && r2.begin() <= r1.end(); }

record Range(int begin, int end) {}