package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day8Test {

    private final Day8 underTest = new Day8();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(17621L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(20685524831999L, result);
    }
}
