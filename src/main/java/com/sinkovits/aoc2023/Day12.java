package com.sinkovits.aoc2023;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sinkovits.aoc2023.Day12.*;

public class Day12 extends AbstractDay<Context> {

    private static final Cache<CalcKey, Long> CACHE =
            Caffeine.newBuilder().weakKeys().maximumSize(10_000_000).build();

    public Day12() {
        super("test", Context.class);
        //                super("input_day12", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        SpringRow row = new SpringRow();
        String[] split = line.split(StringUtils.SPACE);
        row.data = split[0];
        row.brokenSprings =
                ParsingUtil.parseNumbers(split[1]).stream().map(Day12::longToIntCast).toList();
        context.rows.add(row);
    }

    @Override
    protected long calculateFirst(Context context) {
        return context.rows.stream().mapToLong(this::calculatePossibleArrangementsDp).sum();
    }

    public static SpringRow createRow(String line) {
        SpringRow row = new SpringRow();
        String[] split = line.split(StringUtils.SPACE);
        row.data = split[0];
        row.brokenSprings =
                ParsingUtil.parseNumbers(split[1]).stream().map(Day12::longToIntCast).toList();
        return row;
    }

    private long calculatePossibleArrangementsDp(SpringRow row) {
        List<Integer> list =
                row.brokenSprings.stream().mapToInt(Day12::longToIntCast).boxed().toList();
        return calculateArrangements(row.data, list);
    }

    private long calculatePossibleArrangementsRec(SpringRow row) {
        List<Integer> list =
                row.brokenSprings.stream().mapToInt(Day12::longToIntCast).boxed().toList();
        return calculateArrangements((row.data + ".").toCharArray(), list);
    }

    private long calculateArrangements(char[] charArray, List<Integer> list) {
        int i = 0;

        return 0;
    }

    private long calculateArrangements(char[] charArray, int charPos, List<Integer> list, char prev, int springPos, int springVal) {
        if(charPos == charArray.length - 1) {
            return 0;
        }

        if(charArray[charPos] == '.') {
            return 0;
        }

        if(charArray[charPos] == '#') {
            if(springVal == 1) {
                return 1;
            } else {
                return calculateArrangements(charArray, charPos++, list, '#', springPos, springVal--);
            }
        }

        if(charArray[charPos] == '?') {

        }

        return 0;
    }

    public static int longToIntCast(long number) {
        return (int) number;
    }

    public int calculateArrangements(String springs, List<Integer> brokenSprings) {
        char[] chars = springs.toCharArray();
        int targetSum = brokenSprings.stream().mapToInt(Integer::intValue).sum();

        int[][] dp = new int[chars.length + 1][targetSum + 1];

        // Base cases
        dp[0][0] = 1;
        for (int i = 1; i <= targetSum; i++) {
            dp[0][i] = 0;
        }

        // DP calculation
        for (int i = 1; i <= chars.length; i++) {
            for (int j = 0; j <= targetSum; j++) {
                if (springs.charAt(i - 1) == '.') {
                    dp[i][j] = dp[i - 1][j];
                } else if (springs.charAt(i - 1) == '#') {
                    handleBrokenSpringCase(i, j, dp);
                } else if (springs.charAt(i - 1) == '?') {
                    dp[i][j] = dp[i - 1][j]; // . case
                    if (j > 0) {
                        handleBrokenSpringCase(i, j, dp);
                    }
                }
            }
        }

        return dp[chars.length][targetSum];
    }

    private static void handleBrokenSpringCase(int i, int j, int[][] dp) {
        if (j > 0) {
            // If we have a valid broken spring otherwise 0.
            // @TODO: multi value case
            // int currentSpring = brokenSprings.get(j - 1);
            if (j <= i) {
                dp[i][j] = dp[i - 1][j - 1];
                if (dp[i - 1][j] > 0) {
                    // If the previous state was valid then this cannot be. Unless it's a ?
                    dp[i][j] += 1 - dp[i - 1][j];
                }
            }
        }
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
        List<Integer> brokenSprings = row.brokenSprings;

        long jokers = row.data.chars().filter(ch -> ch == '?').count();
        long wrongSprings = row.data.chars().filter(ch -> ch == '#').count();
        int targetSum = brokenSprings.stream().mapToInt(Integer::intValue).sum();
        return calc(
                row.data,
                regexpPattern(brokenSprings),
                regexp(brokenSprings),
                jokers,
                wrongSprings,
                targetSum);
    }

    private long calculatePossibleArrangements2(SpringRow row) {
        // String[] split = row.data.split("\\?", -1);
        List<Integer> brokenSprings = row.brokenSprings;

        String input = five(row.data);
        long jokers = input.chars().filter(ch -> ch == '?').count();
        long wrongSprings = input.chars().filter(ch -> ch == '#').count();
        int targetSum = brokenSprings.stream().mapToInt(Integer::intValue).sum();
        return calc(
                input,
                regexpPattern(five(brokenSprings)),
                regexp(five(brokenSprings)),
                jokers,
                wrongSprings,
                targetSum);
    }

    private long calc(
            String input,
            String regexpPattern,
            Pattern regexp,
            long jokers,
            long wrongSprings,
            int targetSum) {
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
            long result =
                    calc(string1, regexpPattern, regexp, jokers - 1, wrongSprings, targetSum)
                            + calc(
                                    string2,
                                    regexpPattern,
                                    regexp,
                                    jokers - 1,
                                    wrongSprings + 1,
                                    targetSum);
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

    private List<Integer> five(List<Integer> brokenSprings) {
        List<Integer> result = new ArrayList<>(brokenSprings.size() * 5);
        for (int i = 0; i < 5; i++) {
            result.addAll(brokenSprings);
        }
        return result;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Context {
        List<SpringRow> rows = new ArrayList<>();
    }

    public static class SpringRow {
        String data;
        List<Integer> brokenSprings;
    }

    Pattern regexp(List<Integer> brokenSprings) {
        return Pattern.compile(regexpPattern(brokenSprings));
    }

    private static String regexpPattern(List<Integer> brokenSprings) {
        StringJoiner sj = new StringJoiner("\\.{1}\\.*", "\\.*", "\\.*");
        for (Integer brokenSpring : brokenSprings) {
            sj.add("#{%d}".formatted(brokenSpring));
        }
        return sj.toString();
    }

    private record CalcKey(String input, String regexpPattern) {}
}
