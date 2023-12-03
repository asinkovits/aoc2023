package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.*;

import static com.sinkovits.aoc2023.Day3.AdjacencyCalculator.*;

@Slf4j
public class Day3 implements AdventOfCodeDailyExercise {

    @Getter
    static final class Day3Context {
        private final List<PotentialPartNumber> potentialPartNumbers = new LinkedList<>();
        private final List<Symbol> symbols = new ArrayList<>();
        private int sum = 0;

        public void addPotentialPartNumber(PotentialPartNumber potentialPartNumber) {
            potentialPartNumbers.add(potentialPartNumber);
        }

        public void addSymbol(Symbol symbol) {
            symbols.add(symbol);
        }
    }

    record Coordinate(int columnNumber, int lineNumber) {
        public static Coordinate of(int columnNumber, int lineNumber) {
            return new Coordinate(columnNumber, lineNumber);
        }
    }

    record Symbol(char symbol, Coordinate coordinate) {
        public static final char GEAR = '*';

        public static Symbol of(char symbol, int columnNumber, int lineNumber) {
            return new Symbol(symbol, new Coordinate(columnNumber, lineNumber));
        }
    }

    record PotentialPartNumber(int partNumber, Coordinate start) {
        public static PotentialPartNumber of(int partNumber, int columnNumber, int lineNumber) {
            return new PotentialPartNumber(partNumber, new Coordinate(columnNumber, lineNumber));
        }
    }

    @Getter
    @NoArgsConstructor
    static final class PotentialPartNumberParser {
        private static final int RADIX = 10;
        private int value;
        private Coordinate start;

        public Optional<PotentialPartNumber> finish() {
            if(start != null) {
                Optional<PotentialPartNumber> result = Optional.of(new PotentialPartNumber(value, start));
                value = 0;
                start = null;
                return result;
            }
            return Optional.empty();
        }

        public void addDigit(char digit, int columnNumber, int lineNumber) {
            if(start == null) {
                start = new Coordinate(columnNumber, lineNumber);
            }
            value = (value * RADIX) + Character.digit(digit, RADIX);
        }
    }

    // Simplified AdjacencyCalculator
    public static class AdjacencyCalculator {
        public static boolean isAdjacent(PotentialPartNumber potentialPartNumber, Coordinate symbolCoordinate) {
            int numberOfDigits = String.valueOf(potentialPartNumber.partNumber).length();
            return potentialPartNumber.start.lineNumber == symbolCoordinate.lineNumber &&
                    (potentialPartNumber.start.columnNumber - 1 == symbolCoordinate.columnNumber ||
                            potentialPartNumber.start.columnNumber + numberOfDigits == symbolCoordinate.columnNumber) ||
                    (potentialPartNumber.start.lineNumber == symbolCoordinate.lineNumber - 1 ||
                            potentialPartNumber.start.lineNumber == symbolCoordinate.lineNumber + 1) &&
                            symbolCoordinate.columnNumber >= potentialPartNumber.start.columnNumber - 1 &&
                            symbolCoordinate.columnNumber <= potentialPartNumber.start.columnNumber + numberOfDigits;
        }
    }

    private void processLineSolution1(int lineNumber, String line, Day3Context context) {
        parseLine(lineNumber, line, context);
        context.symbols.forEach(symbol -> {
            List<PotentialPartNumber> adjacentPartNumbers = context.potentialPartNumbers.stream().filter(ppn -> isAdjacent(ppn, symbol.coordinate)).toList();
            int sum = adjacentPartNumbers.stream().mapToInt(ppn -> ppn.partNumber).sum();
            context.sum += sum;
            context.potentialPartNumbers.removeAll(adjacentPartNumbers);
        });
    }

    private void processLineSolution2(int lineNumber, String line, Day3Context context) {
        parseLine(lineNumber, line, context);
        context.symbols.stream().filter(symbol -> symbol.symbol == Symbol.GEAR)
                .forEach(symbol -> {
                    List<PotentialPartNumber> adjacentPartNumbers = context.potentialPartNumbers.stream().filter(ppn -> isAdjacent(ppn, symbol.coordinate)).toList();
                    if (adjacentPartNumbers.size() == 2) {
                        context.sum += adjacentPartNumbers.get(0).partNumber * adjacentPartNumbers.get(1).partNumber;
                        context.potentialPartNumbers.removeAll(adjacentPartNumbers);
                    }
                });
    }

    private static void parseLine(int lineNumber, String line, Day3Context context) {
        PotentialPartNumberParser potentialPartNumberParser = new PotentialPartNumberParser();
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                potentialPartNumberParser.addDigit(chars[i], i, lineNumber);
            } else {
                potentialPartNumberParser.finish().ifPresent(context::addPotentialPartNumber);
                if (chars[i] != '.') {
                    context.addSymbol(Symbol.of(chars[i], i, lineNumber));
                }
            }
        }
        potentialPartNumberParser.finish().ifPresent(context::addPotentialPartNumber);
    }

    @Override
    public int solveFirst() {
        LineProcessor<Day3Context> lineProcessor = getDay3ContextLineReader();
        Day3Context context = lineProcessor.processLines(this::processLineSolution1);
        log.info("Solution for the first exercise: {}", context.getSum());
        return context.getSum();
    }

    @Override
    public int solveSecond() {
        LineProcessor<Day3Context> lineProcessor = getDay3ContextLineReader();
        Day3Context context = lineProcessor.processLines(this::processLineSolution2);
        log.info("Solution for the first exercise: {}", context.getSum());
        return context.getSum();
    }

    private static LineProcessor<Day3Context> getDay3ContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day3"),
                new Day3Context()
        );
    }
}
