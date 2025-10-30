static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt")).replace('\n', ' ');
    var operations = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)")
                            .matcher(input).results()
                            .map(k -> new MulOp(Integer.parseInt(k.group(1)), Integer.parseInt(k.group(2)), k.start()))
                            .toArray(MulOp[]::new);

    var parsedConditionals = Pattern.compile("do\\(\\)|don't\\(\\)")
                                    .matcher(input).results()
                                    .map(k -> new ConditionalOp(k.group(0).equals("do()"), k.start()))
                                    .toArray(ConditionalOp[]::new);

    var conditionals = new ConditionalOp[parsedConditionals.length + 2];
    conditionals[0] = new ConditionalOp(true, 0);
    System.arraycopy(parsedConditionals, 0, conditionals, 1, parsedConditionals.length);
    conditionals[conditionals.length - 1] = new ConditionalOp(true, Integer.MAX_VALUE);

    var part1Result = Arrays.stream(operations)
                            .mapToInt(k -> k.x * k.y)
                            .sum();

    var part2Result = Arrays.stream(operations)
                            .filter(k -> isOperationActive(k, conditionals))
                            .mapToInt(k -> k.x * k.y)
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static boolean isOperationActive(MulOp operation, ConditionalOp[] conditionals) {
    // There is a more efficient way to search here, because the positions are sorted. Wasn't slow, didn't care.
    var activeConditionalI = IntStream.range(0, conditionals.length - 1)
                                      .filter(i -> operation.position >= conditionals[i].position && operation.position <= conditionals[i + 1].position)
                                      .findFirst()
                                      .orElseThrow();

    return conditionals[activeConditionalI].isDo;
}

record MulOp(int x, int y, int position) {}
record ConditionalOp(boolean isDo, int position) {}
