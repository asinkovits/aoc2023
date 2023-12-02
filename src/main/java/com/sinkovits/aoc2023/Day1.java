package org.example;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

@Slf4j
public class Day1 implements Day {

    @Getter
    private static final class Day1Context {
        private int sum = 0;

        public void add(int value) {
            sum += value;
        }
    }

    private enum Numbers {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);
        final int value;

        Numbers(int value) {
            this.value = value;
        }
    }

    private record NumberPosition(Numbers numbers, int position) {

        private static NumberPosition of(Numbers number, int position) {
            return number != null ? new NumberPosition(number, position) : null;
        }
    }

    private void processLine1(String input, Day1Context context) {
        String replace = removeNonNumeric(input);
        String fn = replace.charAt(0) + "" + replace.charAt(replace.length() - 1);
        context.add(Integer.parseInt(fn));
    }


    private void processLine2(String input, Day1Context context) {
        Pair<NumberPosition, NumberPosition> positions = findFirsAndLastPositions(input);
        StringBuilder sb = new StringBuilder(input);
        if (positions.getLeft() != null) {
            sb.insert(positions.getLeft().position, positions.getLeft().numbers.value);
        }
        if (positions.getRight() != null) {
            sb.insert(positions.getRight().position + 1, positions.getRight().numbers.value);
        }
        String replace = removeNonNumeric(sb.toString());
        String fn = replace.charAt(0) + "" + replace.charAt(replace.length() - 1);
        context.add(Integer.parseInt(fn));
    }

    private String removeNonNumeric(String line) {
        return line.replaceAll("[^0-9]", "");
    }

    private Pair<NumberPosition, NumberPosition> findFirsAndLastPositions(String input) {
        int firstPos = Integer.MAX_VALUE;
        Numbers first = null;
        int lastPos = Integer.MIN_VALUE;
        Numbers last = null;
        String line = input.toLowerCase();
        for (Numbers numbers : Numbers.values()) {
            String number = numbers.name().toLowerCase();
            if (line.contains(number)) {
                int fPos = line.indexOf(number);
                int lPos = line.lastIndexOf(number);
                if (firstPos > fPos) {
                    firstPos = fPos;
                    first = numbers;
                }
                if (lastPos < lPos) {
                    lastPos = lPos;
                    last = numbers;
                }
            }
        }
        return Pair.of(
                NumberPosition.of(first, firstPos),
                NumberPosition.of(last, lastPos)
        );
    }

    public void solveFirst() {
        LineProcessor<Day1Context> lineProcessor = getDay1ContextLineReader();
        Day1Context context = lineProcessor.processLines(this::processLine1);
        log.info("Solution for the first exercise: {}", context.getSum());
    }

    public void solveSecond() {
        LineProcessor<Day1Context> lineProcessor = getDay1ContextLineReader();
        var context = lineProcessor.processLines(this::processLine2);
        log.info("Solution for the second exercise: {}", context.getSum());

    }

    private static LineProcessor<Day1Context> getDay1ContextLineReader() {
        return new LineProcessor<>(
                Path.of("C:\\Users\\Anti\\dev\\aoc2023\\src\\main\\resources\\input_day1"),
                new Day1Context()
        );
    }
}

