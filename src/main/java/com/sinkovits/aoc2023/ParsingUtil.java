package com.sinkovits.aoc2023;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParsingUtil {

    private static final int RADIX = 10;
    private static final String NON_NUMERIC_REGEX = "[^0-9]";

    public static List<Long> parseNumbers(String input) {
        char previous = 0;
        List<Long> result = new ArrayList<>();
        long value = 0;
        boolean negative = false;
        for (char aChar : input.toCharArray()) {
            if (Character.isDigit(aChar)) {
                value = (value * RADIX) + Character.digit(aChar, RADIX) * (negative ? -1 : 1);
            } else if (aChar == '-') {
                negative = true;
            } else {
                negative = false;
                if (aChar != previous) {
                    result.add(value);
                    value = 0;
                }
            }
            previous = aChar;
        }
        result.add(value);
        return result;
    }

    public static String removeNonNumeric(String input) {
        return input.replaceAll(NON_NUMERIC_REGEX, StringUtils.EMPTY);
    }
}
