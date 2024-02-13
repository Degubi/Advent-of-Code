import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class Day12 {
    static final ObjectMapper JSON = new ObjectMapper();  // jackson-databind


    public static void main(String[] args) throws Exception {
        var input = JSON.readValue(Files.readAllBytes(Path.of("Day12.txt")), JsonNode.class);
        var part1Result = Day12.streamChildNodes(input, k -> true)
                               .filter(JsonNode::isNumber)
                               .mapToInt(JsonNode::intValue)
                               .sum();

        var part2Result = Day12.streamChildNodes(input, Day12::doesntContainRedValue)
                               .filter(JsonNode::isNumber)
                               .mapToInt(JsonNode::intValue)
                               .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static boolean doesntContainRedValue(JsonNode node) {
        return node instanceof ObjectNode == false ||
               StreamSupport.stream(Spliterators.spliteratorUnknownSize(((ObjectNode) node).elements(), 0), false)
                            .noneMatch(k -> k.isTextual() && k.textValue().equals("red"));
    }

    static Stream<JsonNode> streamChildNodes(JsonNode node, Predicate<JsonNode> parentPredicate) {
        return StreamSupport.stream(node.spliterator(), false)
                            .flatMap(l -> parentPredicate.test(l) ? Stream.concat(Stream.of(l), streamChildNodes(l, parentPredicate))
                                                                  : Stream.empty());
    }
}