static void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    var numberDigitOnlyRegex = Pattern.compile("\\d");
    var numberDigitWordRegex = Pattern.compile("\\d|one|two|three|four|five|six|seven|eight|nine");
    var part1Result = lines.stream()
                           .mapToInt(k -> parseTwoDigitNumber(k, numberDigitOnlyRegex))
                           .sum();

    var part2Result = lines.stream()
                           .mapToInt(k -> parseTwoDigitNumber(k, numberDigitWordRegex))
                           .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int parseTwoDigitNumber(String line, Pattern regex) {
    var matcher = regex.matcher(line);
    var results = IntStream.range(0, line.length())
                           .mapToObj(i -> matcher.find(i) ? matcher.group() : null)
                           .filter(Objects::nonNull)
                           .toArray(String[]::new);

    return getNumericValue(results[0]) * 10 + getNumericValue(results[results.length - 1]);
}

static int getNumericValue(String text) {
    return switch(text) {
        case "one"   -> 1;
        case "two"   -> 2;
        case "three" -> 3;
        case "four"  -> 4;
        case "five"  -> 5;
        case "six"   -> 6;
        case "seven" -> 7;
        case "eight" -> 8;
        case "nine"  -> 9;
        default -> Character.getNumericValue(text.charAt(0));
    };
}