package com.sinkovits.aoc2023;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sinkovits.aoc2023.Day12.*;

public class Day12 extends AbstractDay<Context> {

    private static final Cache<CalcKey, Long> CACHE =
            Caffeine.newBuilder().weakKeys().maximumSize(50_000_000).build();

    // rest of your code

    public Day12() {
        super("test", Context.class);
        //        super("input_day12", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        SpringRow row = new SpringRow();
        String[] split = line.split(StringUtils.SPACE);
        row.data = split[0];
        row.brokenSprings = ParsingUtil.parseNumbers(split[1]);
        context.rows.add(row);
    }

    @Override
    protected long calculateFirst(Context context) {
        return context.rows.stream().mapToLong(this::calculatePossibleArrangements).sum();
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        parseFirst(lineNumber, line, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        return context.rows.stream()
                .parallel()
                .mapToLong(this::calculatePossibleArrangements2)
                .sum();
    }

    private long calculatePossibleArrangements(SpringRow row) {
        // String[] split = row.data.split("\\?", -1);
        List<Long> brokenSprings = row.brokenSprings;

        long jokers = row.data.chars().filter(ch -> ch == '?').count();
        long wrongSprings = row.data.chars().filter(ch -> ch == '#').count();
        long targetSum = brokenSprings.stream().mapToLong(Long::longValue).sum();
        return calc(row.data, regexpPattern(brokenSprings), regexp(brokenSprings), jokers, wrongSprings, targetSum);
    }

    private long calculatePossibleArrangements2(SpringRow row) {
        // String[] split = row.data.split("\\?", -1);
        List<Long> brokenSprings = row.brokenSprings;

        String input = five(row.data);
        long jokers = input.chars().filter(ch -> ch == '?').count();
        long wrongSprings = input.chars().filter(ch -> ch == '#').count();
        long targetSum = brokenSprings.stream().mapToLong(Long::longValue).sum();
        return calc(input, regexpPattern(five(brokenSprings)), regexp(five(brokenSprings)), jokers, wrongSprings, targetSum);
    }

    private long calc(
            String input, String regexpPattern, Pattern regexp, long jokers, long wrongSprings, long targetSum) {
        CalcKey key = new CalcKey(input, regexpPattern);
        Long l = CACHE.getIfPresent(key);
        if (l != null) {
            return l;
        }

        if (jokers + wrongSprings < targetSum) {
            // Bail
            CACHE.put(key, 0L);
            return 0;
        }

        if (jokers == 0) {
            long result = match(input, regexp);
            CACHE.put(key, result);
            return result;
        } else {
            int lastIndex = input.lastIndexOf("?");
            String string1 =
                    new StringBuilder(input).replace(lastIndex, lastIndex + 1, ".").toString();
            String string2 =
                    new StringBuilder(input).replace(lastIndex, lastIndex + 1, "#").toString();
            long result = calc(string1, regexpPattern, regexp, jokers - 1, wrongSprings, targetSum)
                    + calc(string2, regexpPattern, regexp, jokers - 1, wrongSprings + 1, targetSum);
            CACHE.put(key, result);
            return result;
        }
    }

    private long calc(String input, List<Long> brokenSprings) {
        char[] charArray = input.toCharArray();
        int positionRead = 0;
        for (int i = 0; i < brokenSprings.size(); i++) {
            int matchingCharacters = 0;
            long springLength = brokenSprings.get(i);
            for (int j = positionRead; j < charArray.length; j++) {
                if (charArray[j] == '.') {
                    positionRead++;
                    matchingCharacters = 0;
                } else {
                    positionRead++;
                    matchingCharacters++;
                }
                if (positionRead == charArray.length
                        && (matchingCharacters != springLength || i < brokenSprings.size() - 1)) {
                    return 0;
                }
                if (matchingCharacters == springLength) {
                    break;
                }
                if (matchingCharacters > springLength) {
                    return 0;
                }
            }
            if (i < brokenSprings.size() - 2) {
                if (positionRead == charArray.length - 2 || charArray[positionRead++] != '.') {
                    return 0;
                }
            }
        }
        return 1;
    }

    private long match(String input, Pattern regexp) {
        Matcher matcher = regexp.matcher(input);
        return matcher.matches() ? 1 : 0;
    }

    String five(String input) {
        StringJoiner sj = new StringJoiner("?");
        for (int i = 0; i < 5; i++) {
            sj.add(input);
        }
        return sj.toString();
    }

    private List<Long> five(List<Long> brokenSprings) {
        List<Long> result = new ArrayList<>(brokenSprings.size() * 5);
        for (int i = 0; i < 5; i++) {
            result.addAll(brokenSprings);
        }
        return result;
    }

    protected static class Context {
        List<SpringRow> rows = new ArrayList<>();
    }

    protected static class SpringRow {
        String data;
        List<Long> brokenSprings;
    }

    Pattern regexp(List<Long> brokenSprings) {
        return Pattern.compile(regexpPattern(brokenSprings));
    }

    private static String regexpPattern(List<Long> brokenSprings) {
        StringJoiner sj = new StringJoiner("\\.{1}\\.*", "\\.*", "\\.*");
        for (Long brokenSpring : brokenSprings) {
            sj.add("#{%d}".formatted(brokenSpring));
        }
        return sj.toString();
    }

    private record CalcKey(String input, String regexpPattern) {}
}
