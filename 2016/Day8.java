static void main() throws Exception {  // Using int[][] instead of boolean[][] because there's no boolean stream...
    var instructions = Files.readAllLines(Path.of("input.txt"));
    var part1FinalPixels = instructions.stream()
                                       .reduce(new int[6][50], (k, l) -> applyInstruction(k, l), (_, l) -> l);

    var part1Result = Arrays.stream(part1FinalPixels)
                            .flatMapToInt(Arrays::stream)
                            .filter(k -> k == 1)
                            .count();

    System.out.println("Result 1: " + part1Result + ", result 2: ");

    Arrays.stream(part1FinalPixels) // Read part 2 text looking further from the monitor
          .map(k -> Arrays.stream(k).mapToObj(l -> l == 1 ? "#" : ".").collect(Collectors.joining()))
          .forEach(System.out::println);
}

static int[][] applyInstruction(int[][] pixels, String instruction) {
    var args = instruction.split(" ");

    if(args[0].equals("rect")) {
        var widthHeight = args[1].split("x");
        var width = Integer.parseInt(widthHeight[0]);
        var height = Integer.parseInt(widthHeight[1]);

        IntStream.range(0, height)
                 .forEach(h -> Arrays.fill(pixels[h], 0, width, 1));
    }else{
        var coordinateDirection = args[2].charAt(0);
        var coordinatePosition = Integer.parseInt(args[2], args[2].indexOf('=') + 1, args[2].length(), 10);
        var amount = Integer.parseInt(args[4]);

        if(coordinateDirection == 'x') {
            var rowCount = pixels.length;

            IntStream.range(0, amount).forEach(_ -> {
                var colLast = pixels[rowCount - 1][coordinatePosition];

                IntStream.range(1, rowCount)
                         .map(i -> -i)
                         .sorted()
                         .map(i -> -i)
                         .forEach(i -> pixels[i][coordinatePosition] = pixels[i - 1][coordinatePosition]);

                pixels[0][coordinatePosition] = colLast;
            });
        }else{
            var row = pixels[coordinatePosition];
            var colCount = row.length;

            IntStream.range(0, amount).forEach(_ -> {
                var rowLast = row[colCount -1];

                IntStream.range(1, colCount)
                         .map(i -> -i)
                         .sorted()
                         .map(i -> -i)
                         .forEach(i -> row[i] = row[i - 1]);

                row[0] = rowLast;
            });
        }
    }

    return pixels;
}
