package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {

    private final Day6 underTest = new Day6();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(6209190L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(28545089L, result);
    }
}
