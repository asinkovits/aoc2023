package com.sinkovits.aoc2023;

import com.sinkovits.aoc2023.Day12.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class Day12 extends AbstractDay<Context> {

    public Day12() {
        super("input_day12", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        SpringRow row = new SpringRow();
        String[] split = line.split(StringUtils.SPACE);
        row.data = (split[0] + ".").toCharArray();
        row.brokenSprings =
                ParsingUtil.parseNumbers(split[1]).stream().map(ParsingUtil::longToIntCast).toList();
        context.rows.add(row);
    }

    @Override
    protected long calculateFirst(Context context) {
        return context.rows.stream()
                .mapToLong(row -> calc(row.data, row.brokenSprings, 0, 0, 0, new HashMap<>()))
                .sum();
    }

    private long calc(char[] data, List<Integer> brokenSprings, int charPos, int springPos, int cnt, HashMap<Key, Long> cache) {
        Key key = new Key(charPos, springPos, cnt);
        Long l = cache.get(key);
        if(l != null) {
            return l;
        }

        if (data.length == charPos) {
            if (brokenSprings.size() == springPos) {
                //cache.put(key, 1L);
                return 1;
            }
            //cache.put(key, 1L);
            return 0;
        }
        char currChar = data[charPos];
        if (currChar == '.') {
            long onDot = onDot(data, brokenSprings, charPos, springPos, cnt, cache);
            cache.put(key, onDot);
            return onDot;
        }
        if (currChar == '#') {
            long onHashmark =  onHashmark(data, brokenSprings, charPos, springPos, cnt, cache);
            cache.put(key, onHashmark);
            return onHashmark;
        }
        if (currChar == '?') {
            long onDot = onDot(data, brokenSprings, charPos, springPos, cnt, cache);
            long onHashmark = onHashmark(data, brokenSprings, charPos, springPos, cnt, cache);
            cache.put(key, onDot + onHashmark);
            return  onDot + onHashmark;
        }
        throw new IllegalStateException();
    }

    private long onDot(char[] data, List<Integer> brokenSprings, int charPos, int springPos, int cnt, HashMap<Key, Long> cache) {
        if (springPos == brokenSprings.size()) {
            return calc(data, brokenSprings, charPos + 1, springPos, 0, cache);
        }

        Integer spring = brokenSprings.get(springPos);
        if (cnt == spring) {
            return calc(data, brokenSprings, charPos + 1, springPos + 1, 0, cache);
        }
        if(cnt > 0) {
            // Illegal
            return 0;
        }
        return calc(data, brokenSprings, charPos + 1, springPos, 0, cache);
        //return 0;
    }

    private long onHashmark(char[] data, List<Integer> brokenSprings, int charPos, int springPos, int cnt, HashMap<Key, Long> cache) {
        if (springPos == brokenSprings.size()) {
            // Illegal
            return 0;
        }

        Integer spring = brokenSprings.get(springPos);
        if (cnt == spring) {
            // Illegal
            return 0;
        }
        return calc(data, brokenSprings, charPos + 1, springPos, cnt + 1, cache);
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        SpringRow row = new SpringRow();
        String[] split = line.split(StringUtils.SPACE);
        row.data = (five(split[0]) + ".").toCharArray();
        row.brokenSprings =
                five(ParsingUtil.parseNumbers(split[1]).stream().map(ParsingUtil::longToIntCast).toList());
        context.rows.add(row);
    }

    @Override
    protected long calculateSecond(Context context) {
        return context.rows.stream()
                .mapToLong(row -> calc(row.data, row.brokenSprings, 0, 0, 0, new HashMap<>()))
                .sum();
    }

    public String five(String input) {
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

    protected static class Context {
        List<SpringRow> rows = new ArrayList<>();
    }

    protected static class SpringRow {
        char[] data;
        List<Integer> brokenSprings;
    }

    record Key(int charPos, int springPos, int cnt) {}
}
