static void main() throws Exception {
    var rucksacks = Files.readAllLines(Path.of("input.txt"));
    var part1Result = rucksacks.stream()
                               .map(k -> getCompartmentsPart1(k))
                               .mapToInt(k -> getCommonItemPriority(k))
                               .sum();

    var part2Result = IntStream.range(0, rucksacks.size() - 2)
                               .filter(i -> i % 3 == 0)
                               .map(i -> getCommonItemPriority(rucksacks.get(i), rucksacks.get(i + 1), rucksacks.get(i + 2)))
                               .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static String[] getCompartmentsPart1(String items) {
    var lengthHalf = items.length() / 2;

    return new String[] { items.substring(0, lengthHalf), items.substring(lengthHalf) };
}

static int getCommonItemPriority(String... compartments) {
    var commonItem = compartments[0].chars()
                                    .filter(k -> Arrays.stream(compartments, 1, compartments.length).allMatch(c -> c.contains(Character.toString(k))))
                                    .findFirst()
                                    .orElseThrow();

    return commonItem - (commonItem >= 'a' && commonItem <= 'z' ? 96 : 38);
}