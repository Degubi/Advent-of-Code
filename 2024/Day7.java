static void main() throws Exception {
    var equations = Files.lines(Path.of("input.txt"))
                         .map(k -> parseEquation(k))
                         .toArray(Equation[]::new);

    var part1Operators = new BiLongToLongFunction[] { (v1, v2) -> v1 + v2, (v1, v2) -> v1 * v2 };
    var part1Result = Arrays.stream(equations)
                            .filter(k -> isEquationSolvable(k, part1Operators))
                            .mapToLong(Equation::result)
                            .sum();

    var part2Operators = new BiLongToLongFunction[] { (v1, v2) -> v1 + v2, (v1, v2) -> v1 * v2, (v1, v2) -> Long.parseLong(new StringBuilder().append(v1).append(v2).toString()) };
    var part2Result = Arrays.stream(equations).parallel()
                            .filter(k -> isEquationSolvable(k, part2Operators))
                            .mapToLong(Equation::result)
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Equation parseEquation(String line) {
    var colonIndex = line.indexOf(':');
    var values = Arrays.stream(line.substring(colonIndex + 2).split(" "))
                       .mapToLong(Long::parseLong)
                       .toArray();

    return new Equation(Long.parseLong(line, 0, colonIndex, 10), values);
}

static<T> List<List<T>> createPermutations(T[] elements, int length) {
    var fullPermutations = new ArrayList<List<T>>();
    createPermutations(elements, fullPermutations, new ArrayList<>(), length);
    return fullPermutations;
}

static<T> void createPermutations(T[] elements, List<List<T>> fullPermutations, List<T> current, int remaining) {
    if(remaining == 0) {
        fullPermutations.add(new ArrayList<>(current));
        return;
    }

    for(var element : elements) {
        current.add(element);
        createPermutations(elements, fullPermutations, current, remaining - 1);
        current.remove(current.size() - 1);
    }
}

static boolean isEquationSolvable(Equation equation, BiLongToLongFunction[] possibleOperators) {
    return createPermutations(possibleOperators, equation.values.length).stream()
                                                                        .anyMatch(k -> isEquationCorrect(equation, k));
}

static boolean isEquationCorrect(Equation equation, List<BiLongToLongFunction> operators) {
    return LongStream.range(1, equation.values.length)
                     .reduce(equation.values[0], (result, i) -> operators.get((int) i - 1).apply(result, equation.values[(int) i])) == equation.result;
}

record Equation(long result, long[] values) {}
interface BiLongToLongFunction {
    long apply(long v1, long v2);
}
