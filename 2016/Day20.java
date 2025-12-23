static void main() throws Exception {
    var inputRanges = Files.lines(Path.of("input.txt"))
                           .map(k -> k.split("-"))
                           .map(k -> new Range(Long.parseLong(k[0]), Long.parseLong(k[1])))
                           .sorted(Comparator.comparingLong(Range::begin).thenComparing(Range::end))
                           .toArray(Range[]::new);

    var ranges = deduplicateRanges(inputRanges);

    var part1Result = ranges.stream()
                            .gather(Gatherers.windowSliding(2))
                            .filter(k -> k.getLast().begin != k.getFirst().end)
                            .mapToLong(k -> k.getFirst().end + 1)
                            .findFirst()
                            .orElseThrow();

    var part2Result = ranges.stream()
                            .gather(Gatherers.windowSliding(2))
                            .mapToLong(k -> k.getLast().begin - k.getFirst().end - 1)
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
