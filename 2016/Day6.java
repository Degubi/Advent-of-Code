static void main() throws Exception {
    var characters = Files.lines(Path.of("input.txt"))
                          .map(String::toCharArray)
                          .toArray(char[][]::new);

    var results = IntStream.range(0, characters[0].length)
                           .mapToObj(col -> IntStream.range(0, characters.length)
                                                     .mapToObj(row -> characters[row][col])
                                                     .collect(Collectors.groupingBy(k -> k, Collectors.counting())))
                           .map(k -> k.entrySet().stream()
                                      .sorted(Map.Entry.comparingByValue())
                                      .map(e -> Character.toString(e.getKey()))
                                      .toArray(String[]::new))
                           .collect(Collectors.teeing(
                              Collectors.mapping(k -> k[k.length - 1], Collectors.joining()),
                              Collectors.mapping(k -> k[0], Collectors.joining()),
                              (k, l) -> new String[] { k, l }
                           ));

    System.out.println("Result 1: " + results[0] + ", result 2: " + results[1]);
}