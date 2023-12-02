package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    private final Day2 day2 = new Day2();

    @Test
    void solveFirst() {
        // When
        int result = day2.solveFirst();

        // Then
        assertEquals(2076, result);
    }

    @Test
    void solveSecond() {
        // When
        int result = day2.solveSecond();

        // Then
        assertEquals(70950, result);
    }
}