import java.nio.file.*;
import java.util.stream.*;

public class Day23 {
    record Instruction(String name, String[] args) {}
    record ProgramState(int a, int b, int instructionIndex) {}


    public static void main(String[] args) throws Exception {
        var instructions = Files.lines(Path.of("Day23.txt"))
                                .map(Day23::parseInstruction)
                                .toArray(Instruction[]::new);

        var part1Result = interpretInstructions(0, 0, instructions).b();
        var part2Result = interpretInstructions(1, 0, instructions).b();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static ProgramState interpretInstructions(int a, int b, Instruction[] instructions) {
        return Stream.iterate(new ProgramState(a, b, 0), k -> updateProgramState(k, instructions))
                     .dropWhile(k -> k.instructionIndex() < instructions.length)
                     .findFirst()
                     .orElseThrow();
    }

    static ProgramState updateProgramState(ProgramState currentState, Instruction[] instructions) {
        var instructionIndex = currentState.instructionIndex();
        var instruction = instructions[instructionIndex];

        var args = instruction.args();
        var nextInstructionIndex = switch(instruction.name()) {
            case "jmp" -> instructionIndex + Integer.parseInt(args[0]);
            case "jie" -> instructionIndex + ((args[0].equals("a") ? currentState.a() : currentState.b()) % 2 == 0 ? Integer.parseInt(args[1]) : 1);
            case "jio" -> instructionIndex + ((args[0].equals("a") ? currentState.a() : currentState.b()) == 1 ? Integer.parseInt(args[1]) : 1);
            default    -> instructionIndex + 1;
        };

        return new ProgramState(
            getUpdatedRegisterValue("a", currentState.a(), instruction),
            getUpdatedRegisterValue("b", currentState.b(), instruction),
            nextInstructionIndex
        );
    }

    static int getUpdatedRegisterValue(String registerName, int value, Instruction instruction) {
        return !instruction.args()[0].equals(registerName) ? value : switch(instruction.name()) {
            case "hlf" -> value / 2;
            case "tpl" -> value * 3;
            case "inc" -> value + 1;
            default    -> value;
        };
    }

    static Instruction parseInstruction(String line) {
        var nameSeparatorIndex = line.indexOf(' ');

        return new Instruction(line.substring(0, nameSeparatorIndex), line.substring(nameSeparatorIndex + 1).split(", "));
    }
}