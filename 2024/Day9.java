static void main() throws Exception {
    var inputString = Files.readString(Path.of("input.txt"));
    var disk = IntStream.range(0, inputString.length())
                        .mapToObj(i -> new File(i % 2 == 0 ? i / 2 : -1, Character.getNumericValue(inputString.charAt(i))))
                        .filter(k -> k.size() != 0)
                        .toArray(File[]::new);

    var part1Result = computeChecksum(doPart1Compression(disk));
    var part2Result = 0; // FIXME

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static File[] doPart1Compression(File[] initialDisk) {
    var result = new ArrayList<>(Arrays.asList(initialDisk));
    var currentlyMovedFileIndex = initialDisk.length - 1;
    var currentlyConsumedSpaceIndex = 1;

    while(currentlyConsumedSpaceIndex != -1) {
        var currentlyMovedFile = (File) result.get(currentlyMovedFileIndex);
        var currentlyConsumedSpace = (File) result.get(currentlyConsumedSpaceIndex);

        if(currentlyMovedFile.size < currentlyConsumedSpace.size) {
            result.set(currentlyConsumedSpaceIndex, currentlyMovedFile);
            result.add(currentlyConsumedSpaceIndex + 1, new File(-1, currentlyConsumedSpace.size - currentlyMovedFile.size));
            result.remove(currentlyMovedFileIndex + 1);
        }else if(currentlyMovedFile.size > currentlyConsumedSpace.size) {
            result.set(currentlyConsumedSpaceIndex, new File(currentlyMovedFile.ID, currentlyConsumedSpace.size));
            result.set(currentlyMovedFileIndex, new File(currentlyMovedFile.ID, currentlyMovedFile.size - currentlyConsumedSpace.size));
        }else{
            result.set(currentlyConsumedSpaceIndex, currentlyMovedFile);
            result.remove(currentlyMovedFileIndex);
        }

        currentlyMovedFileIndex = IntStream.range(0, result.size())
                                           .map(i -> -i)
                                           .sorted()  // Reverse search hack, because there's no reversed sort on IntStream
                                           .map(i -> -i)
                                           .filter(i -> result.get(i).ID != -1)
                                           .findFirst()
                                           .orElseThrow();

        currentlyConsumedSpaceIndex = IntStream.range(0, result.size())
                                               .filter(i -> result.get(i).ID == -1)
                                               .findFirst()
                                               .orElse(-1);
    }

    return result.toArray(File[]::new);
}

static long computeChecksum(File[] items) {
    var totalLength = Arrays.stream(items)
                            .mapToLong(File::size)
                            .sum();
    var result = 0L;
    var spaceInsideItem = items[0].size();

    for(int itemI = 0, positionI = 0; positionI < totalLength; ++positionI) {
        if(items[itemI].ID != -1) {
            result += items[itemI].ID * positionI;
            --spaceInsideItem;

            if(spaceInsideItem == 0 && itemI + 1 < items.length) {
                spaceInsideItem = items[++itemI].size();
            }
        }
    }

    return result;
}

record File(int ID, int size) {}