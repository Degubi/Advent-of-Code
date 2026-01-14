static void main() throws Exception {
    var inputMap = Files.lines(Path.of("input.txt"))
                        .map(String::toCharArray)
                        .toArray(char[][]::new);

    var paperRemoveSteps = Stream.iterate(new State(null, inputMap), k -> new State(k.current, removeRemovablePapers(k.current)))
                                 .takeWhile(k -> k.previous == null || countNumberOfPapers(k.previous) != countNumberOfPapers(k.current))
                                 .map(State::current)
                                 .toArray(char[][][]::new);

    var part1Result = countNumberOfPapers(paperRemoveSteps[0]) - countNumberOfPapers(paperRemoveSteps[1]);
    var part2Result = countNumberOfPapers(paperRemoveSteps[0]) - countNumberOfPapers(paperRemoveSteps[paperRemoveSteps.length - 1]);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int countNumberOfPapers(char[][] map) {
    return (int) IntStream.range(0, map.length)
                          .flatMap(rowI -> IntStream.range(0, map[0].length).filter(colI -> map[rowI][colI] == '@'))
                          .count();
}

static char[][] removeRemovablePapers(char[][] map) {
    var positions = IntStream.range(0, map.length)
                             .mapToObj(rowI -> IntStream.range(0, map[0].length)
                                                        .filter(colI -> isPaperFree(rowI, colI, map))
                                                        .mapToObj(colI -> new Position(rowI, colI)))
                             .flatMap(k -> k)
                             .toArray(Position[]::new);

    return Arrays.stream(positions).reduce(copyMap(map), (result, k) -> {
        result[k.rowI][k.colI] = '.';
        return result;
    }, (k, l) -> l);
}

static char[][] copyMap(char[][] map) { return Arrays.stream(map).map(k -> Arrays.copyOf(k, k.length)).toArray(char[][]::new); }

static boolean isPaperFree(int rowI, int colI, char[][] input) {
    return input[rowI][colI] == '@' && (
        isPositionPaper(rowI - 1, colI - 1, input) +
        isPositionPaper(rowI - 1, colI,     input) +
        isPositionPaper(rowI - 1, colI + 1, input) +
        isPositionPaper(rowI,     colI - 1, input) +
        isPositionPaper(rowI,     colI + 1, input) +
        isPositionPaper(rowI + 1, colI - 1, input) +
        isPositionPaper(rowI + 1, colI,     input) +
        isPositionPaper(rowI + 1, colI + 1, input)
    ) < 4;
}

static int isPositionPaper(int rowI, int colI, char[][] input) {
    return rowI >= 0 && colI >= 0 && rowI < input.length && colI < input[0].length && input[rowI][colI] == '@' ? 1 : 0;
}

record Position(int rowI, int colI) {}
record State(char[][] previous, char[][] current) {}