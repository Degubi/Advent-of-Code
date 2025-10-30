static void main() throws Exception {
    var policyRegex = Pattern.compile("(\\d+)-(\\d+) ([a-z]): (.*)");
    var input = Files.lines(Path.of("input.txt"))
                     .map(policyRegex::matcher)
                     .peek(Matcher::find)
                     .map(k -> new PasswordPolicy(Integer.parseInt(k.group(1)), Integer.parseInt(k.group(2)), k.group(3).charAt(0), k.group(4)))
                     .toArray(PasswordPolicy[]::new);

    var part1Result = Arrays.stream(input)
                            .filter(k -> isPart1PolicyValid(k))
                            .count();

    var part2Result = Arrays.stream(input)
                            .filter(k -> isPart2PolicyValid(k))
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isPart1PolicyValid(PasswordPolicy policy) {
    var letterCount = policy.password().chars()
                            .filter(k -> k == policy.letter())
                            .count();

    return letterCount >= policy.minCount() && letterCount <= policy.maxCount();
}

static boolean isPart2PolicyValid(PasswordPolicy policy) {
    return (policy.password().charAt(policy.minCount() - 1) == policy.letter()) ^ (policy.password().charAt(policy.maxCount() - 1) == policy.letter());
}

record PasswordPolicy(int minCount, int maxCount, char letter, String password) {}