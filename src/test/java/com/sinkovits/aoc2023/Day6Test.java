package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

    Day6 day  = new Day6();

    @Test
    void solveFirst() {
        // When
        long result = day.solveFirst();

        // Then
        assertEquals(6209190, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day.solveSecond();

        // Then
        assertEquals(28545089, result);
    }
}