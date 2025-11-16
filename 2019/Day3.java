static void main() throws Exception {
    var input = Files.readAllLines(Path.of("input.txt"));
    var firstWirePointsList = generatePaths(input.getFirst());
    var secondWirePointsList = generatePaths(input.getLast());
    var secondWirePointsSet = new HashSet<>(secondWirePointsList); // Need this for fast search
    var intersectionPoints = firstWirePointsList.stream()
                                                .filter(secondWirePointsSet::contains)
                                                .toArray(Point[]::new);

    var part1Result = Arrays.stream(intersectionPoints)
                            .mapToInt(k -> Math.abs(k.x) + Math.abs(k.y))
                            .min()
                            .orElseThrow();

    var part2Result = Arrays.stream(intersectionPoints)
                            .mapToInt(k -> firstWirePointsList.indexOf(k) + 1 + secondWirePointsList.indexOf(k) + 1) // + 1 because we skipped the first state
                            .min()
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static List<Point> generatePaths(String instructionString) {
    var instructions = instructionString.split(",");

    return Stream.iterate(new State[] { new State(0, new Point(0, 0)) }, k -> k[k.length - 1].instructionIndex == instructions.length ? null : applyInstruction(k[k.length -1], instructions))
                 .skip(1)
                 .takeWhile(Objects::nonNull)
                 .flatMap(Arrays::stream)
                 .map(State::position)
                 .collect(Collectors.toList());
}

static State[] applyInstruction(State currentState, String[] instructions) {
    var instruction = instructions[currentState.instructionIndex];
    var direction = instruction.charAt(0);
    var count = Integer.parseInt(instruction, 1, instruction.length(), 10);
    var xOffset = direction == 'L' ? -1 : direction == 'R' ? 1 : 0;
    var yOffset = direction == 'U' ? 1 : direction == 'D' ? -1 : 0;

    return IntStream.rangeClosed(1, count)
                    .mapToObj(i -> new State(currentState.instructionIndex + 1, new Point(currentState.position.x + xOffset * i, currentState.position.y + yOffset * i)))
                    .toArray(State[]::new);
}

record Point(int x, int y) {}
record State(int instructionIndex, Point position) {}
