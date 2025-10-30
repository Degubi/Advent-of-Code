static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(k -> k.split("x", 3))
                     .map(k -> new int[] { Integer.parseInt(k[0]), Integer.parseInt(k[1]), Integer.parseInt(k[2]) })
                     .toArray(int[][]::new);

    var part1Result = Arrays.stream(input)
                            .mapToInt(k -> calculatePaperAmount(k[0], k[1], k[2]))
                            .sum();

    var part2Result = Arrays.stream(input)
                            .mapToInt(k -> calculateRibbonAmount(k[0], k[1], k[2]))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculatePaperAmount(int l, int w, int h) {
    var extra = Math.min(l * w, Math.min(l * h, w * h));

    return (2 * l * w + 2 * w * h + 2 * h * l) + extra;
}

static int calculateRibbonAmount(int l, int w, int h) {
    var smallest = Math.min(l, Math.min(w, h));
    var middle = Math.max(Math.min(l, w), Math.min(Math.max(l, w), h));

    return (2 * smallest + 2 * middle) + (l * w * h);
}