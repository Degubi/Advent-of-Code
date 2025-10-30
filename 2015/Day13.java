static void main() throws Exception {
    var rules = Files.lines(Path.of("input.txt"))
                     .map(k -> k.split(" "))
                     .map(k -> new Rule(k[0], (k[2].equals("gain") ? 1 : -1) * Integer.parseInt(k[3]), k[10].substring(0, k[10].length() - 1)))
                     .toArray(Rule[]::new);

    var part1Names = Arrays.stream(rules)
                           .map(Rule::personName)
                           .distinct()
                           .toArray(String[]::new);

    var part1Result = generatePermutations(part1Names)
                     .mapToInt(k -> calculateHappinessChangeForPermutation(k, rules))
                     .max()
                     .orElseThrow();

    var part2Names = Arrays.copyOf(part1Names, part1Names.length + 1);
    part2Names[part2Names.length - 1] = "Mario";

    var part2Result = generatePermutations(part2Names)
                     .mapToInt(k -> calculateHappinessChangeForPermutation(k, rules))
                     .max()
                     .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateHappinessChangeForPermutation(String[] names, Rule[] rules) {
    var firstNameChange = calculateHappinessChange(names[0], names[names.length - 1], rules);
    var lastNameChange = calculateHappinessChange(names[names.length - 1], names[0], rules);
    var otherNameChanges = IntStream.range(0, names.length - 1)
                                    .flatMap(i -> IntStream.of(
                                        calculateHappinessChange(names[i], names[i + 1], rules),
                                        calculateHappinessChange(names[i + 1], names[i], rules)))
                                    .sum();

    return firstNameChange + lastNameChange + otherNameChanges;
}

static int calculateHappinessChange(String name, String sittingNextToName, Rule[] rules) {
    return Arrays.stream(rules)
                 .filter(k -> k.personName().equals(name) && k.personSittingNextTo().equals(sittingNextToName))
                 .mapToInt(Rule::happinessChange)
                 .findFirst()
                 .orElse(0);
}

static Stream<String[]> generatePermutations(String[] arr) {
    var elements = new ArrayList<String[]>();
    permute(arr, 0, elements);
    return elements.stream();
}

static void permute(String[] arr, int start, ArrayList<String[]> outElements) {
    if(start == arr.length - 1) {
        outElements.add(arr.clone());
    }else{
        for(var i = start; i < arr.length; ++i) {
            swap(arr, start, i);
            permute(arr, start + 1, outElements);
            swap(arr, start, i);
        }
    }
}

static void swap(String[] arr, int i, int j) {
    var temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

record Rule(String personName, int happinessChange, String personSittingNextTo) {}