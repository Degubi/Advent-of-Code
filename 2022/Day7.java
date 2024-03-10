import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day7 {
    sealed interface Node {
        String name();
        int size();
    }

    record File(String name, int size) implements Node {}
    record Directory(String name, Directory parent, int size, List<Node> files) implements Node {}


    public static void main(String[] args) throws Exception {
        var fileTree = parseFileTree(Files.readAllLines(Path.of("Day7.txt")));

        var directorySizes = Day7.walkDirectories(fileTree)
                                 .mapToInt(Day7::sizeOf)
                                 .toArray();

        var part1Result = Arrays.stream(directorySizes)
                                .filter(k -> k <= 100000)
                                .sum();

        var unusedSpace = 70000000 - directorySizes[0];
        var part2Result = Arrays.stream(directorySizes)
                                .filter(k -> (unusedSpace + k) >= 30000000)
                                .min()
                                .orElseThrow();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    static Stream<Directory> walkDirectories(Directory dir) {
        return Stream.concat(Stream.of(dir), dir.files().stream()
                                                .filter(k -> k instanceof Directory)
                                                .map(k -> (Directory) k)
                                                .flatMap(Day7::walkDirectories));
    }

    static int sizeOf(Node node) {
        return switch(node) {
            case File f -> f.size();
            case Directory d -> d.files().stream()
                                 .mapToInt(Day7::sizeOf)
                                 .sum();
        };
    }

    static Directory parseFileTree(List<String> lines) {
        var result = new Directory("/", null, 0, new ArrayList<>());
        var currentDir = result;

        for(var line : lines.subList(1, lines.size())) {
            var lineArgs = line.split(" ");

            if(lineArgs[0].equals("$")) {
                if(lineArgs[1].equals("cd")) {
                    var dirName = lineArgs[2];

                    if(dirName.equals("..")) {
                        currentDir = currentDir.parent();
                    }else{
                        var newDirectory = new Directory(dirName, currentDir, 0, new ArrayList<>());
                        currentDir.files().add(newDirectory);
                        currentDir = newDirectory;
                    }
                }
            }else{
                var fileInfo = lineArgs[0];

                if(!fileInfo.equals("dir")) {
                    currentDir.files().add(new File(lineArgs[1], Integer.parseInt(fileInfo)));
                }
            }
        }

        return result;
    }
}