package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sinkovits.aoc2023.Day3.Coordinate;
import static com.sinkovits.aoc2023.Day3.PotentialPartNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {

    private final Day3 underTest = new Day3();

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(527364L, result);
    }

    @Test
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(79026871L, result);
    }

    static Stream<Arguments> provideCoordinatesForAdjTest() {
        return Stream.of(
                //
                Arguments.of(PotentialPartNumber.of(123, 0, 0), Coordinate.of(3, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 0, 0), Coordinate.of(4, 0), false),
                Arguments.of(PotentialPartNumber.of(123, 1, 0), Coordinate.of(0, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(0, 0), false),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(0, 1), false),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(1, 1), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(2, 1), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(3, 1), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(4, 1), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(5, 1), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 0), Coordinate.of(6, 1), false),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(0, 0), false),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(1, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(2, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(3, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(4, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(5, 0), true),
                Arguments.of(PotentialPartNumber.of(123, 2, 1), Coordinate.of(6, 0), false));
    }

    @ParameterizedTest
    @MethodSource("provideCoordinatesForAdjTest")
    void adj(PotentialPartNumber ppn, Coordinate s, boolean expected) {
        assertEquals(expected, Day3.AdjacencyCalculator.isAdjacent(ppn, s));
    }
}
