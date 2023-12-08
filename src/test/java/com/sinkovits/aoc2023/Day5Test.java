package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

    Day5 day5 = new Day5();

    @Test
    void solveFirst() {
        // When
        long result = day5.solveFirst();

        // Then
        assertEquals(313045984, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day5.solveSecond();

        // Then
        assertEquals(20283860, result);
    }
}