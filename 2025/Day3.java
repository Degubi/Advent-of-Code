static void main() throws Exception {
    var bankDigits = Files.lines(Path.of("input.txt"))
                          .map(k -> k.chars().map(Character::getNumericValue).toArray())
                          .toArray(int[][]::new);

    var part1Result = Arrays.stream(bankDigits)
                            .mapToLong(k -> findMaxJoltage(k, 2))
                            .sum();

    var part2Result = Arrays.stream(bankDigits)
                            .mapToLong(k -> findMaxJoltage(k, 12))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long findMaxJoltage(int[] digits, int resultDigitCount) {
    var resultDigits = new ArrayList<Integer>();
    var digitCountToRemove = digits.length - resultDigitCount;

    for (var digit : digits) {
        while (!resultDigits.isEmpty() && digitCountToRemove > 0 && resultDigits.getLast() < digit) {
            resultDigits.removeLast();
            --digitCountToRemove;
        }

        resultDigits.add(digit);
    }

    return IntStream.range(0, resultDigitCount)
                    .mapToLong(i -> resultDigits.get(i) * (long) Math.pow(10, resultDigitCount - i - 1))
                    .sum();
}