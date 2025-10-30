import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

static void main() throws Exception {
    var json = new ObjectMapper();
    var input = json.readValue(Files.readAllBytes(Path.of("input.txt")), JsonNode.class);
    var part1Result = streamChildNodes(input, k -> true)
                     .filter(JsonNode::isNumber)
                     .mapToInt(JsonNode::intValue)
                     .sum();

    var part2Result = streamChildNodes(input, k -> doesntContainRedValue(k))
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