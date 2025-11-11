static void main() throws Exception {
    var boxes = Files.readAllLines(Path.of("input.txt"));
    var stats = boxes.stream()
                     .map(k -> k.chars().boxed().collect(Collectors.groupingBy(l -> (char) l.intValue(), Collectors.counting())))
                     .map(k -> new boolean[] { k.values().contains(2L), k.values().contains(3L) })
                     .collect(Collectors.teeing(
                          Collectors.groupingBy(k -> k[0], Collectors.counting()),
                          Collectors.groupingBy(k -> k[1], Collectors.counting()),
                          (k1, k2) -> new long[] { k1.getOrDefault(true, 0L), k2.getOrDefault(true, 0L) }
                      ));

    var part1Result = stats[0] * stats[1];
    var part2Result = IntStream.range(0, boxes.size())
                               .mapToObj(w1I -> {
                                   var word1 = boxes.get(w1I);
                                   var w2I = IntStream.range(0, boxes.size())
                                                   .filter(k -> IntStream.range(0, word1.length())
                                                                         .filter(i -> word1.charAt(i) != boxes.get(k).charAt(i))
                                                                         .count() == 1)
                                                   .findFirst()
                                                   .orElse(-1);

                                    return w2I == -1 ? null : new String[] { word1, boxes.get(w2I) };
                               })
                               .filter(Objects::nonNull)
                               .findFirst()
                               .map(k -> IntStream.range(0, k[0].length())
                                                  .filter(i -> k[0].charAt(i) == k[1].charAt(i))
                                                  .mapToObj(i -> Character.toString(k[0].charAt(i)))
                                                  .collect(Collectors.joining()))
                               .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}
