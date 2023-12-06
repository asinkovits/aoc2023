package com.sinkovits.aoc2023;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParsingUtil {

    private static final int RADIX = 10;

    public static List<Long> parseNumbers(String input) {
        char previous = 0;
        List<Long> result = new ArrayList<>();
        long value = 0;
        for (char aChar : input.toCharArray()) {
            if (Character.isDigit(aChar)) {
                value = (value * RADIX) + Character.digit(aChar, RADIX);
            } else {
                if(aChar != previous){
                    result.add(value);
                    value = 0;
                }
            }
            previous = aChar;
        }
        result.add(value);
        return result;
    }
}
