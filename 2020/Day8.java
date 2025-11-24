static void main() throws Exception {
    var instructions = Files.lines(Path.of("input.txt"))
                            .map(k -> new Instruction(k.substring(0, k.indexOf(' ')), Integer.parseInt(k, k.indexOf(' ') + 1, k.length(), 10)))
                            .toArray(Instruction[]::new);

    var part1Result = execute(instructions).accumulator;
    var part2Result = IntStream.range(0, instructions.length)
                               .mapToObj(i -> execute(replaceInstruction(i, instructions)))
                               .filter(ExecutionResult::terminatedCorrectly)
                               .findFirst()
                               .orElseThrow()
                               .accumulator;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static ExecutionResult execute(Instruction[] instructions) {
    var finalState = Stream.iterate(new InterpreterState(0, 0, new ArrayList<>()), k -> k.instructionIndex == instructions.length ? null : applyInstruction(k, instructions[k.instructionIndex()]))
                           .takeWhile(k -> k != null && !k.alreadyRanInstructionIndices().contains(k.instructionIndex()))
                           .reduce((_, l) -> l)
                           .orElseThrow();

    return new ExecutionResult(finalState.instructionIndex == instructions.length, finalState.accumulator);
}

static Instruction[] replaceInstruction(int currentModificationIndex, Instruction[] instructions) {
    var modifiedInstructions = Arrays.copyOf(instructions, instructions.length);

    modifiedInstructions[currentModificationIndex] = switch(modifiedInstructions[currentModificationIndex].name()) {
        case "nop" -> new Instruction("jmp", modifiedInstructions[currentModificationIndex].value());
        case "acc" -> modifiedInstructions[currentModificationIndex];
        case "jmp" -> new Instruction("nop", modifiedInstructions[currentModificationIndex].value());
        default    -> null;
    };

    return modifiedInstructions;
}

static InterpreterState applyInstruction(InterpreterState state, Instruction instruction) {
    var alreadyRanInstructionIndices = state.alreadyRanInstructionIndices();
    var accumulator = state.accumulator();
    var instructionIndex = state.instructionIndex();

    alreadyRanInstructionIndices.add(state.instructionIndex());

    return switch(instruction.name()) {
        case "nop" -> new InterpreterState(accumulator, instructionIndex + 1, alreadyRanInstructionIndices);
        case "acc" -> new InterpreterState(accumulator + instruction.value(), instructionIndex + 1, alreadyRanInstructionIndices);
        case "jmp" -> new InterpreterState(accumulator, instructionIndex + instruction.value(), alreadyRanInstructionIndices);
        default    -> null;
    };
}

record Instruction(String name, int value) {}
record InterpreterState(int accumulator, int instructionIndex, ArrayList<Integer> alreadyRanInstructionIndices) {}
record ExecutionResult(boolean terminatedCorrectly, int accumulator) {}
