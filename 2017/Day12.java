static void main() throws Exception {
    var programs = Files.lines(Path.of("input.txt"))
                        .map(k -> createProgram(k))
                        .toArray(Program[]::new);

    var groups = createGroups(programs);

    var part1Result = groups.get(0).size();
    var part2Result = groups.keySet().size();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static HashMap<Integer, HashSet<Integer>> createGroups(Program[] programs) {
    var groups = new HashMap<Integer, HashSet<Integer>>();

    for(var i = 0; i <= programs[programs.length - 1].left; ++i) {
        var iInteger = Integer.valueOf(i);

        if(groups.values().stream().noneMatch(k -> k.contains(iInteger))) {
            groups.put(i, collectItemsForGroup(iInteger, programs));
        }
    }

    return groups;
}

static HashSet<Integer> collectItemsForGroup(Integer currentItem, Program[] programs) {
    var result = new HashSet<Integer>();
    var valuesToVisit = new ArrayList<Integer>();
    result.add(currentItem);
    valuesToVisit.add(currentItem);

    while(!valuesToVisit.isEmpty()) {
        var valueToVisit = valuesToVisit.removeLast();

        for(var prog : programs) {
            if(Arrays.stream(prog.right).anyMatch(k -> k == (int) valueToVisit)) {
                if (result.add(prog.left)) {
                    valuesToVisit.add(prog.left);
                }

                for(var r : prog.right) {
                    if(result.add(r)) {
                        valuesToVisit.add(r);
                    }
                }
            }
        }
    }

    return result;
}

static Program createProgram(String line) {
    var firstSpaceIndex = line.indexOf(' ');
    var secondSpaceIndex = line.indexOf(' ', firstSpaceIndex + 1);

    return new Program(
        Integer.parseInt(line, 0, firstSpaceIndex, 10),
        Arrays.stream(line.substring(secondSpaceIndex + 1, line.length()).split(", "))
              .mapToInt(Integer::parseInt)
              .toArray()
    );
}

record Program(int left, int[] right) {}