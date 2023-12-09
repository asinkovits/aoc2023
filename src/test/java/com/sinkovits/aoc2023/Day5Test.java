package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

    private final Day5 underTest = new Day5();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(313045984L, result);
    }

    @Test
    @Disabled
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(20283860L, result);
    }
}