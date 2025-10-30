static void main() throws Exception {
    var presents = Integer.parseInt(Files.readString(Path.of("input.txt")));
    var part1Result = IntStream.range(1, Integer.MAX_VALUE)
                               .filter(k -> calculatePresentAmount(k, 10, _ -> true) >= presents)
                               .findFirst()
                               .orElseThrow();

    var part2Result = IntStream.range(1, Integer.MAX_VALUE)
                               .filter(k -> calculatePresentAmount(k, 11, d -> d <= 50) >= presents)
                               .findFirst()
                               .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculatePresentAmount(int houseNumber, int presentsPerDelivery, IntPredicate divisorPredicate) {
    return IntStream.rangeClosed(1, (int) Math.sqrt(houseNumber))
                    .filter(k -> houseNumber % k == 0)
                    .flatMap(k -> getDivisorPairs(k, houseNumber))
                    .filter(k -> divisorPredicate.test(houseNumber / k))
                    .sum() * presentsPerDelivery;
}

static IntStream getDivisorPairs(int divisor, int value) {
    var other = value / divisor;

    return divisor == other ? IntStream.of(divisor) : IntStream.of(divisor, other);
}