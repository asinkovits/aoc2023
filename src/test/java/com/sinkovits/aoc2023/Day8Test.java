package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    Day8 day = new Day8();

    @Test
    void solveFirst() {
        // When
        long result = day.solveFirst();

        // Then
        assertEquals(17621, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day.solveSecond();

        // Then
        assertEquals(20685524831999L, result);
    }
}