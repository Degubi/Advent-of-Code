static void main() throws Exception {
    var parseResults = Files.lines(Path.of("input.txt"))
                            .map(k -> parseLine(k))
                            .collect(Collectors.partitioningBy(k -> k.stopIndex() == -1));

    var part1Result = parseResults.get(false).stream()
                                  .mapToInt(k -> getIllegalCharacterValue(k.line().charAt(k.stopIndex())))
                                  .sum();

    var part2Result = parseResults.get(true).stream()
                                  .mapToLong(k -> getAutocompleteScore(k))
                                  .sorted()
                                  .toArray();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result[part2Result.length / 2]);
}

static long getAutocompleteScore(LineParseResult parseResult) {
    return parseResult.parseState().reversed().stream()
                      .mapToLong(k -> getMissingCharacterValue(k))
                      .reduce(0, (res, k) -> res * 5 + k);
}

static int getIllegalCharacterValue(char character) {
    return switch(character) {
        case ')'  -> 3;
        case ']'  -> 57;
        case '}'  -> 1197;
        case '>'  -> 25137;
        default   -> 0;
    };
}

static int getMissingCharacterValue(char character) {
    return switch(character) {
        case '(' -> 1;
        case '[' -> 2;
        case '{' -> 3;
        case '<' -> 4;
        default  -> 0;
    };
}

static LineParseResult parseLine(String line) {
    var parseState = new ArrayList<Character>();

    for(var i = 0; i < line.length(); ++i) {
        var currentChar = line.charAt(i);

        if(currentChar == '(' || currentChar == '[' || currentChar == '{' || currentChar == '<') {
            parseState.add(currentChar);
        }else{
            var lastChar = parseState.getLast();

            if(doPairsMatch(lastChar, currentChar)) {
                parseState.removeLast();
            }else{
                return new LineParseResult(line, parseState, i);
            }
        }
    }

    return new LineParseResult(line, parseState, -1);
}

static boolean doPairsMatch(char opening, char closing) {
    return opening == '(' && closing == ')' ||
           opening == '{' && closing == '}' ||
           opening == '[' && closing == ']' ||
           opening == '<' && closing == '>';
}

record LineParseResult(String line, ArrayList<Character> parseState, int stopIndex) {}