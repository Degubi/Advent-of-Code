static void main() throws Exception {
    var input = Integer.parseInt(Files.readString(Path.of("input.txt")));

    var rectBeginEnd = IntStream.range(1, Integer.MAX_VALUE)
                                .filter(k -> k % 2 == 1)
                                .mapToObj(k -> k * k)
                                .gather(Gatherers.windowSliding(2))
                                .filter(k -> input >= k.getFirst() && input <= k.getLast())
                                .findFirst()
                                .orElseThrow();

    var rectBegin = rectBeginEnd.getFirst() + 1;
    var rectEnd = rectBeginEnd.getLast();

    var rectangleSideItemCount = (rectBeginEnd.getLast() - rectBeginEnd.getFirst()) / 4;
    var rectangleIndex = (int) Math.sqrt(rectEnd) / 2;

    var corner1 = rectBegin + (rectangleSideItemCount * 1 - 1);
    var corner2 = rectBegin + (rectangleSideItemCount * 2 - 1);
    var corner3 = rectBegin + (rectangleSideItemCount * 3 - 1);
    var corner4 = rectBegin + (rectangleSideItemCount * 4 - 1);
    var side1 = corner1 - rectangleSideItemCount / 2;
    var side2 = corner2 - rectangleSideItemCount / 2;
    var side3 = corner3 - rectangleSideItemCount / 2;
    var side4 = corner4 - rectangleSideItemCount / 2;

    var sideStepCount = Math.min(Math.abs(side1 - input), Math.min(Math.abs(side2 - input), Math.min(Math.abs(side3 - input), Math.abs(side4 - input))));

    var part1Result = rectangleIndex + sideStepCount;

    var part2Result = 0;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}