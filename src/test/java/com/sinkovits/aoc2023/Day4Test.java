package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

    private final Day4 day4 = new Day4();

    @Test
    @Disabled
    void solveFirst() {
        // When
        int result = day4.solveFirst();

        // Then
        assertEquals(17803, result);
    }

    @Test
    void solveSecond() {
        // When
        int result = day4.solveSecond();

        // Then
        assertEquals(5554894, result);
    }
}