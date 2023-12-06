package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class Day6 implements AdventOfCodeDailyExercise {

    @Getter
    private static class Context {

        private List<Long> times;
        private List<Long> distances;

        public void setTimes(List<Long> times) {
            this.times = times;
        }

        public void setDistances(List<Long> distances) {
            this.distances = distances;
        }

    }

    @Override
    public int solveFirst() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::processLineSolution1);
        long result = calculateValueBruteForce(context);
        log.info("Solution for the first exercise: {}", result);
        return (int) result;
    }

    @Override
    public int solveSecond() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::processLineSolution2);
        long result = calculateValueBruteForce(context);
        log.info("Solution for the second exercise: {}", result);
        return (int) result;
    }

    private long calculateValueBruteForce(Context context) {
        return IntStream.range(0, context.distances.size())
                .mapToObj(i -> Pair.of(context.times.get(i), context.distances.get(i)))
                .map(this::bruteForce)
                .reduce(1L, (a, b) -> a * b);
    }

    private void processLineSolution1(int lineNumber, String line, Context context) {
        String[] split = line.split(":");
        List<Long> longs = ParsingUtil.parseNumbers(split[1].trim());
        if (lineNumber == 0) {
            context.setTimes(longs);
        } else {
            context.setDistances(longs);
        }
    }

    private void processLineSolution2(int lineNumber, String line, Context context) {
        String[] split = line.split(":");
        List<Long> longs = ParsingUtil.parseNumbers(split[1].trim().replaceAll(StringUtils.SPACE, StringUtils.EMPTY));
        if (lineNumber == 0) {
            context.setTimes(longs);
        } else {
            context.setDistances(longs);
        }
    }

    private long bruteForce(Pair<Long, Long> timeDistancePair) {
        return LongStream.range(1, timeDistancePair.getLeft())
                .filter(i -> i * (timeDistancePair.getLeft() - i) > timeDistancePair.getRight())
                .count();
    }

    private static LineProcessor<Context> getContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day6"),
                new Context()
        );
    }


}
