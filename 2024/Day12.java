// There are soo many optimization potentials, wasn't slow, didn't care
static void main() throws Exception {
    var garden = Files.lines(Path.of("input.txt"))
                      .map(String::toCharArray)
                      .toArray(char[][]::new);

    var regions = new ArrayList<Region>();
    var potentialNewRegions = new ArrayList<>(List.of(new Region(garden[0][0], new ArrayList<>(List.of(new Position(0, 0))))));

    do {
        var currentlyExpandedRegion = potentialNewRegions.removeLast();
        var positionQueue = new ArrayList<>(List.of(currentlyExpandedRegion.positions.getFirst()));

        if(regions.stream().anyMatch(k -> k.positions.contains(positionQueue.getFirst()))) {
            continue;
        }

        regions.add(currentlyExpandedRegion);

        while(!positionQueue.isEmpty()) {
            var currentPosition = positionQueue.removeLast();

            handlePosition(-1, 0, currentPosition, currentlyExpandedRegion, positionQueue, potentialNewRegions, regions, garden);
            handlePosition(1, 0, currentPosition, currentlyExpandedRegion, positionQueue, potentialNewRegions, regions, garden);
            handlePosition(0, -1, currentPosition, currentlyExpandedRegion, positionQueue, potentialNewRegions, regions, garden);
            handlePosition(0, 1, currentPosition, currentlyExpandedRegion, positionQueue, potentialNewRegions, regions, garden);
        }
    }while(!potentialNewRegions.isEmpty());

    var part1Result = regions.stream()
                             .mapToInt(k -> k.positions.size() * calculatePerimeter(k.positions))
                             .sum();

    System.out.println("Result 1: " + part1Result); // TODO: Part 2
}

static int calculatePerimeter(ArrayList<Position> positions) {
    return positions.stream()
                    .mapToInt(k -> getFreeSideValue(-1, 0, k, positions) + getFreeSideValue(1, 0, k, positions) + getFreeSideValue(0, -1, k, positions) + getFreeSideValue(0, 1, k, positions))
                    .sum();
}

static int getFreeSideValue(int xOffset, int yOffset, Position currentPosition, ArrayList<Position> positions) {
    return positions.contains(new Position(currentPosition.x + xOffset, currentPosition.y + yOffset)) ? 0 : 1;
}

static void handlePosition(int xOffset, int yOffset, Position currentPosition, Region currentlyExpandedRegion, ArrayList<Position> positionQueue, ArrayList<Region> potentialNewRegions, ArrayList<Region> regions, char[][] garden) {
    var nextX = currentPosition.x + xOffset;
    var nextY = currentPosition.y + yOffset;

    if(nextX >= 0 && nextX < garden.length && nextY >= 0 && nextY < garden[0].length) {
        var nextPosition = new Position(nextX, nextY);

        if(!positionQueue.contains(nextPosition) && !currentlyExpandedRegion.positions.contains(nextPosition)) {
            var nextPositionLetter = garden[nextX][nextY];

            if(nextPositionLetter == currentlyExpandedRegion.letter) {
                currentlyExpandedRegion.positions.add(nextPosition);
                positionQueue.add(nextPosition);
            }else{
                if(potentialNewRegions.stream().noneMatch(k -> k.positions.contains(nextPosition)) && regions.stream().noneMatch(k -> k.positions.contains(nextPosition))) {
                    potentialNewRegions.add(new Region(nextPositionLetter, new ArrayList<>(List.of(nextPosition))));
                }
            }
        }
    }
}

record Position(int x, int y) {}
record Region(char letter, ArrayList<Position> positions) {}
