package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    private final Day11 underTest = new Day11();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(9734203L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(568914596391L, result);
    }
}
