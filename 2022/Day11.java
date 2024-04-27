import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day11 {
    static final Pattern STARTING_ITEMS_REGEX = Pattern.compile("Starting items: (.*)");

    record Monkey(List<Integer> items, IntUnaryOperator leftArgument, char operator, IntUnaryOperator rightArgument, int divByTestValue, int trueMonkeyIndex, int falseMonkeyIndex) {}
    record IterationState(int[] inspectCounts, Monkey[] monkeys) {}


    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("Day11.txt"));
        var monkeys = Arrays.stream(input.split("\n\n"))
                            .map(Day11::parseMonkey)
                            .toArray(Monkey[]::new);

        var testValueLcm = Arrays.stream(monkeys)
                                 .mapToInt(Monkey::divByTestValue)
                                 .reduce(Day11::lcm)
                                 .orElseThrow();

        var part1Result = getMonkeyBusinessLevel(monkeys, 3, 20, testValueLcm);
        var part2Result = getMonkeyBusinessLevel(monkeys, 1, 10000, testValueLcm);

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static long getMonkeyBusinessLevel(Monkey[] monkeys, int worryLevelDivide, int iterations, int testValueLcm) {
        var inspectCounts = Stream.iterate(new IterationState(new int[monkeys.length], copyMonkeys(monkeys)), k -> iterateMonkeys(k, worryLevelDivide, testValueLcm))
                                  .skip(1)
                                  .limit(iterations)
                                  .reduce((k, l) -> l)
                                  .orElseThrow()
                                  .inspectCounts();

        Arrays.sort(inspectCounts);
        return ((long) inspectCounts[inspectCounts.length - 1]) * ((long) inspectCounts[inspectCounts.length - 2]);
    }

    static Monkey[] copyMonkeys(Monkey[] monkeys) {
        return Arrays.stream(monkeys)
                     .map(k -> new Monkey(new ArrayList<>(k.items()), k.leftArgument(), k.operator(), k.rightArgument(), k.divByTestValue(), k.trueMonkeyIndex(), k.falseMonkeyIndex()))
                     .toArray(Monkey[]::new);
    }

    static IterationState iterateMonkeys(IterationState state, int worryLevelDivide, int testValueLcm) {
        IntStream.range(0, state.inspectCounts().length)
                 .forEach(i -> {
                    var monkey = state.monkeys()[i];
                    var items = monkey.items();

                    state.inspectCounts()[i] += items.size();
                    items.forEach(k -> {
                        var newValue = switch(monkey.operator()) {
                            case '+' -> monkey.leftArgument().applyAsInt(k) + monkey.rightArgument().applyAsInt(k);
                            case '-' -> monkey.leftArgument().applyAsInt(k) - monkey.rightArgument().applyAsInt(k);
                            case '*' -> monkey.leftArgument().applyAsInt(k) * monkey.rightArgument().applyAsInt(k);
                            case '/' -> monkey.leftArgument().applyAsInt(k) / monkey.rightArgument().applyAsInt(k);
                            default  -> -1;
                        } / worryLevelDivide % testValueLcm;

                        state.monkeys()[newValue % monkey.divByTestValue() == 0 ? monkey.trueMonkeyIndex() : monkey.falseMonkeyIndex()].items().add(newValue);
                    });
                    items.clear();
                 });

        return state;
    }

    static int gcd(int n1, int n2) { return n2 == 0 ? n1 : gcd(n2, n1 % n2); }
    static int lcm(int n1, int n2) { return n1 * (n2 / gcd(n1, n2)); }

    static Monkey parseMonkey(String data) {
        var lines = data.split("\n");
        var startingItemsMatcher = STARTING_ITEMS_REGEX.matcher(lines[1]);
        startingItemsMatcher.find();

        var startingItems = Arrays.stream(startingItemsMatcher.group(1).split(", "))
                                  .map(Integer::valueOf)
                                  .collect(Collectors.toList());

        var operationArgs = lines[2].split(" ");
        var leftArgument = parseArgument(operationArgs[operationArgs.length - 3]);
        var operator = operationArgs[operationArgs.length - 2].charAt(0);
        var rightArgument = parseArgument(operationArgs[operationArgs.length - 1]);

        return new Monkey(startingItems, leftArgument, operator, rightArgument, parseLastInteger(lines[3]), parseLastInteger(lines[4]), parseLastInteger(lines[5]));
    }

    static IntUnaryOperator parseArgument(String arg) {
        if(arg.equals("old")) {
            return k -> k;
        }

        var constant = Integer.parseInt(arg);
        return k -> constant;
    }

    static int parseLastInteger(String line) {
        return Integer.parseInt(line, line.lastIndexOf(' ') + 1, line.length(), 10);
    }
}