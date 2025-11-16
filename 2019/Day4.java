static void main() throws Exception {
    var range = Files.readString(Path.of("input.txt")).split("-");
    var rangeBegin = Integer.parseInt(range[0]);
    var rangeEnd = Integer.parseInt(range[1]);

    var part1Result = IntStream.rangeClosed(rangeBegin, rangeEnd)
                               .mapToObj(Integer::toString)
                               .filter(k -> isMatchingDigitPart1(k) && isIncreasing(k))
                               .count();

    var part2Result = IntStream.rangeClosed(rangeBegin, rangeEnd)
                               .mapToObj(Integer::toString)
                               .filter(k -> isMatchingDigitPart2(k) && isIncreasing(k))
                               .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isMatchingDigitPart1(String value) {
    return value.chars().boxed()
                .gather(Gatherers.windowSliding(2))
                .anyMatch(k -> k.get(0).equals(k.get(1)));
}

static boolean isIncreasing(String value) {
    return value.chars().boxed()
                .gather(Gatherers.windowSliding(2))
                .allMatch(k -> k.get(0) <= k.get(1));
}

static boolean isMatchingDigitPart2(String value) {
    return (" " + value + " ").chars().boxed()
                              .gather(Gatherers.windowSliding(4))
                              .anyMatch(k -> !k.get(0).equals(k.get(1)) && k.get(1).equals(k.get(2)) && !k.get(2).equals(k.get(3)));
}
