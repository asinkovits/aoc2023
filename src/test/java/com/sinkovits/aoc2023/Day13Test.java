package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    private final Day13 underTest = new Day13();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(405L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(0L, result);
    }
}
