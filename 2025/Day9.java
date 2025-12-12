static void main() throws Exception {
    var points = Files.lines(Path.of("input.txt"))
                      .map(k -> k.split(","))
                      .map(k -> new Point(Integer.parseInt(k[0]), Integer.parseInt(k[1])))
                      .toArray(Point[]::new);

    var rectangles = Arrays.stream(points)
                           .flatMap(k -> Arrays.stream(points).map(l -> new Rectangle(k, l, (Math.abs(k.x - l.x) + 1L) * (Math.abs(k.y - l.y) + 1L))))
                           .filter(k -> !k.point1.equals(k.point2) && (k.point1.x != k.point2.x && k.point1.y != k.point2.y))
                           .toArray(Rectangle[]::new);

    var part1Result = Arrays.stream(rectangles)
                            .mapToLong(Rectangle::area)
                            .max()
                            .orElseThrow();

    var part2Result = 0; // FIXME

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

record Point(int x, int y) {}
record Rectangle(Point point1, Point point2, long area) {}