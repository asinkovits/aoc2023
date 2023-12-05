package com.sinkovits.aoc2023;

import java.util.Arrays;

public class Main {

    public static final AdventOfCodeDailyExercise[] EXERCISES = {
            new Day1(),
            new Day2(),
            new Day3(),
            new Day4(),
            new Day5()
    };

    public static void main(String[] args) {
        if (args.length < 1) {
            solveAll();
        } else {
            int day = Integer.parseInt(args[0]);
            solve(EXERCISES[day - 1]);
        }
    }

    private static void solveAll() {
        Arrays.stream(EXERCISES).forEach(
                Main::solve
        );
    }

    private static void solve(AdventOfCodeDailyExercise exercise) {
        exercise.solveFirst();
        exercise.solveSecond();
    }
}