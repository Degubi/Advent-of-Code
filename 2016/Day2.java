static void main() throws Exception {
    var instructions = Files.readAllLines(Path.of("input.txt"));
    var part1Result = evaluateInstructions((value, k) -> applyMovePart1(value, k), instructions);
    var part2Result = evaluateInstructions((value, k) -> applyMovePart2(value, k), instructions);

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static String evaluateInstructions(IntBinaryOperator moveApplier, List<String> instructions) {
    return Stream.iterate(new State(0, 5), k -> applyNextInstruction(k, moveApplier, instructions))
                 .skip(1)
                 .takeWhile(Objects::nonNull)
                 .map(k -> Integer.toString(k.buttonValue, 16))
                 .collect(Collectors.joining())
                 .toUpperCase();

}

static State applyNextInstruction(State state, IntBinaryOperator moveApplier, List<String> instructions) {
    return state.instructionIndex >= instructions.size() ? null : new State(
        state.instructionIndex + 1,
        instructions.get(state.instructionIndex)
                    .chars()
                    .reduce(state.buttonValue, moveApplier));
}

static int applyMovePart1(int value, int direction) {
    return switch(direction) {
        case 'U' -> value <= 3 ? value : value - 3;
        case 'D' -> value >= 7 ? value : value + 3;
        case 'L' -> value % 3 == 1 ? value : value - 1;
        case 'R' -> value % 3 == 0 ? value : value + 1;
        default  -> -1;
    };
}

static int applyMovePart2(int value, int direction) {
    return switch(direction) {
        case 'U' -> value - switch(value) {
            case 3 -> 2;
            case 6, 7, 8 -> 4;
            case 10, 11, 12 -> 4;
            case 13 -> 2;
            default -> 0;
        };
        case 'D' -> value + switch(value) {
            case 1 -> 2;
            case 2, 3, 4 -> 4;
            case 6, 7, 8 -> 4;
            case 11 -> 2;
            default -> 0;
        };
        case 'L' -> value == 1 || value == 2 || value == 5 || value == 10 || value == 13 ? value : value - 1;
        case 'R' -> value == 1 || value == 4 || value == 9 || value == 12 || value == 13 ? value : value + 1;
        default  -> -1;
    };
}

record State(int instructionIndex, int buttonValue) {}
