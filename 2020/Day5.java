static void main() throws Exception {
    var ids = Files.lines(Path.of("input.txt"))
                   .mapToInt(k -> parseBSPValue(0, 7, k, 128) * 8 + parseBSPValue(7, 9, k, 8))
                   .sorted()
                   .toArray();

    var part1Result = ids[ids.length - 1];
    var part2Result = IntStream.range(0, ids.length - 1)
                               .filter(i -> ids[i] != (ids[i + 1] - 1))
                               .map(i -> ids[i] + 1)
                               .findFirst()
                               .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int parseBSPValue(int begin, int end, String letters, int range) {
    var bottomRangeLetter = range == 128 ? 'F' : 'L';
    var resultRange = IntStream.range(begin, end)
                               .mapToObj(letters::charAt)
                               .reduce(new Range(0, range - 1), (k, l) -> updateRange(k, l, bottomRangeLetter), (_, l) -> l);

    return letters.charAt(end) == bottomRangeLetter ? resultRange.begin() : resultRange.end();
}

static Range updateRange(Range range, int letter, char bottomRangeLetter) {
    var midPoint = (range.begin() + range.end()) / 2;

    return letter == bottomRangeLetter ? new Range(range.begin(), midPoint) : new Range(midPoint + 1, range.end());
}

record Range(int begin, int end) {}