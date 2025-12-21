static void main() throws Exception {
    var inputString = Files.readString(Path.of("input.txt"));
    var disk = IntStream.range(0, inputString.length())
                        .mapToObj(i -> new Block(i % 2 == 0 ? i / 2 : Block.EMPTY_ID, Character.getNumericValue(inputString.charAt(i))))
                        .filter(k -> k.size() != 0)
                        .toArray(Block[]::new);

    var part1Result = computeChecksum(doPart1Compression(disk));
    var part2Result = computeChecksum(doPart2Compression(disk));

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static ArrayList<Block> doPart1Compression(Block[] initialDisk) {
    var result = new ArrayList<>(Arrays.asList(initialDisk));
    var currentlyMovedBlockIndex = initialDisk.length - 1;
    var currentlyConsumedSpaceIndex = 1;

    while(currentlyConsumedSpaceIndex != -1) {
        var currentlyMovedBlock = result.get(currentlyMovedBlockIndex);
        var currentlyConsumedSpace = result.get(currentlyConsumedSpaceIndex);

        if(currentlyMovedBlock.size < currentlyConsumedSpace.size) {
            result.set(currentlyConsumedSpaceIndex, currentlyMovedBlock);
            result.add(currentlyConsumedSpaceIndex + 1, new Block(Block.EMPTY_ID, currentlyConsumedSpace.size - currentlyMovedBlock.size));
            result.remove(currentlyMovedBlockIndex + 1);
        }else if(currentlyMovedBlock.size > currentlyConsumedSpace.size) {
            result.set(currentlyConsumedSpaceIndex, new Block(currentlyMovedBlock.ID, currentlyConsumedSpace.size));
            result.set(currentlyMovedBlockIndex, new Block(currentlyMovedBlock.ID, currentlyMovedBlock.size - currentlyConsumedSpace.size));
        }else{
            result.set(currentlyConsumedSpaceIndex, currentlyMovedBlock);
            result.remove(currentlyMovedBlockIndex);
        }

        currentlyMovedBlockIndex = IntStream.iterate(result.size() - 1, i -> i - 1)
                                            .filter(i -> result.get(i).ID != Block.EMPTY_ID)
                                            .findFirst()
                                            .orElseThrow();

        currentlyConsumedSpaceIndex = IntStream.range(0, result.size())
                                               .filter(i -> result.get(i).ID == Block.EMPTY_ID)
                                               .findFirst()
                                               .orElse(-1);
    }

    return result;
}

static ArrayList<Block> doPart2Compression(Block[] initialDisk) {
    var result = new ArrayList<>(Arrays.asList(initialDisk));

    for(var currentlyMovedBlockIndex = initialDisk.length - 1; currentlyMovedBlockIndex >= 0; --currentlyMovedBlockIndex) {
        var currentlyMovedBlock = result.get(currentlyMovedBlockIndex);

        if(currentlyMovedBlock.ID != Block.EMPTY_ID) {
            var currentlyConsumedSpaceIndex = IntStream.range(0, result.size())
                                                       .filter(i -> result.get(i).ID == Block.EMPTY_ID && currentlyMovedBlock.size <= result.get(i).size)
                                                       .findFirst()
                                                       .orElse(-1);

            if(currentlyConsumedSpaceIndex != -1 && currentlyMovedBlockIndex > currentlyConsumedSpaceIndex) {
                var currentlyConsumedSpace = result.get(currentlyConsumedSpaceIndex);

                if(currentlyMovedBlock.size < currentlyConsumedSpace.size) {
                    result.set(currentlyConsumedSpaceIndex, currentlyMovedBlock);
                    result.add(currentlyConsumedSpaceIndex + 1, new Block(Block.EMPTY_ID, currentlyConsumedSpace.size - currentlyMovedBlock.size));
                    result.set(currentlyMovedBlockIndex + 1, new Block(Block.EMPTY_ID, currentlyMovedBlock.size));
                    ++currentlyMovedBlockIndex;
                }else if(currentlyMovedBlock.size == currentlyConsumedSpace.size){
                    result.set(currentlyConsumedSpaceIndex, currentlyMovedBlock);
                    result.set(currentlyMovedBlockIndex, new Block(Block.EMPTY_ID, currentlyMovedBlock.size));
                }
            }
        }
    }

    return result;
}

static long computeChecksum(ArrayList<Block> items) {
    var totalLength = items.stream()
                           .mapToLong(Block::size)
                           .sum();
    var result = 0L;
    var spaceInsideItem = items.getFirst().size();

    for(int itemI = 0, positionI = 0; positionI < totalLength; ++positionI) {
        var item = items.get(itemI);

        result += (item.ID == Block.EMPTY_ID ? 0 : items.get(itemI).ID) * positionI;
        --spaceInsideItem;

        if(spaceInsideItem == 0 && itemI + 1 < items.size()) {
            spaceInsideItem = items.get(++itemI).size();
        }
    }

    return result;
}

record Block(int ID, int size) {
    static final int EMPTY_ID = -1;
}