static void main() throws Exception {
    var inputRegex = Pattern.compile("\\s*(\\d*)\\s*(\\d*)\\s*(\\d*)");
    var input = Files.lines(Path.of("input.txt"))
                     .map(inputRegex::matcher)
                     .peek(Matcher::find)
                     .map(k -> new int[] { Integer.parseInt(k.group(1)), Integer.parseInt(k.group(2)), Integer.parseInt(k.group(3)) })
                     .toArray(int[][]::new);

    var part1Result = Arrays.stream(input)
                            .filter(k -> isValidTriangle(k[0], k[1], k[2]))
                            .count();

    var part2Result = IntStream.range(0, 3)
                               .map(y -> (int) IntStream.iterate(0, x -> x < input.length, x -> x + 3)
                                                        .filter(x -> isValidTriangle(input[x][y], input[x + 1][y], input[x + 2][y]))
                                                        .count())
                               .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isValidTriangle(int x, int y, int z) {
    return x + y > z && x + z > y && y + z > x;
}
