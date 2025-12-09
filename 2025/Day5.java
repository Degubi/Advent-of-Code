static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt")).split("\n\n");
    var inputRanges = input[0].lines()
                              .map(k -> k.split("-"))
                              .map(k -> new Range(Long.parseLong(k[0]), Long.parseLong(k[1])))
                              .sorted(Comparator.comparingLong(Range::begin).thenComparing(Range::end))
                              .toArray(Range[]::new);

    var ranges = deduplicateRanges(inputRanges);
    var part1Result = input[1].lines()
                              .mapToLong(Long::parseLong)
                              .filter(k -> ranges.stream().anyMatch(r -> k >= r.begin && k <= r.end))
                              .count();

    var part2Result = ranges.stream()
                            .mapToLong(k -> k.end - k.begin + 1)
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static ArrayList<Range> deduplicateRanges(Range[] ranges) {
    var result = new ArrayList<Range>();
    var currentRange = ranges[0];

    for(var nextRange : ranges) {
        if(currentRange.end < nextRange.begin) {
            result.add(currentRange);
            currentRange = nextRange;
        }else{
            currentRange = new Range(currentRange.begin, Math.max(currentRange.end, nextRange.end));
        }
    }

    result.add(currentRange);
    return result;
}

record Range(long begin, long end) {}