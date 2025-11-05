static void main() throws Exception {
    var abbaRegex = Pattern.compile("([a-z])([a-z])(?!\\1)\\2\\1").asPredicate();
    var abaRegex = Pattern.compile("(?=(?:([a-z])(?!\\1)([a-z])\\1))");

    var ips = Files.lines(Path.of("input.txt"))
                   .map(k -> parseIp(k))
                   .toArray(Ip[]::new);

    var part1Result = Arrays.stream(ips)
                           .filter(ip -> Arrays.stream(ip.inBracketParts).noneMatch(abbaRegex::test) && Arrays.stream(ip.outsideBracketParts).anyMatch(abbaRegex::test))
                           .count();

    var part2Result = Arrays.stream(ips)
                            .filter(ip -> {
                                return Arrays.stream(ip.outsideBracketParts)
                                             .flatMap(k -> abaRegex.matcher(k).results())
                                             .anyMatch(k -> {
                                                 var firstChar = k.group(1);
                                                 var secondChar = k.group(2);
                                                 var bab = secondChar + firstChar + secondChar;

                                                 return Arrays.stream(ip.inBracketParts)
                                                              .anyMatch(b -> b.contains(bab));
                                             });
                            })
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Ip parseIp(String line) {
    var bracketIndices = IntStream.range(0, line.length())
                                  .filter(i -> line.charAt(i) == '[' || line.charAt(i) == ']')
                                  .toArray();

    var inBracketParts = IntStream.iterate(0, i -> i < bracketIndices.length, i -> i + 2)
                                  .mapToObj(i -> line.substring(bracketIndices[i] + 1, bracketIndices[i + 1]))
                                  .toArray(String[]::new);

    var outBracketParts = IntStream.concat(IntStream.of(-1), IntStream.concat(IntStream.of(bracketIndices), IntStream.of(line.length())))
                                   .boxed()
                                   .gather(Gatherers.windowFixed(2))
                                   .map(k -> line.substring(k.getFirst() + 1, k.getLast()))
                                   .toArray(String[]::new);

    return new Ip(inBracketParts, outBracketParts);
}

record Ip(String[] inBracketParts, String[] outsideBracketParts) {}
