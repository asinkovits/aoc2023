package com.sinkovits.aoc2023;

import com.sinkovits.aoc2023.Day11.Context;

public class Day11 extends AbstractDay<Context> {

    public Day11() {
        // super("input_day11", Context.class);
        super("test", Context.class);
    }

    // n(n-1)/2
    // manhattan distance

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

    protected static class Context {}

    record Coordinate(int x, int y) {
        static Coordinate of(int x, int y) {
            return new Coordinate(x, y);
        }
    }
}
