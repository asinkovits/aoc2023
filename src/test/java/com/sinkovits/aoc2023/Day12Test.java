package com.sinkovits.aoc2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    private final Day12 underTest = new Day12();

    public static Stream<Arguments> contexts() {
        return Stream.of(
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("... 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("..# 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow(".## 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow(".?# 3"))), 0),

//                Arguments.of(new Day12.Context(List.of(Day12.createRow("#.. 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("#.. 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("#.. 3"))), 0),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("?## 3"))), 1),
//                Arguments.of(new Day12.Context(List.of(Day12.createRow("?## 3"))), 1),
                Arguments.of(new Day12.Context(List.of(Day12.createRow("#.# 3"))), 1));
    }

    @Test
    void solveFirst() {
        // When
        long result = underTest.solveFirst();

        // Then
        assertEquals(7084L, result);
    }

    @Test
    @Disabled
    void solveSecond() {
        // When
        long result = underTest.solveSecond();

        // Then
        assertEquals(525152L, result);
    }

    //@Disabled
    @ParameterizedTest
    @MethodSource("contexts")
    void testDp(Day12.Context context, int expected) {
        // Given
        Day12.SpringRow springRow = context.rows.get(0);

        // When
        int result = underTest.calculateArrangements(springRow.data, springRow.brokenSprings);

        // Then
        assertEquals(expected, result);
    }
}
