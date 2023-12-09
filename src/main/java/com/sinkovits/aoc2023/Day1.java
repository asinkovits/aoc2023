package com.sinkovits.aoc2023;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import static com.sinkovits.aoc2023.ParsingUtil.removeNonNumeric;

@Slf4j
public class Day1 extends AbstractDay<CountingContext> {

    public Day1() {
        super("input_day1", CountingContext.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, CountingContext context) {
        String replace = removeNonNumeric(line);
        long calibrationValue = extractCalibrationValue(replace);
        context.add(calibrationValue);
    }

    @Override
    protected long calculateFirst(CountingContext context) {
        return context.getSum();
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, CountingContext context) {
        String preprocessedInput = resolveFirstAndLastDigitsSpelledOutWithLetters(line);
        parseFirst(lineNumber, preprocessedInput, context);
    }

    @Override
    protected long calculateSecond(CountingContext context) {
        return context.getSum();
    }

    private enum Numbers {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9);
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

    private static long extractCalibrationValue(String input) {
        String fn = String.format("%c%c", input.charAt(0), input.charAt(input.length() - 1));
        return Long.parseLong(fn);
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
        return Pair.of(NumberPosition.of(first, firstPos), NumberPosition.of(last, lastPos));
    }
}
