static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(String::toCharArray)
                     .toArray(char[][]::new);

    var splitterPositions = IntStream.range(0, input.length)
                                     .mapToObj(rowI -> IntStream.range(0, input[0].length)
                                                                .filter(colI -> input[rowI][colI] == '^')
                                                                .mapToObj(colI -> new Position(rowI, colI)))
                                     .flatMap(k -> k)
                                     .toArray(Position[]::new);

    var part1Result = Arrays.stream(splitterPositions)
                            .filter(k -> k.rowI == 2 || canBeamReachPosition(k.rowI, k.colI, input))
                            .count();

    var part2Result = 0; // FIXME

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean canBeamReachPosition(int rowI, int colI, char[][] input) {
    return IntStream.range(0, rowI)
                    .map(k -> -k)
                    .sorted()
                    .map(k -> -k)
                    .takeWhile(k -> input[k][colI] == '.')
                    .anyMatch(k -> input[k][colI + 1] == '^' || input[k][colI - 1] == '^');
}

record Position(int rowI, int colI) {}