static void main() throws Exception {
    var containers = Files.lines(Path.of("input.txt"))
                          .mapToInt(Integer::parseInt)
                          .toArray();
    var eggnog = 150;

    var containersThatFit = IntStream.rangeClosed(1, containers.length)
                                     .mapToObj(i -> createCombinations(containers, i))
                                     .flatMap(k -> k.stream())
                                     .filter(k -> k.stream().mapToInt(l -> l).sum() == eggnog)
                                     .collect(Collectors.toList());

    var part1Result = containersThatFit.size();

    var containerMinSize = containersThatFit.stream()
                                            .mapToInt(List::size)
                                            .min()
                                            .orElseThrow();

    var part2Result = containersThatFit.stream()
                                       .filter(k -> k.size() == containerMinSize)
                                       .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static List<List<Integer>> createCombinations(int[] elements, int combinationSize) {
    var fullCombinations = new ArrayList<List<Integer>>();
    createCombinations(elements, fullCombinations, new ArrayList<>(), 0, combinationSize);
    return fullCombinations;
}

static void createCombinations(int[] elements, List<List<Integer>> fullCombinations, List<Integer> combination, int index, int missing) {
    if(missing == 0) {
        fullCombinations.add(combination);
        return;
    }

    for(var i = index; i <= elements.length - missing; ++i) {
        var newCombination = i == elements.length - missing ? combination : new ArrayList<>(combination);
        newCombination.add(elements[i]);
        createCombinations(elements, fullCombinations, newCombination, i + 1, missing - 1);
    }
}