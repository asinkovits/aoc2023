package com.sinkovits.aoc2023;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.LongStream;

import static com.sinkovits.aoc2023.ParsingUtil.*;

@Slf4j
public class Day5 implements AdventOfCodeDailyExercise {

    public static final String COLON = ":";

    private record MappingRule(long destinationStart, long sourceStart, long length) {

        static MappingRule of(List<Long> input) {
            return new MappingRule(input.get(0), input.get(1), input.get(2));
        }
    }

    @Data
    private static class Context {
        private MappingRuleType mappingRuleType = null;
        private LongStream seeds;
        private EnumMap<MappingRuleType, List<MappingRule>> mappingRules = new EnumMap<>(MappingRuleType.class);

        public void addMappingRule(MappingRuleType mappingRuleType, MappingRule mappingRule) {
            this.mappingRuleType = mappingRuleType;
            this.mappingRules.computeIfAbsent(mappingRuleType, k -> new ArrayList<>())
                    .add(mappingRule);
        }
    }

    private enum MappingRuleType {
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

    @Override
    public long solveFirst() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::processLineSolution1);
        long min = calculateMinimumLocation(context.seeds, context.mappingRules);
        log.info("Solution for the first exercise: {}", min);
        return (int) min;
    }

    @Override
    public long solveSecond() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::processLineSolution2);
        long min = calculateMinimumLocation(context.seeds, context.mappingRules);
        log.info("Solution for the second exercise: {}", min);
        return (int) min;
    }


    private static LineProcessor<Context> getContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day5"),
                new Context()
        );
    }

    private void processLineSolution1(int lineNumber, String line, Context context) {
        if (lineNumber == 0) {
            context.setSeeds(parseSeeds1(line.split(COLON)[1].trim()));
        } else {
            parseLineCommon(line, context);
        }
    }

    private void processLineSolution2(int lineNumber, String line, Context context) {
        if (lineNumber == 0) {
            context.setSeeds(parseSeeds2(line.split(COLON)[1].trim()));
        } else {
            parseLineCommon(line, context);
        }
    }

    private LongStream parseSeeds1(String input) {
        return parseNumbers(input)
                .stream()
                .mapToLong(Long::longValue);
    }

    private LongStream parseSeeds2(String input) {
        List<Long> longs = parseNumbers(input);
        return LongStream.concat(
                LongStream.range(longs.get(0), longs.get(0) + longs.get(1)),
                LongStream.range(longs.get(2), longs.get(2) + longs.get(3)));
    }

    private void parseLineCommon(String line, Context context) {
        if (StringUtils.EMPTY.equals(line.trim())) {
            context.mappingRuleType = null;
            return;
        }
        Optional<MappingRuleType> parseState = tryParseStateChange(line);
        parseState.ifPresentOrElse(context::setMappingRuleType, () ->
                parseData(line, context));
    }

    private Optional<MappingRuleType> tryParseStateChange(String line) {
        return line.contains(COLON) ? Arrays.stream(MappingRuleType.values())
                .filter(ps -> line.trim().contains(ps.command))
                .findFirst() : Optional.empty();
    }

    private void parseData(String line, Context context) {
        if (context.mappingRuleType != null) {
            context.addMappingRule(context.mappingRuleType, MappingRule.of(parseNumbers(line)));
        }
    }

    private long calculateMinimumLocation(LongStream seeds, EnumMap<MappingRuleType, List<MappingRule>> mappingRules) {
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
