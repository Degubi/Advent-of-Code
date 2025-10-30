static final Transform IDENTITY_TRANSFORM = new Transform(0, 0, 0, 0);

static void main() throws Exception {
    var sections = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
                         .map(k -> k.split("\n"))
                         .toArray(String[][]::new);

    var part1Seeds = Arrays.stream(sections[0][0].substring("seeds: ".length()).split(" "))
                           .mapToLong(Long::parseLong)
                           .toArray();

    var transformSegments = parseTransformationSegments(sections);

    var part1Result = Arrays.stream(part1Seeds)
                            .map(k -> applyTransformationSections(k, transformSegments))
                            .min()
                            .orElseThrow();
    // TODO
    var part2Result = 0;

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static long applyTransformationSections(long value, Transform[][] transformSegments) {
    return Arrays.stream(transformSegments)
                 .reduce(value, (k, l) -> applyTransformSegment(k, l), (r, _) -> r);
}

static long applyTransformSegment(long value, Transform[] sectionTransforms) {
    var transform = Arrays.stream(sectionTransforms)
                          .filter(k -> isTransformApplicable(value, k))
                          .findFirst()
                          .orElse(IDENTITY_TRANSFORM);

    return applyTransform(value, transform);
}

static boolean isTransformApplicable(long value, Transform transform) {
    return value >= transform.sourceRangeStart() && value < transform.sourceRangeEnd();
}

static long applyTransform(long value, Transform transform) {
    return value - transform.sourceRangeStart() + transform.destinationRangeStart();
}

static Transform[][] parseTransformationSegments(String[][] sections) {
    return Arrays.stream(sections)
                 .skip(1)  // Seeds
                 .map(k -> parseTransformationSegment(k))
                 .toArray(Transform[][]::new);
}

static Transform[] parseTransformationSegment(String[] segmentLines) {
    var transforms = Arrays.stream(segmentLines)
                           .skip(1)  // Map
                           .map(k -> parseTransformation(k))
                           .sorted(Comparator.comparingLong(Transform::sourceRangeStart))
                           .toArray(Transform[]::new);

    var firstTransform = transforms[0];
    if(firstTransform.sourceRangeStart() == 0) {
        return transforms;
    }

    var zeroSourceRangeStartPadded = new Transform[transforms.length + 1];
    zeroSourceRangeStartPadded[0] = new Transform(0, firstTransform.sourceRangeStart(), 0, firstTransform.sourceRangeStart());
    System.arraycopy(transforms, 0, zeroSourceRangeStartPadded, 1, transforms.length);
    return zeroSourceRangeStartPadded;
}

static Transform parseTransformation(String paramsString) {
    var params = paramsString.split(" ", 3);
    var destinationRangeStart = Long.parseLong(params[0]);
    var sourceRangeStart = Long.parseLong(params[1]);
    var rangeLength = Long.parseLong(params[2]);

    return new Transform(sourceRangeStart, sourceRangeStart + rangeLength, destinationRangeStart, destinationRangeStart + rangeLength);
}

record Transform(long sourceRangeStart, long sourceRangeEnd, long destinationRangeStart, long destinationRangeEnd) {}