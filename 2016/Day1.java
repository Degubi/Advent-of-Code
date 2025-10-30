static void main() throws Exception {
    var instructions = Files.readString(Path.of("input.txt")).split(", ");

    var states = Stream.iterate(new State(0, 0, 0, 0), k -> updateState(k, instructions))
                       .takeWhile(Objects::nonNull)
                       .toArray(State[]::new);

    var lastState = states[states.length - 1];
    var part1Result = Math.abs(lastState.x) + Math.abs(lastState.y);

    // TODO: Part 2
    System.out.println("Result 1: " + part1Result);
}

static State updateState(State currentState, String[] instructions) {
    if(currentState.instructionIndex == instructions.length) return null;

    var instruction = instructions[currentState.instructionIndex];
    var direction = instruction.charAt(0);
    var count = Integer.parseInt(instruction, 1, instruction.length(), 10);
    var newRotation = (currentState.rotation + (direction == 'R' ? 90 : -90) + 360) % 360;

    return new State(
        currentState.x + (newRotation == 0 ? count : newRotation == 180 ? -count : 0),
        currentState.y + (newRotation == 90 ? count : newRotation == 270 ? -count : 0),
        newRotation,
        currentState.instructionIndex + 1
    );
}

record State(int x, int y, int rotation, int instructionIndex) {}
