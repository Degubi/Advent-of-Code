static void main() throws Exception {
    var input = Files.readAllLines(Path.of("input.txt"));
    var playerHitPoints = 100;
    var enemyHitPoints = parseNumberFromLine(input.get(0));
    var enemyDamage = parseNumberFromLine(input.get(1));
    var enemyProtection = parseNumberFromLine(input.get(2));
    var weapons = new Item[] {
        new Item( 8, 4, 0), // Dagger
        new Item(10, 5, 0), // Shortsword
        new Item(25, 6, 0), // Warhammer
        new Item(40, 7, 0), // Longsword
        new Item(74, 8, 0)  // Greataxe
    };

    var armors = new Item[] {
        new Item(  0, 0, 0), // Empty
        new Item( 13, 0, 1), // Leather
        new Item( 31, 0, 2), // Chainmail
        new Item( 53, 0, 3), // Splintmail
        new Item( 75, 0, 4), // Bandedmail
        new Item(102, 0, 5)  // Platemail
    };

    var rings = new Item[] {
        new Item(  0, 0, 0),  // Empty
        new Item(  0, 0, 0),  // Empty
        new Item( 25, 1, 0),  // Damage +1
        new Item( 50, 2, 0),  // Damage +2
        new Item(100, 3, 0),  // Damage +3
        new Item( 20, 0, 1),  // Defense +1
        new Item( 40, 0, 2),  // Defense +2
        new Item( 80, 0, 3),  // Defense +3
    };

    var fightStats = Arrays.stream(weapons)
                           .flatMap(w ->
                               Arrays.stream(armors).flatMap(a ->
                               Arrays.stream(rings).flatMap(r1 ->
                               Arrays.stream(rings).map(r2 -> new Inventory(w, a, r1, r2)))))
                           .filter(k -> k.ring1 != k.ring2)
                           .map(k -> new Result(k.weapon.cost + k.armor.cost + k.ring1.cost + k.ring2.cost, doFight(
                              playerHitPoints,
                              k.weapon.damage     + k.armor.damage     + k.ring1.damage     + k.ring2.damage,
                              k.weapon.protection + k.armor.protection + k.ring1.protection + k.ring2.protection,
                              enemyHitPoints,
                              enemyDamage,
                              enemyProtection
                           )))
                           .collect(Collectors.partitioningBy(Result::win, Collectors.summarizingInt(Result::totalSpending)));

    var part1Result = fightStats.get(true).getMin();
    var part2Result = fightStats.get(false).getMax();

    System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
}

static int parseNumberFromLine(String line) {
    return Integer.parseInt(line, line.indexOf(':') + 2, line.length(), 10);
}

static boolean doFight(int playerHitPoints, int playerDamage, int playerProtection, int bossHitPoints, int bossDamage, int bossProtection) {
    var currentPlayerHitPoints = playerHitPoints;
    var currentBossHitPoints = bossHitPoints;
    var playerTurn = true;

    while(true) {
        currentBossHitPoints   -= playerTurn  ? Math.max(1, playerDamage - bossProtection) : 0;
        currentPlayerHitPoints -= !playerTurn ? Math.max(1, bossDamage - playerProtection) : 0;
        playerTurn = !playerTurn;

        if(currentPlayerHitPoints <= 0) {
            return false;
        }

        if(currentBossHitPoints <= 0) {
            return true;
        }
    }
}

record Item(int cost, int damage, int protection) {}
record Result(int totalSpending, boolean win) {}
record Inventory(Item weapon, Item armor, Item ring1, Item ring2) {}