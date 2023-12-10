package com.sinkovits.aoc2023;

import lombok.extern.slf4j.Slf4j;

import static com.sinkovits.aoc2023.Day10.Context;

@Slf4j
public class Day10 extends AbstractDay<Context> {

    public static class Context {}

    public Day10() {
        super("test", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {}

    @Override
    protected long calculateFirst(Context context) {
        return 0;
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {}

    @Override
    protected long calculateSecond(Context context) {
        return 0;
    }
}
