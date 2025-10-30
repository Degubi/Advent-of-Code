static void main() throws Exception {
    var instructions = Files.lines(Path.of("input.txt"))
                            .map(k -> k.split(" ", 2))
                            .map(k -> new Instruction(k[0], Integer.parseInt(k[1])))
                            .toArray(Instruction[]::new);

    var part1Stats = evaluateInstructions(instructions, (k, l) -> applyPart1Instruction(k, l));
    var part2Stats = evaluateInstructions(instructions, (k, l) -> applyPart2Instruction(k, l));
    var part1Result = part1Stats.horizontal() * part1Stats.depth();
    var part2Result = part2Stats.horizontal() * part2Stats.depth();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Stats evaluateInstructions(Instruction[] instructions, BiFunction<Stats, Instruction, Stats> evaluator) {
    return Arrays.stream(instructions)
                 .reduce(new Stats(0, 0, 0), evaluator, (_, l) -> l);
}

static Stats applyPart1Instruction(Stats currentStats, Instruction instruction) {
    return switch(instruction.direction()) {
        case "forward" -> new Stats(currentStats.horizontal() + instruction.value(), currentStats.depth(), 0);
        case "down"    -> new Stats(currentStats.horizontal(), currentStats.depth() + instruction.value(), 0);
        case "up"      -> new Stats(currentStats.horizontal(), currentStats.depth() - instruction.value(), 0);
        default        -> currentStats;
    };
}

static Stats applyPart2Instruction(Stats currentStats, Instruction instruction) {
    return switch(instruction.direction()) {
        case "forward" -> new Stats(currentStats.horizontal() + instruction.value(), currentStats.depth() + currentStats.aim() * instruction.value(), currentStats.aim());
        case "down"    -> new Stats(currentStats.horizontal(), currentStats.depth(), currentStats.aim() + instruction.value());
        case "up"      -> new Stats(currentStats.horizontal(), currentStats.depth(), currentStats.aim() - instruction.value());
        default        -> currentStats;
    };
}

record Instruction(String direction, int value) {}
record Stats(int horizontal, int depth, int aim) {}