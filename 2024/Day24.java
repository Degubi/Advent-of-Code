import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

public class Main {

    record Instruction(String inputWire1, String inputWire2, String operator, String outputWire) {}

    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");

        var instructions = input[1].lines()
                                   .map(k -> k.split(" "))
                                   .map(k -> new Instruction(k[0], k[2], k[1], k[4]))
                                   .toArray(Instruction[]::new);

        var wires = Arrays.stream(instructions).reduce(parseInitialGates(input[0]), (result, k) -> {
            computeWireValue(k.outputWire, instructions, result);
            return result;
        }, (k, l) -> l);

        var part1Result = wires.entrySet().stream()
                               .filter(e -> e.getKey().startsWith("z"))
                               .mapToLong(e -> (long) e.getValue() << Long.parseLong(e.getKey(), 1, 3, 10))
                               .reduce(0, (result, k) -> result | k);

        System.out.println("Result 1: " + part1Result); // TODO: Part 2
    }

    static int computeWireValue(String outputWire, Instruction[] instructions, Map<String, Integer> wires) {
        var cachedValue = wires.get(outputWire);

        if(cachedValue == null) {
            var instruction = findInstructionForOutput(outputWire, instructions);
            var wire1Value = computeWireValue(instruction.inputWire1, instructions, wires);
            var wire2Value = computeWireValue(instruction.inputWire2, instructions, wires);
            var value = switch(instruction.operator) {
                case "AND" -> wire1Value & wire2Value;
                case "OR"  -> wire1Value | wire2Value;
                case "XOR" -> wire1Value ^ wire2Value;
                default    -> Integer.MIN_VALUE;
            };

            wires.put(instruction.outputWire, value);
            return value;
        }

        return cachedValue;
    }

    static Instruction findInstructionForOutput(String outputWire, Instruction[] instructions) {
        return Arrays.stream(instructions)
                     .filter(k -> k.outputWire.equals(outputWire))
                     .findFirst()
                     .orElseThrow();
    }

    static Map<String, Integer> parseInitialGates(String gates) {
        return gates.lines()
                    .collect(Collectors.toMap(k -> k.substring(0, 3), k -> Character.getNumericValue(k.charAt(k.length() - 1))));
    }
}
