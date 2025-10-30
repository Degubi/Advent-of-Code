static final Pattern CARD_PATTERN = Pattern.compile("Card\\s+(\\d+): (.*) \\| (.*)");
static final Pattern NUMBER_SPLIT_PATTERN = Pattern.compile("\\s+");

static void main() throws Exception {
    var cards = Files.lines(Path.of("input.txt"))
                     .map(k -> createCard(k))
                     .toArray(Card[]::new);

    var part1Result = Arrays.stream(cards)
                            .mapToInt(k -> (int) Math.pow(2, k.winningNumberCount() - 1))
                            .sum();

    var part2Result = Arrays.stream(cards)
                            .mapToInt(k -> 1 + extractNestedCardCounts(k, cards))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Card createCard(String line) {
    var matcher = CARD_PATTERN.matcher(line);
    matcher.find();

    var winningNumbers = parseNumbers(matcher.group(2));
    var playerNumbers = parseNumbers(matcher.group(3));
    var winningNumberCount = Arrays.stream(winningNumbers)
                                   .filter(k -> Arrays.stream(playerNumbers).anyMatch(l -> k == l))
                                   .count();

    return new Card(Integer.parseInt(matcher.group(1)), (int) winningNumberCount);
}

static int[] parseNumbers(String numbersText) {
    return NUMBER_SPLIT_PATTERN.splitAsStream(numbersText.strip())
                               .mapToInt(Integer::parseInt)
                               .toArray();
}

static int extractNestedCardCounts(Card card, Card[] cards) {
    return Arrays.stream(cards, card.id(), card.id() + card.winningNumberCount())
                 .mapToInt(k -> 1 + extractNestedCardCounts(k, cards))
                 .sum();
}

record Card(int id, int winningNumberCount) {}