package com.sinkovits.aoc2023;

import com.sinkovits.aoc2023.Day11.Context;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 extends AbstractDay<Context> {

    public Day11() {
        super("input_day11", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        char[] charArray = line.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '#') {
                context.galaxies.add(Vector.of(i, lineNumber));
            }
        }
        context.sizeX = charArray.length;
        context.sizeY++;
    }

    @Override
    protected long calculateFirst(Context context) {
        return calculateSolution(context, 1);
    }

    private long calculateSolution(Context context, int factor) {
        expandUniverse(context, factor);
        List<Pair<Vector, Vector>> galaxyPairs = createGalaxyPairs(context);
        return galaxyPairs
                .stream()
                .mapToLong(this::manhattanDistance)
                .sum();
    }

    private int manhattanDistance(Pair<Vector, Vector> pair) {
        return manhattanDistance(pair.getLeft(), pair.getRight());
    }

    private int manhattanDistance(Vector left, Vector right) {
        return Math.abs(left.x - right.x)
                + Math.abs(left.y - right.y);
    }

    private List<Pair<Vector, Vector>> createGalaxyPairs(Context context) {
        List<Pair<Vector, Vector>> result = new ArrayList<>();
        List<Vector> list = context.galaxies.stream().toList();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                result.add(Pair.of(list.get(i), list.get(j)));
            }
        }
        return result;
    }

    private static void expandUniverse(Context context, int factor) {
        Set<Integer> columnsWithGalaxies =
                context.galaxies.stream().map(Vector::getX).collect(Collectors.toSet());
        Set<Integer> rowsWithGalaxies =
                context.galaxies.stream().map(Vector::getY).collect(Collectors.toSet());
        List<Integer> columnsWithoutGalaxies =
                IntStream.range(0, context.sizeX)
                        .boxed()
                        .filter(Predicate.not(columnsWithGalaxies::contains))
                        .sorted()
                        .toList();
        List<Integer> rowsWithoutGalaxies =
                IntStream.range(0, context.sizeY)
                        .boxed()
                        .filter(Predicate.not(rowsWithGalaxies::contains))
                        .sorted()
                        .toList();

        context.sizeX += (columnsWithoutGalaxies.size() * factor);
        context.sizeY += (rowsWithoutGalaxies.size() * factor);

        for (int i = 0; i < columnsWithoutGalaxies.size(); i++) {
            for (Vector galaxy : context.galaxies) {
                if (galaxy.x > (columnsWithoutGalaxies.get(i) + (i * factor))) {
                    galaxy.mutate(Vector.X.mul(factor));
                }
            }
        }

        for (int i = 0; i < rowsWithoutGalaxies.size(); i++) {
            for (Vector galaxy : context.galaxies) {
                if (galaxy.y > (rowsWithoutGalaxies.get(i) + (i * factor))) {
                    galaxy.mutate(Vector.Y.mul(factor));
                }
            }
        }
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        parseFirst(lineNumber, line, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        return calculateSolution(context, 999999);
    }

    protected static class Context {
        Set<Vector> galaxies = new TreeSet<>(Comparator.comparing(Vector::getX).thenComparing(Vector::getY));
        int sizeX;
        int sizeY;
    }

    @Data
    @AllArgsConstructor
    protected static class Vector {

        public static Vector X = Vector.of(1, 0);
        public static Vector Y = Vector.of(0, 1);

        private int x;
        private int y;

        public static Vector of(int x, int y) {
            return new Vector(x, y);
        }

        public Vector add(Vector other) {
            return new Vector(this.x + other.x, this.y + other.y);
        }

        public Vector mul(int factor) {
            return new Vector(this.x * factor, this.y * factor);
        }

        public void mutate(Vector other) {
            this.x += other.x;
            this.y += other.y;
        }
    }
}
