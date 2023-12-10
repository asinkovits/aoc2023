package com.sinkovits.aoc2023;

import java.util.Arrays;

public class Main {

    public static final AdventOfCodeDailyExercise[] EXERCISES = {
        new Day1(),
        new Day2(),
        new Day3(),
        new Day4(),
        new Day5(),
        new Day6(),
        new Day7(),
        new Day8(),
        new Day9(),
        new Day10(),
    };

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                solveAll();
            } else {
                int day = Integer.parseInt(args[0]);
                solve(EXERCISES[day - 1]);
            }
        } catch (Exception e) {
            printUsage();
            System.exit(1);
        }
    }

    private static void solveAll() {
        Arrays.stream(EXERCISES).forEach(Main::solve);
    }

    private static void solve(AdventOfCodeDailyExercise exercise) {
        exercise.solveFirst();
        exercise.solveSecond();
    }

    private static void printUsage() {
        System.out.println("Usage: java Main <day>");
        System.out.println("<day> is the day of the Advent of Code exercise you want to solve.");
        System.out.printf("It should be an integer between 1 and %d.\n", EXERCISES.length);
        System.out.println("If no argument is provided, all exercises will be solved.");
    }
}
