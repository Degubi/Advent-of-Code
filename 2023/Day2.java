static void main() throws Exception {
    var games = Files.lines(Path.of("input.txt"))
                     .map(k -> makeGame(k))
                     .toArray(Game[]::new);

    var part1Result = Arrays.stream(games)
                            .filter(k -> isGamePossible(k))
                            .mapToInt(Game::id)
                            .sum();

    var part2Result = Arrays.stream(games)
                            .map(k -> statisticizeColorRevealCounts(k))
                            .map(Map::values)
                            .mapToInt(k -> k.stream().mapToInt(IntSummaryStatistics::getMax).reduce(1, (l, m) -> l * m))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Game makeGame(String line) {
    var gamePrefixIndex = line.indexOf(':');
    var revealSets = Arrays.stream(line.substring(gamePrefixIndex + 1).split(";"))
                           .map(k -> k.split(","))
                           .map(k -> createRevealSet(k))
                           .toArray(Reveal[][]::new);

    return new Game(Integer.parseInt(line.substring(0, gamePrefixIndex).split(" ", 2)[1]), revealSets);
}

static Reveal[] createRevealSet(String[] setString) {
    return Arrays.stream(setString)
                 .map(String::strip)
                 .map(k -> k.split(" ", 2))
                 .map(k -> new Reveal(Integer.parseInt(k[0]), k[1]))
                 .toArray(Reveal[]::new);
}

static boolean isGamePossible(Game game) {
    return Arrays.stream(game.reveals())
                 .flatMap(Arrays::stream)
                 .allMatch(k -> isRevealValid(k));
}

static boolean isRevealValid(Reveal reveal) {
    return switch(reveal.color()) {
        case "red"   -> reveal.count() <= 12;
        case "green" -> reveal.count() <= 13;
        case "blue"  -> reveal.count() <= 14;
        default      -> false;
    };
}

static Map<String, IntSummaryStatistics> statisticizeColorRevealCounts(Game game) {
    return Arrays.stream(game.reveals())
                 .flatMap(Arrays::stream)
                 .collect(Collectors.groupingBy(Reveal::color, Collectors.summarizingInt(Reveal::count)));
}

record Game(int id, Reveal[][] reveals) {}
record Reveal(int count, String color) {}