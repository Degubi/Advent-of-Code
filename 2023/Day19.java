static final Pattern WORKFLOW_PATTERN = Pattern.compile("([a-z]+)\\{(.*)\\}");
static final Pattern RULE_PATTERN = Pattern.compile("([a-z])(<|>)(\\d+):([a-zA-Z]+)");
static final Pattern RATING_PATTERN = Pattern.compile("([a-z])=(\\d+)");

static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt")).split("\n\n", 2);
    var workflows = Arrays.stream(input[0].split("\n"))
                          .map(k -> parseWorkflow(k))
                          .toArray(Workflow[]::new);

    var ratings = Arrays.stream(input[1].split("\n"))
                        .map(k -> parseRatings(k))
                        .toArray(Rating[][]::new);

    var part1Result = Arrays.stream(ratings)
                            .filter(k -> evaluateWorkflow("in", workflows, k))
                            .mapToLong(k -> Arrays.stream(k).mapToInt(Rating::value).sum())
                            .sum();

    // TODO
    var part2Result = 0;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean evaluateWorkflow(String result, Workflow[] workflows, Rating[] ratings) {
    return result.equals("A") ? true :
           result.equals("R") ? false : evaluateWorkflow(applyRules(findWorkflow(result, workflows).rules(), ratings), workflows, ratings);
}

static Workflow findWorkflow(String name, Workflow[] workflows) {
    return Arrays.stream(workflows)
                 .filter(k -> k.name().equals(name))
                 .findFirst()
                 .orElseThrow();
}

static String applyRules(Rule[] rules, Rating[] ratings) {
    return Arrays.stream(rules)
                 .filter(k -> k.condition().test(ratings))
                 .map(Rule::result)
                 .findFirst()
                 .orElseThrow();
}

static Workflow parseWorkflow(String line) {
    var matcher = WORKFLOW_PATTERN.matcher(line);
    matcher.find();

    var rules = Arrays.stream(matcher.group(2).split(","))
                      .map(k -> parseRule(k))
                      .toArray(Rule[]::new);

    return new Workflow(matcher.group(1), rules);
}

static Rule parseRule(String ruleString) {
    if(!ruleString.contains(":")) {
        return new Rule(_ -> true, ruleString);
    }

    var matcher = RULE_PATTERN.matcher(ruleString);
    matcher.find();

    var argument = matcher.group(1);
    var operator = matcher.group(2);
    var value = Integer.parseInt(matcher.group(3));
    var result = matcher.group(4);

    return operator.equals("<") ? new Rule(k -> extractRatingValue(k, argument) < value, result)
                                : new Rule(k -> extractRatingValue(k, argument) > value, result);
}

static Rating[] parseRatings(String line) {
    return RATING_PATTERN.matcher(line).results()
                         .map(k -> new Rating(k.group(1), Integer.parseInt(k.group(2))))
                         .toArray(Rating[]::new);
}

static int extractRatingValue(Rating[] ratings, String letter) {
    return Arrays.stream(ratings)
                 .filter(k -> k.letter().equals(letter))
                 .mapToInt(Rating::value)
                 .findFirst()
                 .orElseThrow();
}

static int sumFromKToN(int a, int b) { return a == b ? a : (b - a + 1) * (a + b) / 2; }

record Workflow(String name, Rule[] rules) {}
record Rule(Predicate<Rating[]> condition, String result) {}
record Rating(String letter, int value) {}
record Range(int begin, int end) {}