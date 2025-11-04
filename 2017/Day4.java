static void main() throws Exception {
    var inputParts = Files.lines(Path.of("input.txt"))
                          .map(k -> k.split(" "))
                          .toArray(String[][]::new);

    var part1Result = Arrays.stream(inputParts)
                            .filter(k -> isValidPassPhrasePart1(k))
                            .count();

    var part2Result = Arrays.stream(inputParts)
                            .filter(k -> isValidPassPhrasePart2(k))
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isValidPassPhrasePart1(String[] parts) {
    return Arrays.stream(parts)
                 .distinct()
                 .count() == parts.length;
}

static boolean isValidPassPhrasePart2(String[] parts) {
    return Arrays.stream(parts)
                 .map(k -> k.chars().boxed().collect(Collectors.groupingBy(l -> l, Collectors.counting())))
                 .distinct()
                 .count() == parts.length;
}
