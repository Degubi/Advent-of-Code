static void main() throws Exception {
    var cityDistances = Files.lines(Path.of("input.txt"))
                             .map(k -> k.split(" "))
                             .collect(Collectors.toMap(k -> Set.of(k[0], k[2]), k -> Integer.parseInt(k[4])));

    var cities = cityDistances.keySet().stream()
                              .flatMap(Set::stream)
                              .distinct()
                              .toArray(String[]::new);

    var routeStats = generatePermutations(cities)
                    .mapToInt(k -> calculateRouteDistance(k, cityDistances))
                    .summaryStatistics();

    var part1Result = routeStats.getMin();
    var part2Result = routeStats.getMax();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int calculateRouteDistance(String[] route, Map<Set<String>, Integer> cityDistances) {
    return IntStream.range(0, route.length - 1)
                    .map(i -> cityDistances.get(Set.of(route[i], route[i + 1])))
                    .sum();
}

static Stream<String[]> generatePermutations(String[] arr) {
    var elements = new ArrayList<String[]>();
    permute(arr, 0, elements);
    return elements.stream();
}

static void permute(String[] arr, int start, ArrayList<String[]> outElements) {
    if(start == arr.length - 1) {
        outElements.add(arr.clone());
    }else{
        for(var i = start; i < arr.length; ++i) {
            swap(arr, start, i);
            permute(arr, start + 1, outElements);
            swap(arr, start, i);
        }
    }
}

static void swap(String[] arr, int i, int j) {
    var temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}