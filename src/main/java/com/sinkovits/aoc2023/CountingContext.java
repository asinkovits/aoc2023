package com.sinkovits.aoc2023;

import lombok.Getter;

@Getter
class CountingContext {
    private int sum = 0;

    public void add(int value) {
        sum += value;
    }
}
