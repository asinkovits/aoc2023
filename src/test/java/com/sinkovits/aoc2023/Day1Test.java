package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    private final Day1 day1 = new Day1();

    @Test
    void solveFirst() {
        // When
        long result = day1.solveFirst();

        // Then
        assertEquals(55130, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = day1.solveSecond();

        // Then
        assertEquals(54985, result);
    }
}