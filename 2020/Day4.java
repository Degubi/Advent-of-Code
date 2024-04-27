import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day4 {
    static final Pattern HAIR_COLOR_REGEX = Pattern.compile("#[0-9a-f]{6}");
    static final Pattern HEIGHT_REGEX = Pattern.compile("(\\d+)(cm|in)");
    static final Pattern PASSPORT_ID_REGEX = Pattern.compile("[0-9]{9}");


    public static void main(String[] args) throws Exception {
        var passports = Arrays.stream(Files.readString(Path.of("Day4.txt")).split("\n\n"))
                              .map(k -> k.replace('\n', ' ').split(" "))
                              .map(k -> Arrays.stream(k).map(l -> l.split(":")).collect(Collectors.toMap(l -> l[0], l -> l[1])))
                              .collect(Collectors.toList());

        var part1Result = passports.stream()
                                   .filter(Day4::isPassportValidPart1)
                                   .count();

        var part2Result = passports.stream()
                                   .filter(k -> isPassportValidPart1(k) && isPassportValidPart2(k))
                                   .count();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static boolean isPassportValidPart1(Map<String, String> passportFields) {
        return passportFields.size() == 8 || (passportFields.size() == 7 && !passportFields.containsKey("cid"));
    }

    static boolean isPassportValidPart2(Map<String, String> passportFields) {
        var birthYear = Integer.parseInt(passportFields.get("byr"));
        var issueYear = Integer.parseInt(passportFields.get("iyr"));
        var expirationYear = Integer.parseInt(passportFields.get("eyr"));
        var heightMatch = HEIGHT_REGEX.matcher(passportFields.get("hgt"));
        var validHeight = heightMatch.find();
        var heightValue = validHeight ? Integer.parseInt(heightMatch.group(1)) : -1;
        var heightType = validHeight ? heightMatch.group(2) : null;
        var eyeColor = passportFields.get("ecl");

        return birthYear >= 1920 && birthYear <= 2002 &&
               issueYear >= 2010 && issueYear <= 2020 &&
               expirationYear >= 2020 && expirationYear <= 2030 &&
               validHeight && ((heightType.equals("cm") && heightValue >= 150 && heightValue <= 193) || (heightType.equals("in") && heightValue >= 59 && heightValue <= 76)) &&
               HAIR_COLOR_REGEX.matcher(passportFields.get("hcl")).matches() &&
               (eyeColor.equals("amb") || eyeColor.equals("blu") || eyeColor.equals("brn") || eyeColor.equals("gry") || eyeColor.equals("grn") || eyeColor.equals("hzl") || eyeColor.equals("oth")) &&
               PASSPORT_ID_REGEX.matcher(passportFields.get("pid")).matches();
    }
}