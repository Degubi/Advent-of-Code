static void main() throws Exception {
    var instructions = Files.readString(Path.of("input.txt")).split(", ");

    var positions = Stream.iterate(new State[] { new State(0, 0, 0, 0) }, k -> updateState(k[k.length - 1], instructions))
                          .takeWhile(Objects::nonNull)
                          .flatMap(Arrays::stream)
                          .map(k -> new Position(k.x, k.y))
                          .collect(Collectors.toList());

    var lastPosition = positions.getLast();
    var part1Result = Math.abs(lastPosition.x) + Math.abs(lastPosition.y);
    var part2Result = positions.stream()
                               .filter(k -> Collections.frequency(positions, k) > 1)
                               .findFirst()
                               .map(k -> Math.abs(k.x) + Math.abs(k.y))
                               .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static State[] updateState(State currentState, String[] instructions) {
    if(currentState.instructionIndex == instructions.length) return null;

    var instruction = instructions[currentState.instructionIndex];
    var direction = instruction.charAt(0);
    var count = Integer.parseInt(instruction, 1, instruction.length(), 10);
    var newRotation = (currentState.rotation + (direction == 'R' ? 90 : -90) + 360) % 360;

    return IntStream.range(0, count)
                    .mapToObj(i -> new State(
                        currentState.x + (newRotation == 0 ? (i + 1) : newRotation == 180 ? -(i + 1) : 0),
                        currentState.y + (newRotation == 90 ? (i + 1) : newRotation == 270 ? -(i + 1) : 0),
                        newRotation,
                        currentState.instructionIndex + 1
                    ))
                    .toArray(State[]::new);
}

record State(int x, int y, int rotation, int instructionIndex) {}
record Position(int x, int y) {}
