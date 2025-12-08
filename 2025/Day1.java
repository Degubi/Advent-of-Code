static void main() throws Exception {
    var instructions = Files.readAllLines(Path.of("input.txt"));
    var states = Stream.iterate(new State(50, 0, 0), k -> k.instructionIndex == instructions.size() ? null : applyInstruction(k, instructions))
                       .takeWhile(Objects::nonNull)
                       .toArray(State[]::new);

    var part1Result = Arrays.stream(states)
                            .mapToInt(State::value)
                            .filter(k -> k % 100 == 0)
                            .count();

    var part2Result = Arrays.stream(states)
                            .mapToInt(State::rotations)
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static State applyInstruction(State state, List<String> instructions) {
    var instruction = instructions.get(state.instructionIndex);
    var rotationCount = Integer.parseInt(instruction, 1, instruction.length(), 10);
    var directionMultiplier = instruction.charAt(0) == 'L' ? -1 : 1;
    var previousValue = state.value;
    var nextValue = previousValue + rotationCount * directionMultiplier;
    var rotations = Math.floorDiv(directionMultiplier * nextValue, 100) - Math.floorDiv(directionMultiplier * previousValue, 100);

    return new State(nextValue, state.instructionIndex + 1, rotations);
}

record State(int value, int instructionIndex, int rotations) {}
