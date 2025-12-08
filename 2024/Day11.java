static void main() throws Exception {
    var inputStones = Arrays.stream(Files.readString(Path.of("input.txt")).trim().split(" "))
                            .mapToLong(Long::parseLong)
                            .toArray();

    var stoneCountCache = new HashMap<CacheEntry, Long>();
    var part1Result = Arrays.stream(inputStones)
                            .map(k -> getStoneCount(k, 25, stoneCountCache))
                            .sum();

    var part2Result = Arrays.stream(inputStones)
                            .map(k -> getStoneCount(k, 75, stoneCountCache))
                            .sum();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long getStoneCount(long stone, long count, HashMap<CacheEntry, Long> stoneCountCache) {
    if(count == 0) return 1;

    var cacheKey = new CacheEntry(stone, count);
    var cached = stoneCountCache.get(cacheKey);

    if(cached != null) return cached;

    var nextCount = count - 1;
    if(stone == 0) {
        var result = getStoneCount(1, nextCount, stoneCountCache);
        stoneCountCache.put(cacheKey, result);
        return result;
    }

    var digitCount = ((int) Math.log10(stone)) + 1;
    if(digitCount % 2 == 0) {
         var idk = (long) Math.pow(10, digitCount / 2);
         var result = getStoneCount(stone / idk, nextCount, stoneCountCache) + getStoneCount(stone % idk, nextCount, stoneCountCache);

         stoneCountCache.put(cacheKey, result);
         return result;
    }

    var result = getStoneCount(stone * 2024, nextCount, stoneCountCache);
    stoneCountCache.put(cacheKey, result);
    return result;
}

record CacheEntry(long stone, long count) {}
