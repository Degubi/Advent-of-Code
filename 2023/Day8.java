import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day8 {
    static final Pattern NODE_REGEX = Pattern.compile("([A-Z0-9]{3}) = \\(([A-Z0-9]{3}), ([A-Z0-9]{3})\\)");

    record NodePair(String left, String right) {}


    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of("Day8.txt"));
        var instructions = lines.get(0);
        var network = lines.subList(2, lines.size()).stream()
                           .map(Day8::matchNode)
                           .collect(Collectors.toMap(k -> k.group(1), k -> new NodePair(k.group(2), k.group(3))));

        var part1Result = getWalkIterationCount("AAA", instructions, network, k -> k.equals("ZZZ"));
        var part2Result = network.keySet().stream()
                                 .filter(k -> k.endsWith("A"))
                                 .mapToLong(n -> getWalkIterationCount(n, instructions, network, k -> k.endsWith("Z")))
                                 .reduce(Day8::lcm)
                                 .orElseThrow();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static long getWalkIterationCount(String startingNode, String instructions, Map<String, NodePair> network, Predicate<String> finishNodeTester) {
        var currentNodeState = new AtomicReference<>(startingNode);

        return Stream.generate(instructions::chars)
                     .flatMap(IntStream::boxed)
                     .takeWhile(k -> {
                         var node = network.get(currentNodeState.get());
                         var newNode = k == 'L' ? node.left() : node.right();

                         currentNodeState.set(newNode);
                         return !finishNodeTester.test(newNode);
                     })
                     .count() + 1;
    }

    static Matcher matchNode(String text) {
        var matcher = NODE_REGEX.matcher(text);
        matcher.find();
        return matcher;
    }

    static long gcd(long n1, long n2) { return n2 == 0 ? n1 : gcd(n2, n1 % n2); }
    static long lcm(long n1, long n2) { return n1 * (n2 / gcd(n1, n2)); }
}