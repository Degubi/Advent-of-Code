static void main() throws Exception {
    var instructionRegex = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");
    var instructions = Files.lines(Path.of("input.txt"))
                            .map(k -> parseInstruction(k, instructionRegex))
                            .toArray(Instruction[]::new);

    var part1Lights = Arrays.stream(instructions)
                            .reduce(new int[1000][1000], (k, l) -> applyPart1Intstruction(k, l), (_, l) -> l);

    var part1Result = Arrays.stream(part1Lights)
                            .flatMapToInt(Arrays::stream)
                            .filter(k -> k == 1)
                            .count();

    var part2Lights = Arrays.stream(instructions)
                            .reduce(new int[1000][1000], (k, l) -> applyPart2Intstruction(k, l), (_, l) -> l);

    var part2Result = Arrays.stream(part2Lights)
                            .flatMapToInt(Arrays::stream)
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int[][] applyPart1Intstruction(int[][] accumulator, Instruction instruction) {
    return switch(instruction.operation()) {
        case "turn on"  -> doOperation(k -> 1, accumulator, instruction);
        case "turn off" -> doOperation(k -> 0, accumulator, instruction);
        case "toggle"   -> doOperation(k -> k == 0 ? 1 : 0, accumulator, instruction);
        default         -> accumulator;
    };
}

static int[][] applyPart2Intstruction(int[][] accumulator, Instruction instruction) {
    return switch(instruction.operation()) {
        case "turn on"  -> doOperation(k -> k + 1, accumulator, instruction);
        case "turn off" -> doOperation(k -> Math.max(0, k - 1), accumulator, instruction);
        case "toggle"   -> doOperation(k -> k + 2, accumulator, instruction);
        default         -> accumulator;
    };
}

static int[][] doOperation(IntUnaryOperator valueTransformer, int[][] accumulator, Instruction instruction) {
    IntStream.rangeClosed(instruction.fromColumn(), instruction.toColumn())
             .mapToObj(k -> accumulator[k])
             .forEach(row -> IntStream.rangeClosed(instruction.fromRow(), instruction.toRow())
                                      .forEach(k -> row[k] = valueTransformer.applyAsInt(row[k])));
    return accumulator;
}

static Instruction parseInstruction(String line, Pattern instructionRegex) {
    var matcher = instructionRegex.matcher(line);
    matcher.find();

    return new Instruction(
        matcher.group(1),
        Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
        Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5))
    );
}

record Instruction(String operation, int fromColumn, int fromRow, int toColumn, int toRow) {}