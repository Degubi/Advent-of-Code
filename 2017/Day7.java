static void main() throws Exception {
    var nodes = Files.lines(Path.of("input.txt"))
                     .map(k -> parseNode(k))
                     .toArray(Node[]::new);

    var part1Result = Stream.iterate(nodes[0], k -> findParentNode(k, nodes))
                            .gather(Gatherers.windowSliding(2))
                            .dropWhile(k -> k.get(1) != null)
                            .map(k -> k.get(0))
                            .findFirst()
                            .orElseThrow();

    var part2Result = findOutlierWeightDifference(part1Result, new NodeWithTotalWeight[0], null, nodes);

    System.out.println("Result 1: " + part1Result.name + ", result 2: " + part2Result);
}


static int findOutlierWeightDifference(Node startingNode, NodeWithTotalWeight[] previousNodeWeights, NodeWithTotalWeight previousOutlier, Node[] nodes) {
    var nodesWithWeights = Arrays.stream(startingNode.childNodeNames)
                                 .map(k -> findNodeByName(k, nodes))
                                 .map(k -> new NodeWithTotalWeight(k, calculateTotalNodeWeight(k, nodes)))
                                 .sorted(Comparator.comparing(NodeWithTotalWeight::weight))
                                 .toArray(NodeWithTotalWeight[]::new);

    var outlier = nodesWithWeights[0].weight == nodesWithWeights[1].weight ? nodesWithWeights[nodesWithWeights.length - 1] : nodesWithWeights[0];

    return calculateDifference(nodesWithWeights, outlier.node.weight) == 0 ? previousOutlier.node.weight - calculateDifference(previousNodeWeights, previousOutlier.weight)
                                                                           : findOutlierWeightDifference(outlier.node, nodesWithWeights, outlier, nodes);
}

static int calculateDifference(NodeWithTotalWeight[] nodesWithWeights, int outlierWeight) {
    return Math.abs(nodesWithWeights[nodesWithWeights.length / 2].weight - outlierWeight);
}

static Node parseNode(String line) {
    var dashIndex = line.indexOf('-');
    var weightBeginParenIndex = line.indexOf('(');
    var weightEndParenIndex = line.indexOf(')');

    return new Node(
        line.substring(0, line.indexOf(' ')),
        Integer.parseInt(line, weightBeginParenIndex + 1, weightEndParenIndex, 10),
        dashIndex == -1 ? new String[0] : line.substring(dashIndex + 3).split(", ")
    );
}

static int calculateTotalNodeWeight(Node node, Node[] nodes) {
    return node.weight + Arrays.stream(node.childNodeNames)
                               .mapToInt(k -> calculateTotalNodeWeight(findNodeByName(k, nodes), nodes))
                               .sum();
}

static Node findNodeByName(String name, Node[] nodes) {
    return Arrays.stream(nodes)
                 .filter(k -> k.name.equals(name))
                 .findFirst()
                 .orElseThrow();
}

static Node findParentNode(Node node, Node[] nodes) {
    return Arrays.stream(nodes)
                 .filter(k -> Arrays.stream(k.childNodeNames).anyMatch(c -> c.equals(node.name)))
                 .findFirst()
                 .orElse(null);
}

record NodeWithTotalWeight(Node node, int weight) {}
record Node(String name, int weight, String[] childNodeNames) {}