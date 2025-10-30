static void main() throws Exception {
    var stringEscapeRegex = Pattern.compile("\\\\(\\\\|\\\"|x[a-z0-9]{2})");
    var stringStats = Files.lines(Path.of("input.txt"))
                           .map(k -> createStringData(k, stringEscapeRegex))
                           .reduce(new StringData(0, 0, 0), (result, k) -> new StringData(
                                result.textCharacters() + k.textCharacters(),
                                result.escapedCharacters() + k.escapedCharacters(),
                                result.reEncodedCharacters() + k.reEncodedCharacters()
                           ), (_, l) -> l);

    var part1Result = stringStats.textCharacters() - stringStats.escapedCharacters();
    var part2Result = stringStats.reEncodedCharacters() - stringStats.textCharacters();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static StringData createStringData(String text, Pattern stringEscapeRegex) {
    var escapedText = stringEscapeRegex.matcher(text).replaceAll(k -> unescapeCapture(k.group(1)));
    var reEncodedText = "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";

    return new StringData(text.length(), escapedText.length() - 2, reEncodedText.length());
}

static String unescapeCapture(String text) {
    return text.equals("\\") ? "\\\\" :
           text.charAt(0) == 'x' ? Character.toString(Integer.parseInt(text, 1, text.length(), 16)) : text;
}

record StringData(int textCharacters, int escapedCharacters, int reEncodedCharacters) {}