static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .mapToInt(Integer::parseInt)
                     .toArray();

    var part1Result = Arrays.stream(input)
                            .map(k -> calculateFuel(k))
                            .sum();

    var part2Result = Arrays.stream(input)
                            .map(k -> sumFuelValues(k))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateFuel(int mass) { return mass / 3 - 2; }

static int sumFuelValues(int mass) {
    return IntStream.iterate(mass, k -> calculateFuel(k))
                    .skip(1)
                    .takeWhile(k -> k > 0)
                    .sum();
}