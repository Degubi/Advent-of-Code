static void main() throws Exception {
    var sections = Files.readString(Path.of("input.txt")).split("\n\n");
    var crateLines = sections[0].split("\n");
    var longestCrateLine = Arrays.stream(crateLines, 0, crateLines.length - 1)
                                 .max(Comparator.comparingInt(String::length))
                                 .orElseThrow();

    var paddedCrateLines = Arrays.stream(crateLines, 0, crateLines.length - 1)
                                 .map(k -> k + " ".repeat(longestCrateLine.length() - k.length()))
                                 .toArray(String[]::new);

    var crateColumns = IntStream.iterate(1, k -> k + 4)
                                .takeWhile(i -> i < longestCrateLine.length())
                                .mapToObj(i -> createBoxStack(i, paddedCrateLines))
                                .collect(Collectors.toList());

    var instructions = sections[1].lines()
                                  .map(k -> k.split(" "))
                                  .map(k -> new Instruction(Integer.parseInt(k[1]), Integer.parseInt(k[3]) - 1, Integer.parseInt(k[5]) - 1))
                                  .toArray(Instruction[]::new);

    var part1Result = executeInstructions(crateColumns, instructions, false);
    var part2Result = executeInstructions(crateColumns, instructions, true);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static String executeInstructions(List<List<Character>> crateColumns, Instruction[] instructions, boolean reverseCratesWhenMoving) {
    return Arrays.stream(instructions)
                 .reduce(cloneCrateColumns(crateColumns), (result, k) -> {
                     var from = result.get(k.from());
                     var to = result.get(k.to());
                     var cratesToMove = IntStream.range(0, k.count())
                                                 .mapToObj(_ -> from.removeLast())
                                                 .collect(Collectors.toList());

                     to.addAll(reverseCratesWhenMoving ? cratesToMove.reversed() : cratesToMove);
                     return result;
                 }, (_, l ) -> l)
                 .stream()
                 .map(List::getLast)
                 .map(String::valueOf)
                 .collect(Collectors.joining());
}

static List<Character> createBoxStack(int columnIndex, String[] paddedCrateLines) {
    return IntStream.iterate(paddedCrateLines.length - 1, k -> k - 1)
                    .takeWhile(i -> i >= 0)
                    .mapToObj(i -> paddedCrateLines[i].charAt(columnIndex))
                    .filter(k -> k != ' ')
                    .collect(Collectors.toList());
}

static List<List<Character>> cloneCrateColumns(List<List<Character>> crates) {
    return crates.stream()
                 .map(ArrayList::new)
                 .collect(Collectors.toList());
}

record Instruction(int count, int from, int to) {}