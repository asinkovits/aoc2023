package com.sinkovits.aoc2023;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Slf4j
public class Day2 implements AdventOfCodeDailyExercise {

    private static final SetOfCubes SOLUTION_1_CONSTRAINTS = SetOfCubes.of(
            ColorAndNumber.of(Color.RED, 12),
            ColorAndNumber.of(Color.GREEN, 13),
            ColorAndNumber.of(Color.BLUE, 14)
    );

    private enum Color {
        RED, BLUE, GREEN
    }

    private record ColorAndNumber(Color color, int number) implements Comparable<ColorAndNumber> {
        static final ColorAndNumber EMPTY_RED = ColorAndNumber.of(Color.RED, 0);
        static final ColorAndNumber EMPTY_GREEN = ColorAndNumber.of(Color.GREEN, 0);
        static final ColorAndNumber EMPTY_BLUE = ColorAndNumber.of(Color.BLUE, 0);
        static final Comparator<ColorAndNumber> COMPARATOR = Comparator
                .comparingInt(ColorAndNumber::number);

        static ColorAndNumber of(Color color, int number) {
            return new ColorAndNumber(color, number);
        }

        static ColorAndNumber parse(String input) {
            String[] split = input.split(StringUtils.SPACE);
            return of(Color.valueOf(split[1].toUpperCase()), Integer.parseInt(split[0]));
        }

        @Override
        public int compareTo(ColorAndNumber o) {
            return COMPARATOR
                    .compare(this, o);
        }
    }

    @Builder
    @RequiredArgsConstructor
    private static class SetOfCubes {
        @NonNull
        @Builder.Default
        private final ColorAndNumber reds = ColorAndNumber.EMPTY_RED;
        @NonNull
        @Builder.Default
        private final ColorAndNumber greens = ColorAndNumber.EMPTY_BLUE;
        @NonNull
        @Builder.Default
        private final ColorAndNumber blues = ColorAndNumber.EMPTY_GREEN;

        static SetOfCubes of(ColorAndNumber reds, ColorAndNumber greens, ColorAndNumber blues) {
            return SetOfCubes.builder()
                    .reds(reds)
                    .greens(greens)
                    .blues(blues)
                    .build();
        }

        static SetOfCubes parse(String input) {
            SetOfCubesBuilder builder = SetOfCubes.builder();
            String[] colorsAndNumbers = input.trim().split(",");
            for (String colorAndNumberString : colorsAndNumbers) {
                ColorAndNumber colorAndNumber = ColorAndNumber.parse(colorAndNumberString.trim());
                switch (colorAndNumber.color) {
                    case RED -> builder.reds(colorAndNumber);
                    case GREEN -> builder.greens(colorAndNumber);
                    case BLUE -> builder.blues(colorAndNumber);
                }
            }
            return builder.build();
        }

        public boolean isValid(SetOfCubes constraints) {
            return reds.compareTo(constraints.reds) <= 0 &&
                    greens.compareTo(constraints.greens) <= 0 &&
                    blues.compareTo(constraints.blues) <= 0;
        }

        int power() {
            return reds.number * greens.number * blues.number;
        }
    }

    @Builder
    private record Game(int id, List<SetOfCubes> sets) {
        static Game parse(String line) {
            //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game.GameBuilder builder = Game.builder();
            String[] gameAndSets = line.split(":");
            builder.id(Integer.parseInt(gameAndSets[0].split(StringUtils.SPACE)[1]));
            String[] sets = gameAndSets[1].split(";");
            builder.sets(Arrays.stream(sets)
                    .map(SetOfCubes::parse)
                    .toList());
            return builder.build();
        }

        public boolean isValid(SetOfCubes constraints) {
            return sets
                    .stream()
                    .allMatch(set -> set.isValid(constraints));
        }

        SetOfCubes fewestNumberCubesRequired() {
            Optional<ColorAndNumber> red = sets
                    .stream()
                    .map(setOfCubes -> setOfCubes.reds)
                    .max(ColorAndNumber::compareTo);

            Optional<ColorAndNumber> green = sets
                    .stream()
                    .map(setOfCubes -> setOfCubes.greens)
                    .max(ColorAndNumber::compareTo);

            Optional<ColorAndNumber> blue = sets
                    .stream()
                    .map(setOfCubes -> setOfCubes.blues)
                    .max(ColorAndNumber::compareTo);

            return SetOfCubes.of(
                   red.orElseThrow(),
                   green.orElseThrow(),
                   blue.orElseThrow()
            );
        }
    }

    private void processLineSolution1(String line, CountingContext context) {
        Game game = Game.parse(line);
        if (game.isValid(SOLUTION_1_CONSTRAINTS)) {
            context.add(game.id());
        }
    }

    private void processLineSolution2(String line, CountingContext context) {
        Game game = Game.parse(line);
        SetOfCubes fewestNumberCubesRequired = game.fewestNumberCubesRequired();
        context.add(fewestNumberCubesRequired.power());
    }

    @Override
    public void solveFirst() {
        LineProcessor<CountingContext> lineProcessor = getDay2ContextLineReader();
        CountingContext context = lineProcessor.processLines(this::processLineSolution1);
        log.info("Solution for the first exercise: {}", context.getSum());
    }

    @Override
    public void solveSecond() {
        LineProcessor<CountingContext> lineProcessor = getDay2ContextLineReader();
        CountingContext context = lineProcessor.processLines(this::processLineSolution2);
        log.info("Solution for the second exercise: {}", context.getSum());
    }

    private static LineProcessor<CountingContext> getDay2ContextLineReader() {
        return new LineProcessor<>(
                Path.of("C:\\Users\\Anti\\dev\\aoc2023\\src\\main\\resources\\input_day2"),
                new CountingContext()
        );
    }
}
