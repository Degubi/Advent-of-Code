static void main() throws Exception {
    var instructions = Files.readAllLines(Path.of("input.txt"));
    var part1Result = interpretInstructions(0, 0, 0, 0, instructions).a;
    var part2Result = interpretInstructions(0, 0, 1, 0, instructions).a;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static State interpretInstructions(int a, int b, int c, int d, List<String> instructions) {
    return Stream.iterate(new State(a, b, c, d, 0), k -> applyInstruction(k, instructions.get(k.instructionIndex)))
                 .dropWhile(k -> k.instructionIndex < instructions.size())
                 .findFirst()
                 .orElseThrow();
}

static State applyInstruction(State state, String instruction) {
    var instructionArgs = instruction.split(" ");

    return switch(instructionArgs[0]) {
        case "cpy" -> {
            var valueFirstChar = instructionArgs[1].charAt(0);
            var value = Character.isLetter(valueFirstChar) ? getRegisterValue(valueFirstChar, state) : Integer.parseInt(instructionArgs[1]);

            yield updateRegisterValue(instructionArgs[2].charAt(0), k -> value, state);
        }
        case "inc" -> updateRegisterValue(instructionArgs[1].charAt(0), k -> k + 1, state);
        case "dec" -> updateRegisterValue(instructionArgs[1].charAt(0), k -> k - 1, state);
        case "jnz" -> {
            var offsetToApply = Integer.parseInt(instructionArgs[2]);

            yield new State(
                state.a,
                state.b,
                state.c,
                state.d,
                state.instructionIndex + (getRegisterValue(instructionArgs[1].charAt(0), state) != 0 ? offsetToApply : 1)
            );
        }
        default -> null;
    };
}

static int getRegisterValue(char registerLetter, State state) {
    return registerLetter == 'a' ? state.a : registerLetter == 'b' ? state.b : registerLetter == 'c' ? state.c : state.d;
}

static State updateRegisterValue(char registerLetter, IntUnaryOperator transformer, State state) {
    return new State(
        registerLetter == 'a' ? transformer.applyAsInt(state.a) : state.a,
        registerLetter == 'b' ? transformer.applyAsInt(state.b) : state.b,
        registerLetter == 'c' ? transformer.applyAsInt(state.c) : state.c,
        registerLetter == 'd' ? transformer.applyAsInt(state.d) : state.d,
        state.instructionIndex + 1
    );
}

record State(int a, int b, int c, int d, int instructionIndex) {}
