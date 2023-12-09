package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

    private final Day7 underTest = new Day7();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(256448566L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(254412181L, result);
    }
}
