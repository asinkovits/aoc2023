package com.sinkovits.aoc2023;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@Slf4j
public class Day2 implements AdventOfCodeDailyExercise {

    private enum Color {
        RED, BLUE, GREEN
    }

    private record ColorAndNumber(Color color, int number) implements Comparable<ColorAndNumber> {
        static ColorAndNumber of(Color color, int number) {
            return new ColorAndNumber(color, number);
        }

        static ColorAndNumber parse(String input) {
            String[] split = input.split(StringUtils.SPACE);
            return of(Color.valueOf(split[1].toUpperCase()), Integer.parseInt(split[0]));
        }


        @Override
        public int compareTo(ColorAndNumber o) {
            return Comparator
                    .comparingInt(ColorAndNumber::number)
                    .compare(this, o);
        }
    }

    @Builder
    private record SetOfCubes(ColorAndNumber reds, ColorAndNumber greens, ColorAndNumber blues) {
        static SetOfCubes of(ColorAndNumber reds, ColorAndNumber greens, ColorAndNumber blues) {
            return new SetOfCubes(reds, greens, blues);
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

        public boolean isValid(SetOfCubes configuration) {
            if (this.reds != null && this.reds.compareTo(configuration.reds) > 0) {
                return false;
            }
            if (this.greens != null && this.greens.compareTo(configuration.greens) > 0) {
                return false;
            }
            if (this.blues != null && this.blues.compareTo(configuration.blues) > 0) {
                return false;
            }
            return true;
        }
    }

    @Builder
    private record Game(int id, List<SetOfCubes> sets) {
        static Game parse(String line) {
            //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game.GameBuilder builder = Game.builder();
            String[] gameAndSets = line.split(":");
            builder.id(Integer.parseInt(gameAndSets[0].split(" ")[1]));
            String[] sets = gameAndSets[1].split(";");
            builder.sets(Arrays.stream(sets)
                    .map(SetOfCubes::parse)
                    .toList());
            return builder.build();
        }

        public boolean isValid(SetOfCubes configuration) {
            return sets
                    .stream()
                    .allMatch(set -> set.isValid(configuration));
        }
    }

    private void processLineSolution1(String line, CountingContext context) {
        Game game = Game.parse(line);
        if (game.isValid(CONFIGURATION)) {
            context.add(game.id());
        }
    }

    private static final SetOfCubes CONFIGURATION = SetOfCubes.of(
            ColorAndNumber.of(Color.RED, 12),
            ColorAndNumber.of(Color.GREEN, 13),
            ColorAndNumber.of(Color.BLUE, 14)
    );

    @Override
    public void solveFirst() {
        LineProcessor<CountingContext> lineProcessor = getDay2ContextLineReader();
        CountingContext context = lineProcessor.processLines(this::processLineSolution1);
        log.info("Solution for the first exercise: {}", context.getSum());
    }

    @Override
    public void solveSecond() {

    }

    private static LineProcessor<CountingContext> getDay2ContextLineReader() {
        return new LineProcessor<>(
                Path.of("C:\\Users\\Anti\\dev\\aoc2023\\src\\main\\resources\\input_day2"),
                new CountingContext()
        );
    }
}
