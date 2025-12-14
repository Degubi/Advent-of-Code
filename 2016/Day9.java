static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));

    var part1Result = Stream.iterate(new State(0, 0), k -> k.positionIndex >= input.length() ? null : generateNextPart1State(k, input))
                            .takeWhile(Objects::nonNull)
                            .reduce((_, l) -> l)
                            .orElseThrow()
                            .decompressedLength;

    var part2Result = 0; // FIXME

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static State generateNextPart1State(State currentState, String input) {
    var currentIndex = currentState.positionIndex;
    var currentCharacter = input.charAt(currentIndex);

    if(currentCharacter == '(') {
        var xIndex = input.indexOf('x', currentIndex + 1);
        var closingParenIndex = input.indexOf(')', currentIndex + 1);
        var charRepeatLength = Integer.parseInt(input, currentIndex + 1, xIndex, 10);
        var repeatCount = Integer.parseInt(input, xIndex + 1, closingParenIndex, 10);
        var jumpCount = charRepeatLength * repeatCount;

        return new State(closingParenIndex + 1 + charRepeatLength, currentState.decompressedLength + jumpCount);
    }

    return new State(currentIndex + 1, currentState.decompressedLength + 1);
}

record State(int positionIndex, int decompressedLength) {}