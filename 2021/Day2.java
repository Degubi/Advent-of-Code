import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class Day2 {
    record Instruction(String direction, int value) {}
    record Stats(int horizontal, int depth, int aim) {}


    public static void main(String[] args) throws Exception {
        var instructions = Files.lines(Path.of("Day2.txt"))
                                .map(k -> k.split(" ", 2))
                                .map(k -> new Instruction(k[0], Integer.parseInt(k[1])))
                                .toArray(Instruction[]::new);

        var part1Stats = evaluateInstructions(instructions, Day2::applyPart1Instruction);
        var part2Stats = evaluateInstructions(instructions, Day2::applyPart2Instruction);
        var part1Result = part1Stats.horizontal() * part1Stats.depth();
        var part2Result = part2Stats.horizontal() * part2Stats.depth();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Stats evaluateInstructions(Instruction[] instructions, BiFunction<Stats, Instruction, Stats> evaluator) {
        return Arrays.stream(instructions)
                     .reduce(new Stats(0, 0, 0), evaluator, (k, l) -> l);
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
}