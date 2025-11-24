static void main() throws Exception {
    var groups = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
                       .map(k -> k.split("\n"))
                       .toArray(String[][]::new);

    var part1Result = Arrays.stream(groups)
                            .mapToLong(k -> String.join("", k).chars().distinct().count())
                            .sum();

    var part2Result = Arrays.stream(groups)
                            .mapToLong(k -> String.join("", k)
                                                  .chars()
                                                  .distinct()
                                                  .filter(c -> Arrays.stream(k).allMatch(l -> l.indexOf(c) != -1))
                                                  .count())
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}
