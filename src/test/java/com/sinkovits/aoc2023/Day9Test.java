package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day9Test {

    Day9 day = new Day9();

    @Test
    void solveFirst() {
        // When
        long result = day.solveFirst();

        // Then
        assertEquals(1980437560L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day.solveSecond();

        // Then
        assertEquals(977L, result);
    }
}