package com.sinkovits.aoc2023;

import lombok.Getter;

@Getter
public class CountingContext {
    private long sum = 0;

    public void add(long value) {
        sum += value;
    }
}
