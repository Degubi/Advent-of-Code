static void main() throws Exception {
    var inputRegex = Pattern.compile("row (\\d+), column (\\d+)")
                            .matcher(Files.readString(Path.of("input.txt")));
    inputRegex.find();

    var row = Integer.parseInt(inputRegex.group(1));
    var column = Integer.parseInt(inputRegex.group(2));

    var part1Result = findValue(row, column);

    System.out.println("Result 1: " + part1Result);
}

static long findValue(int row, int column) {
    var currentRow = 1;
    var currentCol = 1;
    var lastRow = 1;
    var expectedItemCountInDiagonal = 1;
    var remainingItemCountInDiagonal = 1;
    var value = 20151125L;

    while(currentRow != row || currentCol != column) {
        value = value * 252533 % 33554393;

        if(--remainingItemCountInDiagonal == 0) {
            remainingItemCountInDiagonal = ++expectedItemCountInDiagonal;
            currentCol = 1;
            currentRow = ++lastRow;
        }else{
            ++currentCol;
            --currentRow;
        }
    }

    return value;
}