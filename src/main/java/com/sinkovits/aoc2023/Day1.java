package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

@Slf4j
public class Day1 implements AdventOfCodeDailyExercise {

    public static final String NON_NUMERIC_REGEX = "[^0-9]";

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

    private void processLineSolution1(String input, Day1Context context) {
        String replace = removeNonNumeric(input);
        int calibrationValue = extractCalibrationValue(replace);
        context.add(calibrationValue);
    }

    private void processLineSolution2(String input, Day1Context context) {
        String preprocessedInput = resolveFirstAndLastDigitsSpelledOutWithLetters(input);
        processLineSolution1(preprocessedInput, context);
    }

    private String resolveFirstAndLastDigitsSpelledOutWithLetters(String input) {
        Pair<NumberPosition, NumberPosition> positions = findFirsAndLastPositions(input);
        StringBuilder sb = new StringBuilder(input);
        if (positions.getLeft() != null) {
            sb.insert(positions.getLeft().position, positions.getLeft().numbers.value);
        }
        if (positions.getRight() != null) {
            sb.insert(positions.getRight().position + 1, positions.getRight().numbers.value);
        }
        return sb.toString();
    }

    private String removeNonNumeric(String input) {
        return input.replaceAll(NON_NUMERIC_REGEX, StringUtils.EMPTY);
    }

    private static int extractCalibrationValue(String input) {
        String fn = String.format("%c%c", input.charAt(0), input.charAt(input.length() - 1));
        return Integer.parseInt(fn);
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
        Day1Context context = lineProcessor.processLines(this::processLineSolution1);
        log.info("Solution for the first exercise: {}", context.getSum());
    }

    public void solveSecond() {
        LineProcessor<Day1Context> lineProcessor = getDay1ContextLineReader();
        var context = lineProcessor.processLines(this::processLineSolution2);
        log.info("Solution for the second exercise: {}", context.getSum());

    }

    private static LineProcessor<Day1Context> getDay1ContextLineReader() {
        return new LineProcessor<>(
                Path.of("C:\\Users\\Anti\\dev\\aoc2023\\src\\main\\resources\\input_day1"),
                new Day1Context()
        );
    }
}

