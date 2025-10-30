static void main() throws Exception {
    var instructions = Files.lines(Path.of("input.txt"))
                            .map(k -> new Instruction(k.substring(0, k.indexOf(' ')), Integer.parseInt(k, k.indexOf(' ') + 1, k.length(), 10)))
                            .toArray(Instruction[]::new);

    var part1Result = execute(instructions).accumulator();
    var part2Result = Stream.iterate(new TerminatedCorrectlySearchState(0, Arrays.copyOf(instructions, instructions.length), new ExecutionResult(false, -1)), k -> updateSearchState(k, instructions))
                            .dropWhile(k -> !k.lastExecutionResult().terminatedCorrectly())
                            .findFirst()
                            .orElseThrow()
                            .lastExecutionResult().accumulator();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static ExecutionResult execute(Instruction[] instructions) {
    var finalState = Stream.iterate(new InterpreterState(0, 0, new ArrayList<>()), k -> applyInstruction(k, instructions[k.instructionIndex()]))
                           .dropWhile(k -> !k.alreadyRanInstructionIndices().contains(k.instructionIndex()) && k.instructionIndex() != instructions.length - 1)
                           .findFirst()
                           .orElseThrow();

    return new ExecutionResult(finalState.instructionIndex() == instructions.length - 1, finalState.accumulator());
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

static TerminatedCorrectlySearchState updateSearchState(TerminatedCorrectlySearchState state, Instruction[] instructions) {
    var modifiedInstructions = state.modifiedInstructions();
    var modifiedInstructionIndex = state.modifiedInstructionIndex();
    var currentModificationIndex = modifiedInstructionIndex + 1;

    modifiedInstructions[modifiedInstructionIndex] = instructions[modifiedInstructionIndex];
    modifiedInstructions[currentModificationIndex] = switch(modifiedInstructions[currentModificationIndex].name()) {
        case "nop" -> new Instruction("jmp", modifiedInstructions[currentModificationIndex].value());
        case "acc" -> modifiedInstructions[currentModificationIndex];
        case "jmp" -> new Instruction("nop", modifiedInstructions[currentModificationIndex].value());
        default    -> null;
    };

    return new TerminatedCorrectlySearchState(currentModificationIndex, modifiedInstructions, execute(modifiedInstructions));
}

record Instruction(String name, int value) {}
record InterpreterState(int accumulator, int instructionIndex, ArrayList<Integer> alreadyRanInstructionIndices) {}
record ExecutionResult(boolean terminatedCorrectly, int accumulator) {}
record TerminatedCorrectlySearchState(int modifiedInstructionIndex, Instruction[] modifiedInstructions, ExecutionResult lastExecutionResult) {}
