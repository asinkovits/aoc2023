package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;


@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDay<T> implements AdventOfCodeDailyExercise {

    private final String input;
    private final Class<T> contextClazz;

    @Override
    public long solveFirst() {
        LineProcessor<T> lineProcessor = getContextLineReader(input);
        T context = lineProcessor.processLines(this::parseFirst);
        long result = calculateFirst(context);
        log.info("Solution for the first exercise: {}", result);
        return result;
    }

    @Override
    public long solveSecond() {
        LineProcessor<T> lineProcessor = getContextLineReader(input);
        T context = lineProcessor.processLines(this::parseSecond);
        long result = calculateSecond(context);
        log.info("Solution for the second exercise: {}", result);
        return result;
    }

    protected LineProcessor<T> getContextLineReader(String input) {
        try {
            return new LineProcessor<>(
                    Path.of(input),
                    contextClazz.getDeclaredConstructor().newInstance()
            );
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void parseFirst(Integer lineNumber, String line, T context);

    protected abstract long calculateFirst(T context);

    protected abstract void parseSecond(Integer lineNumber, String line, T context);

    protected abstract long calculateSecond(T context);
}
