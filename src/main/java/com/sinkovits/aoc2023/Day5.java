package com.sinkovits.aoc2023;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.LongStream;

@Slf4j
public class Day5 implements AdventOfCodeDailyExercise {

    private static final int RADIX = 10;

    private record MappingRule(long destinationStart, long sourceStart, long length) {
    }

    @Setter
    private static class Day5Context {
        MappingRuleType mappingRuleType = null;
        LongStream seeds;
        EnumMap<MappingRuleType, List<MappingRule>> mappingRules = new EnumMap<>(MappingRuleType.class);

        public void setMappingRules(MappingRuleType mappingRuleType, List<Long> mappingRules) {
            this.mappingRuleType = mappingRuleType;
            this.mappingRules.computeIfAbsent(mappingRuleType, k -> new ArrayList<>())
                    .add(new MappingRule(mappingRules.get(0), mappingRules.get(1), mappingRules.get(2)));
        }

        public long getMin() {
            return seeds.parallel()
                    .map(seed -> mapTo(seed, mappingRules.get(MappingRuleType.S_TO_S)))
                    .map(soil -> mapTo(soil, mappingRules.get(MappingRuleType.S_TO_F)))
                    .map(fertilizer -> mapTo(fertilizer, mappingRules.get(MappingRuleType.F_TO_W)))
                    .map(water -> mapTo(water, mappingRules.get(MappingRuleType.W_TO_L)))
                    .map(light -> mapTo(light, mappingRules.get(MappingRuleType.L_TO_T)))
                    .map(temperature -> mapTo(temperature, mappingRules.get(MappingRuleType.T_TO_H)))
                    .map(humidity -> mapTo(humidity, mappingRules.get(MappingRuleType.H_TO_L)))
                    .min()
                    .orElseThrow();
        }

        long mapTo(long from, List<MappingRule> rules) {
            return rules.stream()
                    .filter(rule -> rule.sourceStart <= from)
                    .filter(rule -> from < rule.sourceStart + rule.length)
                    .findFirst()
                    .map(rule ->
                            rule.destinationStart - rule.sourceStart + from)
                    .orElse(from);
        }
    }

    private enum MappingRuleType {
        S("seeds:"),
        S_TO_S("seed-to-soil map:"),
        S_TO_F("soil-to-fertilizer map:"),
        F_TO_W("fertilizer-to-water map:"),
        W_TO_L("water-to-light map:"),
        L_TO_T("light-to-temperature map:"),
        T_TO_H("temperature-to-humidity map:"),
        H_TO_L("humidity-to-location map:");

        private final String command;

        MappingRuleType(String command) {
            this.command = command;
        }
    }

    private LongStream parseSeeds(String input) {
        List<Long> longs = parseNumbers(input);
        return longs.stream().mapToLong(l -> l);
    }

    private List<Long> parseNumbers(String input) {
        List<Long> result = new ArrayList<>();
        long value = 0;
        for (char aChar : input.toCharArray()) {
            if (Character.isDigit(aChar)) {
                value = (value * RADIX) + Character.digit(aChar, RADIX);
            } else {
                result.add(value);
                value = 0;
            }
        }
        result.add(value);
        return result;
    }

    private void processLineSolution1(int lineNumber, String line, Day5Context context) {
        if (StringUtils.EMPTY.equals(line.trim())) {
            context.mappingRuleType = null;
            return;
        }
        if (lineNumber == 0) {
            context.setSeeds(parseSeeds(line.split(":")[1].trim()));
        }
        Optional<MappingRuleType> parseState = tryParseStateChange(line);
        parseState.ifPresentOrElse(context::setMappingRuleType, () ->
                parseData(line, context));
    }

    private void processLineSolution2(int lineNumber, String line, Day5Context context) {
        if (StringUtils.EMPTY.equals(line.trim())) {
            context.mappingRuleType = null;
            return;
        }
        if (lineNumber == 0) {
            context.setSeeds(getSeeds(line));
        }
        Optional<MappingRuleType> parseState = tryParseStateChange(line);
        parseState.ifPresentOrElse(context::setMappingRuleType, () ->
                parseData(line, context));
    }

    private LongStream getSeeds(String line) {
        List<Long> longs = parseNumbers(line.split(":")[1].trim());
        return LongStream.concat(
                        LongStream.range(longs.get(0), longs.get(0) + longs.get(1)),
                        LongStream.range(longs.get(2), longs.get(2) + longs.get(3)));
    }

    private Optional<MappingRuleType> tryParseStateChange(String line) {
        return Arrays.stream(MappingRuleType.values())
                .filter(ps -> line.trim().contains(ps.command))
                .findFirst();
    }

    private void parseData(String line, Day5Context context) {
        switch (context.mappingRuleType) {
            case S_TO_S, S_TO_F, F_TO_W, W_TO_L, L_TO_T, T_TO_H, H_TO_L ->
                    context.setMappingRules(context.mappingRuleType, parseNumbers(line));
        }
    }

    @Override
    public int solveFirst() {
        LineProcessor<Day5Context> lineProcessor = getContextLineReader();
        Day5Context context = lineProcessor.processLines(this::processLineSolution1);
        long min = context.getMin();
        log.info("Solution for the first exercise: {}", min);
        return (int) min;
    }

    @Override
    public int solveSecond() {
        LineProcessor<Day5Context> lineProcessor = getContextLineReader();
        Day5Context context = lineProcessor.processLines(this::processLineSolution2);
        long min = context.getMin();
        log.info("Solution for the second exercise: {}", min);
        return (int) min;
    }


    private static LineProcessor<Day5Context> getContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day5"),
                new Day5Context()
        );
    }
}
