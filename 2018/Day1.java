static void main() throws Exception {
    var changes = Files.lines(Path.of("input.txt"))
                       .mapToInt(Integer::parseInt)
                       .toArray();

    var part1Result = Stream.iterate(new State1(0, 0), k -> k.index == changes.length ? null : new State1(k.frequency + changes[k.index], k.index + 1))
                            .takeWhile(Objects::nonNull)
                            .map(State1::frequency)
                            .collect(Collectors.toList())
                            .getLast();

    var part2Result = Stream.iterate(new State2(0, 0, new HashSet<>()), k -> new State2(k.frequency + changes[k.index], k.index == changes.length - 1 ? 0 : k.index + 1, k.seenFrequencies))
                            .dropWhile(k -> k.seenFrequencies.add(k.frequency))
                            .findFirst()
                            .orElseThrow().frequency;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

record State1(int frequency, int index) {}
record State2(int frequency, int index, HashSet<Integer> seenFrequencies) {}
