static void main() throws Exception {
    var rooms = Files.lines(Path.of("input.txt"))
                     .map(k -> parseRoom(k))
                     .toArray(Room[]::new);

    var part1Result = Arrays.stream(rooms)
                            .filter(k -> isRoomReal(k))
                            .mapToInt(Room::sectorID)
                            .sum();

    var part2Result = Arrays.stream(rooms)
                            .filter(k -> decryptRoomName(k).equals("northpole object storage"))
                            .map(Room::sectorID)
                            .findFirst()
                            .orElseThrow();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static Room parseRoom(String line) {
    var squareBIndex = line.indexOf('[');
    var sectorIDBeginSlashIndex = line.lastIndexOf('-');

    return new Room(
        line.substring(0, sectorIDBeginSlashIndex),
        Integer.parseInt(line, sectorIDBeginSlashIndex + 1, squareBIndex, 10),
        line.substring(squareBIndex + 1, line.length() - 1)
    );
}

static boolean isRoomReal(Room room) {
    return room.name.chars()
                    .filter(k -> k != '-')
                    .boxed()
                    .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Comparator.comparingLong(Map.Entry<Integer, Long>::getValue).reversed().thenComparing(Map.Entry::getKey))
                    .limit(5)
                    .map(e -> Character.toString(e.getKey()))
                    .collect(Collectors.joining())
                    .equals(room.checksum);
}

static String decryptRoomName(Room room) {
    var numOfLetters = 'z' - 'a' + 1;
    var shiftCount = room.sectorID % numOfLetters;

    return room.name.chars()
                    .map(k -> k == '-' ? ' ' : ((k - 'a' + shiftCount) % numOfLetters) + 'a')
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining());
}

record Room(String name, int sectorID, String checksum) {}