static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var part1Result = findCorrectChecksum(generateDiskState(input, 272));
    var part2Result = findCorrectChecksum(generateDiskState(input, 35651584));

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static String findCorrectChecksum(String diskState) {
    return Stream.iterate(diskState, k -> computeChecksum(k))
                 .filter(k -> k.length() % 2 != 0)
                 .findFirst()
                 .orElseThrow();
}

static String generateDiskState(String input, int length) {
    return Stream.iterate(input, k -> computeNextDiskState(k, length))
                 .filter(k -> k.length() == length)
                 .findFirst()
                 .orElseThrow();
}

static String computeNextDiskState(String a, int length) {
    var b = new StringBuilder(a).reverse()
                                .chars()
                                .map(k -> k == '0' ? '1' : '0')
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);

    var untrimmedResult = a + '0' + b;

    return untrimmedResult.substring(0, Math.min(untrimmedResult.length(), length));
}

static String computeChecksum(String value) {
    return value.chars()
                .boxed()
                .gather(Gatherers.windowFixed(2))
                .map(k -> k.getFirst().equals(k.getLast()) ? "1" : "0")
                .collect(Collectors.joining());
}