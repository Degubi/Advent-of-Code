import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day4 {
    record Position(int row, int column) {}
    record BoardStats(Position winningNumberPosition, int[][] board, int[][] drawIndices) {}

    static final Pattern NUMBER_REGEX = Pattern.compile("\\d+");


    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of("Day4.txt")).split("\n\n");
        var drawnNumbers = Arrays.stream(input[0].split(","))
                                 .mapToInt(Integer::parseInt)
                                 .toArray();

        var boards = Arrays.stream(input, 1, input.length)
                           .map(Day4::parseBoard)
                           .map(k -> createBoardStatistics(k, drawnNumbers))
                           .toArray(BoardStats[]::new);

        var winningBoard = Arrays.stream(boards)
                                 .min(Comparator.comparingInt(k -> k.drawIndices[k.winningNumberPosition().row()][k.winningNumberPosition().column()]))
                                 .orElseThrow();

        var part1Result = calculateBoardScore(winningBoard.winningNumberPosition(), winningBoard.board(), winningBoard.drawIndices());
        var part2Result = 0;  // TODO

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static int calculateBoardScore(Position numberPosition, int[][] board, int[][] drawIndices) {
        var winningNumberDrawIndex = drawIndices[numberPosition.row()][numberPosition.column()];
        var number = board[numberPosition.row()][numberPosition.column()];
        var unmarkedNumbersSum = IntStream.range(0, board.length)
                                          .map(r -> IntStream.range(0, board[r].length).filter(c -> drawIndices[r][c] > winningNumberDrawIndex).map(c -> board[r][c]).sum())
                                          .sum();

        return number * unmarkedNumbersSum;
    }

    static BoardStats createBoardStatistics(int[][] board, int[] drawnNumbers) {
        var drawIndices = IntStream.range(0, board.length)
                                   .mapToObj(r -> IntStream.range(0, board[r].length).map(c -> findDrawIndex(board[r][c], drawnNumbers)).toArray())
                                   .toArray(int[][]::new);

        var winningNumberPosition = IntStream.range(0, board.length)
                                             .mapToObj(r -> IntStream.range(0, board[r].length).mapToObj(c -> new Position(r, c)).max(byDrawIndexValue(drawIndices)).orElseThrow())
                                             .min(byDrawIndexValue(drawIndices))
                                             .orElseThrow();

        return new BoardStats(winningNumberPosition, board, drawIndices);
    }

    static Comparator<Position> byDrawIndexValue(int[][] drawIndices) {
        return (p1, p2) -> Integer.compare(drawIndices[p1.row()][p1.column()], drawIndices[p2.row()][p2.column()]);
    }

    static int findDrawIndex(int value, int[] drawnNumbers) {
        return IntStream.range(0, drawnNumbers.length)
                        .filter(i -> drawnNumbers[i] == value)
                        .findFirst()
                        .orElse(Integer.MAX_VALUE);
    }

    static int[][] parseBoard(String boardLinesText) {
        return Arrays.stream(boardLinesText.split("\n"))
                     .map(k -> NUMBER_REGEX.matcher(k).results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray())
                     .toArray(int[][]::new);
    }
}