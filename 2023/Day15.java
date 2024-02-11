import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day15 {
    static final int BOX_COUNT = 256;

    record Step(String label, char operation, int focalLength) {}


    public static void main(String[] args) throws Exception {
        var steps = Files.readString(Path.of("Day15.txt")).split(",");

        var part1Result = Arrays.stream(steps)
                                .mapToInt(Day15::hash)
                                .sum();

        var boxes = Arrays.stream(steps)
                          .map(Day15::parseStep)
                          .reduce(createEmptyBoxes(), Day15::applyStep, (k, l) -> l);

        var part2Result = IntStream.range(0, BOX_COUNT)
                                   .map(i -> calculateBoxFocusingPower(boxes, i))
                                   .sum();

        System.out.println("Result 1: " + part1Result + ", result 2: " + part2Result);
    }

    @SuppressWarnings("unchecked")
    static ArrayList<Step>[] createEmptyBoxes() {
        return IntStream.range(0, BOX_COUNT)
                        .mapToObj(i -> new ArrayList<Step>())
                        .toArray(ArrayList[]::new);
    }

    static int hash(String text) {
        return text.chars()
                   .reduce(0, (result, k) -> ((result + k) * 17) % BOX_COUNT);
    }

    static int calculateBoxFocusingPower(ArrayList<Step>[] boxes, int index) {
        var box = boxes[index];
        var boxNumber = index + 1;

        return IntStream.range(0, box.size())
                        .map(i -> boxNumber * (i + 1) * box.get(i).focalLength())
                        .sum();
    }

    static Step parseStep(String stepData) {
        var eqIndex = stepData.indexOf('=');

        return eqIndex == -1 ? new Step(stepData.substring(0, stepData.length() - 1), '-', 0)
                             : new Step(stepData.substring(0, eqIndex), '=', Integer.parseInt(stepData.substring(eqIndex + 1)));
    }

    static ArrayList<Step>[] applyStep(ArrayList<Step>[] boxes, Step step) {
        var box = boxes[hash(step.label())];

        if(step.operation() == '=') {
            var existingStep = box.stream()
                                  .filter(k -> k.label().equals(step.label()))
                                  .findFirst()
                                  .orElse(null);

            if(existingStep != null) {
                box.set(box.indexOf(existingStep), step);
            }else{
                box.add(step);
            }
        }else{
            box.removeIf(k -> k.label().equals(step.label()));
        }

        return boxes;
    }
}