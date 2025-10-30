static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .mapToInt(Integer::parseInt)
                     .toArray();

    var part1Result = IntStream.range(0, input.length - 1)
                               .mapToObj(i -> Arrays.stream(input, i + 1, input.length).mapToObj(j -> new int[] { input[i], j }))
                               .flatMap(k -> k)
                               .filter(k -> k[0] + k[1] == 2020)
                               .mapToInt(k -> k[0] * k[1])
                               .findFirst()
                               .orElseThrow();

    var part2Result = IntStream.range(0, input.length - 1)
                               .mapToObj(i -> IntStream.range(i + 1, input.length - 1).mapToObj(j -> Arrays.stream(input, j + 1, input.length).mapToObj(l -> new int[] { input[i], input[j], l })))
                               .flatMap(k -> k)
                               .flatMap(k -> k)
                               .filter(k -> k[0] + k[1] + k[2] == 2020)
                               .mapToInt(k -> k[0] * k[1] * k[2])
                               .findFirst()
                               .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}
