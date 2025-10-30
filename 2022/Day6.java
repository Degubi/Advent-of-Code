static void main(String[] args) throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var part1Result = findMarkerPosition(input, 4);
    var part2Result = findMarkerPosition(input, 14);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int findMarkerPosition(String text, int distinctCharCount) {
    return IntStream.range(0, text.length() - distinctCharCount)
                    .filter(i -> text.substring(i, i + distinctCharCount).chars().distinct().count() == distinctCharCount)
                    .findFirst()
                    .orElse(-distinctCharCount - 1) + distinctCharCount;
}