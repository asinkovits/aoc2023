package com.sinkovits.aoc2023;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.IntStream;

import static com.sinkovits.aoc2023.Day6.Context;

@Slf4j
public class Day6 extends AbstractDay<Context> {

    public Day6() {
        super("input_day6", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        String[] split = line.split(":");
        List<Long> longs = ParsingUtil.parseNumbers(split[1].trim());
        if (lineNumber == 0) {
            context.setTimes(longs);
        } else {
            context.setDistances(longs);
        }
    }

    @Override
    protected long calculateFirst(Context context) {
        return calculateResult(context);
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        String[] split = line.split(":");
        List<Long> longs = ParsingUtil.parseNumbers(split[1].trim().replaceAll(StringUtils.SPACE, StringUtils.EMPTY));
        if (lineNumber == 0) {
            context.setTimes(longs);
        } else {
            context.setDistances(longs);
        }
    }

    @Override
    protected long calculateSecond(Context context) {
        return calculateResult(context);
    }

    @Data
    protected static class Context {
        private List<Long> times;
        private List<Long> distances;
    }

    private long calculateResult(Context context) {
        return IntStream.range(0, context.distances.size())
                .mapToObj(i -> Pair.of(context.times.get(i), context.distances.get(i)))
                .map(this::quadratic)
                .reduce(1L, (a, b) -> a * b);
    }

    private long quadratic(Pair<Long, Long> timeDistancePair) {
        double a = 1.0;
        double b = -timeDistancePair.getLeft();
        double c = timeDistancePair.getRight();
        double x1 =  ((-1 * b) + Math.sqrt((b * b) - (4*a*c))) / (2 * a);
        //double x2 =  ((-1 * b) - Math.sqrt((b * b) - (4*a*c))) / (2 * a);
        double x2 = timeDistancePair.getLeft() - x1;
        return 1L + ((long) Math.floor(x1) - (long) Math.ceil(x2));
    }
}
