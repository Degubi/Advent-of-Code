static void main() throws Exception {
    var machines = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
                         .map(k -> parseMachine(k))
                         .toArray(Machine[]::new);

    var part1Result = Arrays.stream(machines)
                            .mapToInt(k -> calculateMinimalTokensForWin(k, 100))
                            .sum();

    var part2Result = 0; // FIXME, looks like a linear algebra problem, way too tired to look for a library. Might solve later

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Machine parseMachine(String data) {
    var lines = data.split("\n", 3);
    var aLine = lines[0];
    var axOffset = aLine.indexOf('X');
    var ayOffset = aLine.indexOf('Y');
    var bLine = lines[1];
    var bxOffset = bLine.indexOf('X');
    var byOffset = bLine.indexOf('Y');
    var pLine = lines[2];
    var pxOffset = pLine.indexOf('X');
    var pyOffset = pLine.indexOf('Y');

    return new Machine(
        Integer.parseInt(aLine, axOffset + 2, ayOffset - 2, 10),
        Integer.parseInt(aLine, ayOffset + 2, aLine.length(), 10),
        Integer.parseInt(bLine, bxOffset + 2, byOffset - 2, 10),
        Integer.parseInt(bLine, byOffset + 2, bLine.length(), 10),
        Integer.parseInt(pLine, pxOffset + 2, pyOffset - 2, 10),
        Integer.parseInt(pLine, pyOffset + 2, pLine.length(), 10)
    );
}

static int calculateMinimalTokensForWin(Machine machine, int pressLimit) {
    return IntStream.rangeClosed(0, pressLimit).mapToObj(a -> IntStream.rangeClosed(0, pressLimit).mapToObj(b -> new Combination(a, b)))
                    .flatMap(k -> k)
                    .filter(k -> (k.aCount * machine.ax + k.bCount * machine.bx) == machine.px && (k.aCount * machine.ay + k.bCount * machine.by) == machine.py)
                    .mapToInt(k -> k.aCount * 3 + k.bCount)
                    .min()
                    .orElse(0);
}

record Machine(int ax, int ay, int bx, int by, long px, long py) {}
record Combination(int aCount, int bCount) {}
