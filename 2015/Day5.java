static void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    var part1Result = lines.stream()
                           .filter(k -> containsAtLeast3Vowels(k) && containsTwiceInARowLetter(k) && doesntContainWrongChars(k))
                           .count();

    var part2Result = lines.stream()
                           .filter(k -> part2Rule1(k) && part2Rule2(k))
                           .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean containsAtLeast3Vowels(String text) {
    return text.chars()
               .filter(k -> k == 'a' || k == 'e' || k == 'i' || k == 'o' || k == 'u')
               .count() >= 3;
}

static boolean containsTwiceInARowLetter(String text) {
    return IntStream.range(0, text.length() - 1)
                    .anyMatch(i -> text.charAt(i) == text.charAt(i + 1));
}

static boolean doesntContainWrongChars(String text) {
    return !text.contains("ab") && !text.contains("cd") && !text.contains("pq") && !text.contains("xy");
}

static boolean part2Rule1(String text) {
    return IntStream.range(0, text.length() - 1)
                    .anyMatch(i -> IntStream.range(0, text.length() - 1)
                                            .filter(k -> k != i && k != (i + 1) && i != (k + 1))
                                            .anyMatch(k -> text.charAt(i) == text.charAt(k) && text.charAt(i + 1) == text.charAt(k + 1)));
}

static boolean part2Rule2(String text) {
    return IntStream.range(0, text.length() - 2)
                    .anyMatch(i -> text.charAt(i) == text.charAt(i + 2));
}