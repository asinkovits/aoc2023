package com.sinkovits.aoc2023;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParsingUtilTest {

    static Stream<Arguments> provideParseTestData() {
        return Stream.of(
                Arguments.of("1,1,2",
                        List.of(1L, 1L, 2L)),
                Arguments.of("-7 -3 12 48 137 352 825 1759 3441 6286 10983 18885 32900 59312 111199 214421 417517 807255 1531986 2835306 5102751",
                        List.of(-7L, -3L, 12L, 48L, 137L, 352L, 825L, 1759L, 3441L, 6286L, 10983L, 18885L, 32900L, 59312L, 111199L, 214421L, 417517L, 807255L, 1531986L, 2835306L, 5102751L)),
                Arguments.of("215   1064   1505   1100",
                        List.of(215L, 1064L, 1505L, 1100L)),
                Arguments.of("3929882452 3989920675 212758268",
                        List.of(3929882452L, 3989920675L, 212758268L)),
                Arguments.of("2 19 58 83 91 76  9 63 71 77",
                        List.of(2L, 19L, 58L, 83L, 91L, 76L, 9L, 63L, 71L, 77L)),
                Arguments.of("21 79  3 31  1 36 85 17 59 30 87 46 27 63 75 56 72 67 11 42 84 62 53 68 38",
                        List.of(21L, 79L, 3L, 31L, 1L, 36L, 85L, 17L, 59L, 30L, 87L, 46L, 27L, 63L, 75L, 56L, 72L, 67L, 11L, 42L, 84L, 62L, 53L, 68L, 38L))

        );
    }

    @ParameterizedTest
    @MethodSource("provideParseTestData")
    void parseNumbers(String input, List<Long> expected) {
        // When
        List<Long> result = ParsingUtil.parseNumbers(input);

        // Then
        assertEquals(expected, result);
    }
}