package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test {

    Day7 day = new Day7();

    @Test
    void solveFirst() {
        // When
        long result = day.solveFirst();

        // Then
        assertEquals(256448566, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day.solveSecond();

        // Then
        assertEquals(254412181, result);
    }
}