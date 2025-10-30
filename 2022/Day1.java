static void main() throws Exception {
    var calories = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
                         .mapToInt(k -> k.lines().mapToInt(Integer::parseInt).sum())
                         .map(k -> -k) // Reverse sort, this is stupid but ok
                         .sorted()
                         .map(k -> -k)
                         .toArray();

    var part1Result = calories[0];
    var part2Result = calories[0] + calories[1] + calories[2];

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}