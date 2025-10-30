static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var part1Text = Stream.iterate(input, k -> generateLookAndSay(k))
                          .skip(40)
                          .findFirst()
                          .orElseThrow();

    var part2Text = Stream.iterate(part1Text, k -> generateLookAndSay(k))
                          .skip(10)
                          .findFirst()
                          .orElseThrow();

    System.out.println("Result 1: " + part1Text.length() + ", result 2: " + part2Text.length());
}

static String generateLookAndSay(String text) {
    var characterRanges = IntStream.rangeClosed(0, text.length())
                                   .filter(i -> i == 0 || i == text.length() || text.charAt(i) != text.charAt(i - 1))
                                   .toArray();

    return IntStream.range(0, characterRanges.length - 1)
                    .mapToObj(i -> generateSection(text, characterRanges[i], characterRanges[i + 1]))
                    .collect(Collectors.joining());
}

static String generateSection(String text, int begin, int end) {
    return (end - begin) + "" + text.charAt(begin);
}