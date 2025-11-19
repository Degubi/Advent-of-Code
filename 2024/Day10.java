static void main() throws Exception {
    var input = Files.lines(Path.of("input.txt"))
                     .map(String::toCharArray)
                     .toArray(char[][]::new);

    var peaks = IntStream.range(0, input.length)
                         .mapToObj(x -> IntStream.range(0, input[0].length)
                                                 .filter(y -> input[x][y] == '0')
                                                 .mapToObj(y -> searchPeaks(input, x, y)))
                         .flatMap(k -> k)
                         .collect(Collectors.toList());

    var part1Result = peaks.stream().mapToLong(k -> k.stream().distinct().count()).sum();
    var part2Result = peaks.stream().mapToLong(ArrayList::size).sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static ArrayList<Point> searchPeaks(char[][] input, int startX, int startY) {
    var peaks = new ArrayList<Point>();
    var pointsToCheck = new ArrayDeque<Point>();
    pointsToCheck.push(new Point(startX, startY));

    while(!pointsToCheck.isEmpty()) {
        var point = pointsToCheck.pop();
        var value = input[point.x][point.y];

        if(value == '9') {
            peaks.add(new Point(point.x, point.y));
        }else{
            checkPosition(point.x + 1, point.y, value, input, pointsToCheck);
            checkPosition(point.x - 1, point.y, value, input, pointsToCheck);
            checkPosition(point.x, point.y + 1, value, input, pointsToCheck);
            checkPosition(point.x, point.y - 1, value, input, pointsToCheck);
        }
    }

    return peaks;
}

static void checkPosition(int x, int y, char value, char[][] input, ArrayDeque<Point> pointsToCheck) {
    if (x >= 0 && y >= 0 && x < input.length && y < input[0].length && input[x][y] == (value + 1)) {
        pointsToCheck.push(new Point(x, y));
    }
}

record Point(int x, int y) {}