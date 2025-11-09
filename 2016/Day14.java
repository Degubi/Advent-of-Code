static void main() throws Exception {
    var salt = Files.readString(Path.of("input.txt"));
    var md5 = MessageDigest.getInstance("MD5");
    var hex = HexFormat.of();
    var part1HashCache = new HashMap<Integer, String>();
    var part2HashCache = new HashMap<Integer, String>();

    var part1Result = getResult(1, salt, md5, hex, part1HashCache).index;
    var part2Result = getResult(2017, salt, md5, hex, part2HashCache).index;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Hash getResult(int hashRepeatCount, String salt, MessageDigest md5, HexFormat hex, HashMap<Integer, String> hashCache) {
    return IntStream.range(0, Integer.MAX_VALUE)
                    .mapToObj(i -> createHash(hashRepeatCount, salt, i, md5, hex, hashCache))
                    .filter(k -> isKey(hashRepeatCount, k, salt, md5, hex, hashCache))
                    .skip(63)
                    .findFirst()
                    .orElseThrow();
}

static Hash createHash(int hashRepeatCount, String salt, int index, MessageDigest md5, HexFormat hex, HashMap<Integer, String> hashCache) {
    return new Hash(index, hashCache.computeIfAbsent(index, i ->
            Stream.iterate(hash(salt + index, md5, hex), k -> hash(k, md5, hex))
                  .skip(hashRepeatCount - 1)
                  .findFirst()
                  .orElseThrow()));
}

static String hash(String text, MessageDigest md5, HexFormat hex) {
    return hex.formatHex(md5.digest(text.getBytes())).toLowerCase();
}

static boolean isKey(int hashRepeatCount, Hash hash, String salt, MessageDigest md5, HexFormat hex, HashMap<Integer, String> hashCache) {
    return hash.value.chars().boxed()
                     .gather(Gatherers.windowSliding(3))
                     .filter(l -> l.stream().allMatch(l.get(0)::equals))
                     .findFirst()
                     .filter(l -> isKeyCondition2(hashRepeatCount, l.get(0), hash.index, salt, md5, hex, hashCache))
                     .isPresent();
}

static boolean isKeyCondition2(int hashRepeatCount, Integer checkedItem, int index, String salt, MessageDigest md5, HexFormat hex, HashMap<Integer, String> hashCache) {
    return IntStream.range(index + 1, index + 999)
                    .mapToObj(i -> createHash(hashRepeatCount, salt, i, md5, hex, hashCache))
                    .anyMatch(k -> k.value.chars().boxed()
                                          .gather(Gatherers.windowSliding(5))
                                          .anyMatch(e -> e.stream().allMatch(checkedItem::equals)));
}

record Hash(int index, String value) {}
