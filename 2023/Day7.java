static void main() throws Exception {
    var splitLines = Files.lines(Path.of("input.txt"))
                          .map(k -> k.split(" ", 2))
                          .toArray(String[][]::new);

    var part1Result = calculateWinnings(splitLines, k -> getCardCounts(k), k -> getCardRankPart1(k));
    var part2Result = calculateWinnings(splitLines, k -> getBestCardCounts(k), k -> getCardRankPart2(k));

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateWinnings(String[][] splitLines, Function<String, Map<Character, Long>> cardCountCalculator, ToIntFunction<Character> cardRanker) {
    var hands = Arrays.stream(splitLines)
                      .map(k -> new Hand(k[0], cardCountCalculator.apply(k[0]), Integer.parseInt(k[1])))
                      .sorted((h1, h2) -> compareHands(h1, h2, cardRanker))
                      .toArray(Hand[]::new);

    return IntStream.range(0, hands.length)
                    .map(i -> (i + 1) * hands[i].bid())
                    .sum();
}

static HandKind getHandKind(Hand hand) {
    return switch(hand.cardCounts().size()) {
        case 1  -> HandKind.FIVE_OF_A_KIND;
        case 2  -> hand.cardCounts().containsValue(4L) ? HandKind.FOUR_OF_A_KIND : HandKind.FULL_HOUSE;
        case 3  -> hand.cardCounts().containsValue(3L) ? HandKind.THREE_OF_A_KIND : HandKind.TWO_PAIR;
        case 4  -> HandKind.ONE_PAIR;
        case 5  -> HandKind.HIGH_CARD;
        default -> null;
    };
}

static int getCardRankPart1(char card) { return getCardRank(card, 11); }
static int getCardRankPart2(char card) { return getCardRank(card, 1); }

static int getCardRank(char card, int jRank) {
    return switch(card) {
        case 'A' -> 14;
        case 'K' -> 13;
        case 'Q' -> 12;
        case 'J' -> jRank;
        case 'T' -> 10;
        default -> Character.getNumericValue(card);
    };
}

static Map<Character, Long> getCardCounts(String cards) {
    return cards.chars()
                .mapToObj(k -> Character.valueOf((char) k))
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
}

static Map<Character, Long> getBestCardCounts(String cards) {
    var cardCounts = getCardCounts(cards);

    return cardCounts.keySet().stream()
                     .map(k -> cards.replace('J', k))
                     .distinct()
                     .map(k -> new Hand(k, getCardCounts(k), 0))
                     .max((h1, h2) -> compareHands(h1, h2, k -> getCardRankPart2(k)))
                     .orElseThrow()
                     .cardCounts();
}

static int compareHands(Hand hand1, Hand hand2, ToIntFunction<Character> cardRanker) {
    var kindCompare = Integer.compare(getHandKind(hand2).ordinal(), getHandKind(hand1).ordinal());

    return kindCompare != 0 ? kindCompare : compareCards(hand1.cards(), hand2.cards(), cardRanker);
}

static int compareCards(String cards1, String cards2, ToIntFunction<Character> cardRanker) {
    var cardRankDiffIndex = IntStream.range(0, 5)
                                     .filter(i -> cardRanker.applyAsInt(cards1.charAt(i)) != cardRanker.applyAsInt(cards2.charAt(i)))
                                     .findFirst()
                                     .orElseThrow();

    return Integer.compare(cardRanker.applyAsInt(cards1.charAt(cardRankDiffIndex)), cardRanker.applyAsInt(cards2.charAt(cardRankDiffIndex)));
}

enum HandKind {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

record Hand(String cards, Map<Character, Long> cardCounts, int bid) {}