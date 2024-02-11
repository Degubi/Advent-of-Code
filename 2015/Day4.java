import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.stream.*;

public class Day4 {


    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("Day4.txt"));
        var md5 = MessageDigest.getInstance("MD5");
        var hex = HexFormat.of();
        var part1Result = findPrefixedMD5HexString(input, "00000", md5, hex);
        var part2Result = findPrefixedMD5HexString(input, "000000", md5, hex);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int findPrefixedMD5HexString(String input, String prefix, MessageDigest md5, HexFormat hex) {
        return IntStream.iterate(0, k -> k + 1)
                        .filter(k -> hexMD5Encode(input, k, md5, hex).startsWith(prefix))
                        .findFirst()
                        .orElseThrow();
    }

    static String hexMD5Encode(String input, int number, MessageDigest md5, HexFormat hex) {
        return hex.formatHex(md5.digest((input + number).getBytes()));
    }
}