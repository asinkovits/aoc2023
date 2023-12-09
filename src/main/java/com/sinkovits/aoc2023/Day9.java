package com.sinkovits.aoc2023;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.sinkovits.aoc2023.Day9.Context;

@Slf4j
public class Day9 extends AbstractDay<Context> {

    public static class Context {
        List<List<Long>> data = new ArrayList<>();
    }

    public Day9() {
        super("input_day9", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        List<Long> parsedMeasurementValues = ParsingUtil.parseNumbers(line);
        context.data.add(lineNumber, parsedMeasurementValues);
    }

    @Override
    protected long calculateFirst(Context context) {
        return context.data.stream().mapToLong(this::predictNextValue).sum();
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        parseFirst(lineNumber, line, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        return context.data.stream().map(this::reverse).mapToLong(this::predictNextValue).sum();
    }

    private long predictNextValue(List<Long> measurementValues) {
        Deque<Long> lastValues = new LinkedList<>();
        lastValues.addLast(measurementValues.get(measurementValues.size() - 1));
        List<Long> currentSequence = measurementValues;
        while (true) {
            List<Long> newSequence = new ArrayList<>();
            for (int i = 1; i < currentSequence.size(); i++) {
                long difference = currentSequence.get(i) - currentSequence.get(i - 1);
                newSequence.add(difference);
            }
            lastValues.addLast(newSequence.get(newSequence.size() - 1));
            if (newSequence.stream().allMatch(l -> l == 0)) {
                break;
            } else {
                currentSequence = newSequence;
            }
        }
        return lastValues.stream().mapToLong(Long::longValue).sum();
    }

    private List<Long> reverse(List<Long> measurementValues) {
        List<Long> reversedList = new ArrayList<>(measurementValues.size());
        reversedList.addAll(measurementValues);
        Collections.reverse(reversedList);
        return reversedList;
    }
}
