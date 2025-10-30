static final Pattern CONSTANT_OP_REGEX = Pattern.compile("^([a-z]+|\\d+) -> ([a-z]+)$");
static final Pattern UNARY_OP_REGEX = Pattern.compile("^([A-Z]+) ([a-z]+|\\d+) -> ([a-z]+)$");
static final Pattern BINARY_OP_REGEX = Pattern.compile("^([a-z]+|\\d+) ([A-Z]+) ([a-z]+|\\d+) -> ([a-z]+)$");

static void main() throws Exception {
    var operations = Files.lines(Path.of("input.txt"))
                          .map(k -> parseOperation(k))
                          .toArray(Operation[]::new);

    var part1Variables = calculateVariables(extractOperationVariables(operations, k -> null), operations);
    var part1Result = part1Variables.get("a");
    var part2Variables = calculateVariables(extractOperationVariables(operations, k -> k.equals("b") ? part1Result : null), operations);
    var part2Result = part2Variables.get("a");

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static HashMap<String, Integer> extractOperationVariables(Operation[] operations, Function<String, Integer> valueMapper) {
    return Arrays.stream(operations)
                 .flatMap(k -> extractVariables(k))
                 .distinct()
                 .collect(HashMap<String, Integer>::new, (m, k) -> m.put(k, valueMapper.apply(k)), HashMap::putAll);
}

static HashMap<String, Integer> calculateVariables(HashMap<String, Integer> variables, Operation[] operations) {
    return Stream.iterate(variables, k -> Arrays.stream(operations).reduce(k, (l, m) -> updateVariables(l, m), (_, l) -> l))
                 .dropWhile(k -> k.values().stream().anyMatch(Objects::isNull))
                 .findFirst()
                 .orElseThrow();
}

static HashMap<String, Integer> updateVariables(HashMap<String, Integer> variables, Operation operation) {
    variables.computeIfAbsent(operation.resultName(), _ -> switch(operation) {
        case StoreOperation  k -> getArgumentValue(k.value(), variables);
        case UnaryOperation  k -> tryApplyUnaryOperation(variables, k);
        case BinaryOperation k -> tryApplyBinaryOperation(variables, k);
    });

    return variables;
}

static Integer getArgumentValue(Argument argument, Map<String, Integer> variables) {
    return switch(argument) {
        case Variable k -> variables.get(k.name());
        case Constant k -> k.value();
    };
}

static Integer tryApplyUnaryOperation(Map<String, Integer> variables, UnaryOperation operation) {
    var arg1Value = getArgumentValue(operation.arg(), variables);

    return arg1Value == null ? null : switch(operation.name()) {
        case "NOT" -> Short.toUnsignedInt((short) ~arg1Value);
        default    -> null;
    };
}

static Integer tryApplyBinaryOperation(Map<String, Integer> variables, BinaryOperation operation) {
    var arg1Value = getArgumentValue(operation.arg1(), variables);
    var arg2Value = getArgumentValue(operation.arg2(), variables);

    return arg1Value == null || arg2Value == null ? null : switch(operation.name()) {
        case "AND"    -> arg1Value & arg2Value;
        case "OR"     -> arg1Value | arg2Value;
        case "LSHIFT" -> arg1Value << arg2Value;
        case "RSHIFT" -> arg1Value >> arg2Value;
        default       -> null;
    };
}

static Stream<String> extractVariables(Operation operation) {
    return switch(operation) {
        case StoreOperation    k -> Stream.of(k.resultName());
        case UnaryOperation    k when k.arg() instanceof Variable v -> Stream.of(v.name(), k.resultName());
        case UnaryOperation    k -> Stream.of(k.resultName());
        case BinaryOperation   k when k.arg1() instanceof Variable v1 && k.arg2() instanceof Variable v2 -> Stream.of(v1.name(), v2.name(), k.resultName());
        case BinaryOperation   k when k.arg1() instanceof Variable v1 -> Stream.of(v1.name(), k.resultName());
        case BinaryOperation   k when k.arg2() instanceof Variable v2 -> Stream.of(v2.name(), k.resultName());
        case BinaryOperation   k -> Stream.of(k.resultName());
    };
}

static Operation parseOperation(String line) {
    var constantOpMatcher = CONSTANT_OP_REGEX.matcher(line);
    if(constantOpMatcher.find()) {
        return new StoreOperation(parseArgument(constantOpMatcher.group(1)), constantOpMatcher.group(2));
    }

    var unaryOpMatcher = UNARY_OP_REGEX.matcher(line);
    if(unaryOpMatcher.find()) {
        return new UnaryOperation(unaryOpMatcher.group(1), parseArgument(unaryOpMatcher.group(2)), unaryOpMatcher.group(3));
    }

    var binaryOpMatcher = BINARY_OP_REGEX.matcher(line);
    binaryOpMatcher.find();

    return new BinaryOperation(
        binaryOpMatcher.group(2),
        parseArgument(binaryOpMatcher.group(1)), parseArgument(binaryOpMatcher.group(3)),
        binaryOpMatcher.group(4)
    );
}

static Argument parseArgument(String value) {
    try {
        return new Constant(Integer.parseInt(value));
    }catch(NumberFormatException e) {
        return new Variable(value);
    }
}

sealed interface Operation {
    String resultName();
}

sealed interface Argument {}
record Variable(String name) implements Argument {}
record Constant(int value) implements Argument {}

record StoreOperation(Argument value, String resultName) implements Operation {}
record UnaryOperation(String name, Argument arg, String resultName) implements Operation {}
record BinaryOperation(String name, Argument arg1, Argument arg2, String resultName) implements Operation {}