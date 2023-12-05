package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.*;

@Slf4j
public class Day4 implements AdventOfCodeDailyExercise {

    private static final int RADIX = 10;

    @Getter
    static final class Day4Context extends CountingContext {
        private final Map<Integer, Integer> cardIdMultiplier = new HashMap<>();

        public void addWingNumbersByCardId(int cardId, int count) {
            Integer multiplier = cardIdMultiplier.getOrDefault(cardId, 1);
            if (count != 0) {
                for (int i = cardId + 1; i <= cardId + count; i++) {
                    Integer otherMultiplier = cardIdMultiplier.getOrDefault(i, 1);
                    cardIdMultiplier.put(i, otherMultiplier + multiplier);
                }
            }
            add(multiplier);
        }
    }

    private List<Integer> parseNumbers(String cardSection) {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        List<Integer> result = new ArrayList<>();
        int value = 0;
        for (char aChar : cardSection.toCharArray()) {
            if (Character.isDigit(aChar)) {
                value = (value * RADIX) + Character.digit(aChar, RADIX);
            } else if (value != 0) {
                result.add(value);
                value = 0;
            }
        }
        if (value != 0) {
            result.add(value);
        }
        return result;
    }


    private void processLineSolution1(Integer lineNumber, String line, Day4Context context) {
        String[] split = line.split(":");
        String[] split1 = split[1].split("\\|");
        List<Integer> winingNumbers = parseNumbers(split1[0].trim());
        List<Integer> ownNumbers = parseNumbers(split1[1].trim());
        long count = ownNumbers.stream()
                .distinct()
                .filter(winingNumbers::contains)
                .count();
        if (count == 1) {
            context.add(1);
        } else if (count > 1) {
            context.add((int) Math.pow(2, count - 1));
        }
    }

    private void processLineSolution2(Integer lineNumber, String line, Day4Context context) {
        String[] split = line.split(":");
        Integer cardId = parseNumbers(split[0]).get(0);
        String[] split1 = split[1].split("\\|");
        List<Integer> winingNumbers = parseNumbers(split1[0].trim());
        List<Integer> ownNumbers = parseNumbers(split1[1].trim());
        long count = ownNumbers.stream()
                .distinct()
                .filter(winingNumbers::contains)
                .count();
        context.addWingNumbersByCardId(cardId, (int) count);
    }

    @Override
    public int solveFirst() {
        LineProcessor<Day4Context> lineProcessor = getContextLineReader();
        Day4Context context = lineProcessor.processLines(this::processLineSolution1);
        log.info("Solution for the first exercise: {}", context.getSum());
        return context.getSum();
    }


    @Override
    public int solveSecond() {
        LineProcessor<Day4Context> lineProcessor = getContextLineReader();
        Day4Context context = lineProcessor.processLines(this::processLineSolution2);
        log.info("Solution for the second exercise: {}", context.getSum());
        return context.getSum();
    }

    private static LineProcessor<Day4Context> getContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day4"),
                new Day4Context()
        );
    }
}
