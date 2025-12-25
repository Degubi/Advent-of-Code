static void main() throws Exception {
    var instructions = Files.readAllLines(Path.of("input.txt"));
    var states = Stream.iterate(new State(new HashMap<>(), 0), k -> k.instructionIndex == instructions.size() ? null : new State(applyInstruction(k.registers, instructions.get(k.instructionIndex)), k.instructionIndex + 1))
                       .takeWhile(Objects::nonNull)
                       .map(State::registers)
                       .map(HashMap::values)
                       .map(k -> k.stream().mapToInt(Integer::intValue).toArray())
                       .toArray(int[][]::new);

    var part1Result = Arrays.stream(states[states.length - 1])
                            .max()
                            .orElseThrow();

    var part2Result = Arrays.stream(states)
                            .flatMapToInt(Arrays::stream)
                            .max()
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static HashMap<String, Integer> applyInstruction(HashMap<String, Integer> registers, String line) {
    var parts = line.split(" ");
    var result = new HashMap<>(registers);
    var valueToCheck = result.getOrDefault(parts[4], 0).intValue();
    var valueToCompare = Integer.parseInt(parts[6]);
    var checkPassed = switch(parts[5]) {
        case ">"  -> valueToCheck > valueToCompare;
        case ">=" -> valueToCheck >= valueToCompare;
        case "<"  -> valueToCheck < valueToCompare;
        case "<=" -> valueToCheck <= valueToCompare;
        case "==" -> valueToCheck == valueToCompare;
        case "!=" -> valueToCheck != valueToCompare;
        default -> false;
    };

    if(checkPassed) {
        var valueToSet = parts[0];
        var valueToModifyWith = Integer.parseInt(parts[2]);
        var valueToAdd = parts[1].equals("inc") ? valueToModifyWith : -valueToModifyWith;

        result.put(valueToSet, result.getOrDefault(valueToSet, 0) + valueToAdd);
    }

    return result;
}

record State(HashMap<String, Integer> registers, int instructionIndex) {}