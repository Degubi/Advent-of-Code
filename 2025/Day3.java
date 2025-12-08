static void main() throws Exception {
    var banks = Files.readAllLines(Path.of("input.txt"));

    var part1Result = banks.stream()
                           .mapToLong(k -> findLargestJoltage(k, 2))
                           .sum();

    // FIXME
    var part2Result = banks.stream()
                           .mapToLong(k -> findLargestJoltage(k, 12))
                           .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long findLargestJoltage(String bank, int digitCount) {
    var sortedDigits = IntStream.range(0, bank.length())
                                .mapToObj(i -> new Digit(i, Character.getNumericValue(bank.charAt(i))))
                                .sorted(Comparator.comparingInt(Digit::value).reversed())
                                .toArray(Digit[]::new);

    return IntStream.range(0, sortedDigits.length)
                    // .mapToObj(i -> digitCount == 12 ? part2(i, sortedDigits) : part1(i, sortedDigits))
                    .mapToObj(i -> part1(i, sortedDigits))
                    .flatMap(Optional::stream)
                    .mapToLong(k -> sumDigits(k))
                    .findFirst()
                    .orElseThrow();
}

static Optional<Digit[]> part1(int i, Digit[] sortedDigits) {
    return findNextDigit(sortedDigits[i], sortedDigits)
          .map(k -> new Digit[] { sortedDigits[i], k });
}

static Optional<Digit[]> part2(int i, Digit[] sortedDigits) {
    var digits = new Digit[12];
    var previousDigit = sortedDigits[i];
    var previousDigitStoreIndex = 0;
    digits[0] = sortedDigits[i];

    for(var l = 0; l < 11; ++l) {
        var next = findNextDigit(previousDigit, sortedDigits);

        if(next.isPresent()) {
            var value = next.get();

            digits[++previousDigitStoreIndex] = value;
            previousDigit = value;
        }else{
            return Optional.empty();
        }
    }

    return Optional.of(digits);
}

static Optional<Digit> findNextDigit(Digit previousDigit, Digit[] sortedDigits) {
    return IntStream.range(0, sortedDigits.length)
                    .mapToObj(i -> sortedDigits[i])
                    .filter(k -> k.bankIndex > previousDigit.bankIndex)
                    .findFirst();
}

static long sumDigits(Digit[] digits) {
    return IntStream.range(0, digits.length)
                    .mapToLong(i -> digits[i].value * (long) Math.pow(10, digits.length - i - 1))
                    .sum();
}

record Digit(int bankIndex, int value) {}