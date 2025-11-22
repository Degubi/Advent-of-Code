static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var part1Result = fullReact(input).length();
    var part2Result = IntStream.rangeClosed('a', 'z')
                               .mapToObj(k -> input.replace(Character.toString(k), "").replace(Character.toString(Character.toUpperCase(k)), ""))
                               .map(k -> fullReact(k))
                               .min(Comparator.comparingInt(StringBuilder::length))
                               .orElseThrow()
                               .length();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static StringBuilder fullReact(String input) {
    return Stream.iterate(new State(new StringBuilder(input), 0), k -> doReact(k))
                 .takeWhile(Objects::nonNull)
                 .min(Comparator.comparingInt(k -> k.text.length()))
                 .map(State::text)
                 .orElseThrow();
}

static State doReact(State state) {
    var index = state.index;
    var text = state.text;

    if(index == text.length() - 1) {
        return null;
    }

    var currentChar = text.charAt(index);
    var nextChar = text.charAt(index + 1);

    return Character.isUpperCase(currentChar) ^ Character.isUpperCase(nextChar) && Character.toLowerCase(currentChar) == Character.toLowerCase(nextChar)
           ? new State(text.delete(index, index + 2), Math.max(0, index - 1))
           : new State(text, index + 1);
}

record State(StringBuilder text, int index) {}