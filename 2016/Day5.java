import java.util.*;
import java.util.stream.*;

static void main() throws Exception {
    var input = Files.readString(Path.of("input.txt"));
    var md5 = MessageDigest.getInstance("MD5");
    var hexFormat = HexFormat.of();
    var part1Result = IntStream.range(0, Integer.MAX_VALUE)
                               .mapToObj(i -> hexFormat.formatHex(md5.digest((input + i).getBytes())))
                               .filter(k -> k.startsWith("00000"))
                               .limit(8)
                               .map(k -> Character.toString(k.charAt(5)))
                               .collect(Collectors.joining());

    var part2Result = new String(
            IntStream.range(0, Integer.MAX_VALUE)
                     .mapToObj(i -> hexFormat.formatHex(md5.digest((input + i).getBytes())))
                     .filter(k -> k.startsWith("00000") && Character.isDigit(k.charAt(5)) && (k.charAt(5) - '0') <= 7)
                     .map(k -> new PositionDistinctWrapper(k.charAt(5) - '0', (byte) k.charAt(6)))
                     .distinct()
                     .limit(8)
                     .reduce(new byte[8], (result, k) -> {
                         result[k.position] = k.value;
                         return result;
                     }, (k, l) -> l));

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

record PositionDistinctWrapper(int position, byte value) {

    @Override
    public final boolean equals(Object o) {
        return o instanceof PositionDistinctWrapper(var position, _) && position == this.position;
    }

    @Override
    public final int hashCode() {
        return Integer.hashCode(position);
    }
}
