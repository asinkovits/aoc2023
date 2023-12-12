package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.sinkovits.aoc2023.Day10.Context;

@Slf4j
public class Day10 extends AbstractDay<Context> {

    public Day10() {
        // super("input_day10", Context.class);
        super("input_day10", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        char[] charArray = line.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            Coordinate coordinate = new Coordinate(i, lineNumber);
            context.map.put(coordinate, charArray[i]);
            if (charArray[i] == 'S') {
                context.start = coordinate;
            }
        }
    }

    @Override
    protected long calculateFirst(Context context) {
        long lengthOfAnimalsLoop = findLengthOfAnimalsLoop(context.start, context.map);
        return lengthOfAnimalsLoop / 2 + 1;
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        parseFirst(lineNumber, line, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        return findLoop(context.start, context.map);
    }

    private long findLengthOfAnimalsLoop(Coordinate start, Map<Coordinate, Character> map) {
        Coordinate max =
                map.keySet().stream()
                        .max(Comparator.comparing(Coordinate::getX).thenComparing(Coordinate::getY))
                        .map(Coordinate.SOUTH::add)
                        .map(Coordinate.EAST::add)
                        .orElseThrow();
        List<Coordinate> neighbours = getValidNeighbourPipes(start, map, max);
        return neighbours.stream().findFirst().stream()
                .mapToLong(coordinate -> calculateLengthOfTheLoop(start, coordinate, map))
                .filter(distance -> distance != -1)
                .min()
                .orElseThrow();
    }

    private long findLoop(Coordinate start, Map<Coordinate, Character> map) {
        Coordinate max =
                map.keySet().stream()
                        .max(Comparator.comparing(Coordinate::getX).thenComparing(Coordinate::getY))
                        .map(Coordinate.SOUTH::add)
                        .map(Coordinate.EAST::add)
                        .orElseThrow();
        List<Coordinate> neighbours = getValidNeighbourPipes(start, map, max);
        Coordinate firstSection = neighbours.get(0);

        List<Coordinate> path =
                calculatePathOfTheLoop(start, firstSection, map).stream()
                        .map(Pair::getLeft)
                        .toList();
        Set<Coordinate> pathCoordinateSet = new HashSet<>(path);
        Set<Coordinate> outsExcludedByFill =
                markSectionsOutOfTheLoop(Coordinate.of(0, 0), pathCoordinateSet, max);

        List<Coordinate> leftover =
                map.keySet().stream()
                        .filter(Predicate.not(outsExcludedByFill::contains))
                        .filter(Predicate.not(pathCoordinateSet::contains))
                        .filter(coordinate -> isInPath(coordinate, path))
                        .sorted(
                                Comparator.comparing(Coordinate::getX)
                                        .thenComparing(Coordinate::getY))
                        .toList();

        return leftover.size();
    }

    private boolean isInPath(Coordinate inspectedCoordinate, List<Coordinate> path) {
        //int j = path.size() - 1;
        boolean c = false;
        Coordinate previous = path.get(path.size() - 1);
        for (Coordinate current : path) {
            if (inspectedCoordinate.equals(current)) {
                return false;
            }

            if ((current.y > inspectedCoordinate.y)
                    != (previous.y > inspectedCoordinate.y)) {
                int slope =
                        (inspectedCoordinate.x - current.x) * (previous.y - current.y)
                                - (previous.x - current.x)
                                * (inspectedCoordinate.y - current.y);
                if (slope == 0) {
                    // point is on boundary
                    return true;
                }
                if ((slope < 0) != (previous.y < current.y)) {
                    c = !c;
                }
            }
            previous = current;
        }

        return c;
    }

    private Set<Coordinate> markSectionsOutOfTheLoop(
            Coordinate topLeft, Set<Coordinate> path, Coordinate max) {
        Set<Coordinate> result = new HashSet<>();
        HashSet<Coordinate> processed = new HashSet<>(path);
        Queue<Coordinate> q = new LinkedList<>();
        q.add(topLeft);
        while (!q.isEmpty()) {
            Coordinate current = q.remove();
            processed.add(current);

            List<Coordinate> neighbours =
                    Stream.of(
                                    current.add(Coordinate.NORTH),
                                    current.add(Coordinate.SOUTH),
                                    current.add(Coordinate.EAST),
                                    current.add(Coordinate.WEST))
                            .filter(coordinate -> coordinate.isValid(max))
                            .filter(Predicate.not(processed::contains))
                            .toList();
            q.addAll(neighbours);
            processed.addAll(neighbours);
            result.add(current);
        }
        return result;
    }

    private List<Pair<Coordinate, Character>> calculatePathOfTheLoop(
            Coordinate start, Coordinate firstSection, Map<Coordinate, Character> map) {
        List<Pair<Coordinate, Character>> result = new ArrayList<>();
        result.add(Pair.of(start, 'S'));

        Coordinate previous = start;
        Coordinate current = firstSection;
        while (true) {
            result.add(Pair.of(current, map.get(current)));
            Optional<Coordinate> nextPipeSection = getNextValidSection(previous, current, map);
            Coordinate next = nextPipeSection.orElseThrow();
            if (map.get(next) == 'S') {
                break;
            }
            previous = current;
            current = next;
        }
        return result;
    }

    private long calculateLengthOfTheLoop(
            Coordinate start, Coordinate firstSection, Map<Coordinate, Character> map) {
        Map<Coordinate, Long> distaces = new HashMap<>();
        distaces.put(start, 0L);

        Coordinate previous = start;
        Coordinate current = firstSection;
        while (true) {
            Optional<Coordinate> nextPipeSection = getNextValidSection(previous, current, map);
            if (nextPipeSection.isEmpty()) {
                return -1;
            }
            long currentDistance = distaces.get(previous) + 1;
            Coordinate next = nextPipeSection.get();
            if (map.get(next) == 'S') {
                return currentDistance;
            }
            distaces.put(current, currentDistance);
            previous = current;
            current = next;
        }
    }

    Optional<Coordinate> getNextValidSection(
            Coordinate previous, Coordinate current, Map<Coordinate, Character> map) {
        Character symbol = map.get(current);
        Pipe pipe = Pipe.bySymbol(symbol);
        return pipe.neighboursRelativeCoordinate.stream()
                .map(current::add)
                .filter(Predicate.not(previous::equals))
                .filter(coordinate -> map.get(coordinate) != '.')
                .findFirst();
    }

    private List<Coordinate> getValidNeighbourPipes(
            Coordinate start, Map<Coordinate, Character> map, Coordinate max) {
        return Stream.of(
                        start.add(Coordinate.NORTH),
                        start.add(Coordinate.SOUTH),
                        start.add(Coordinate.EAST),
                        start.add(Coordinate.WEST))
                .filter(coordinate -> coordinate.isValid(max))
                .filter(coordinate -> map.get(coordinate) != '.')
                .filter(coordinate -> canEnterFrom(start, coordinate, map))
                .toList();
    }

    private boolean canEnterFrom(
            Coordinate start, Coordinate pipe, Map<Coordinate, Character> map) {
        return Pipe.bySymbol(map.get(pipe)).neighboursRelativeCoordinate.stream()
                .map(pipe::add)
                .anyMatch(start::equals);
    }

    public static class Context {
        Map<Coordinate, Character> map = new HashMap<>();
        Coordinate start;
    }

    @Value
    static class Coordinate {

        int x;
        int y;

        public static final Coordinate NORTH = Coordinate.of(0, -1);
        public static final Coordinate SOUTH = Coordinate.of(0, 1);
        public static final Coordinate EAST = Coordinate.of(1, 0);
        public static final Coordinate WEST = Coordinate.of(-1, 0);

        public static Coordinate of(int x, int y) {
            return new Coordinate(x, y);
        }

        public Coordinate add(Coordinate other) {
            return new Coordinate(this.x + other.x, this.y + other.y);
        }

        public boolean isValid(Coordinate max) {
            return this.x >= 0 && this.y >= 0 && this.x < max.x && this.y < max.y;
        }
    }

    @RequiredArgsConstructor
    enum Pipe {
        NS('|', List.of(Coordinate.NORTH, Coordinate.SOUTH)),
        EW('-', List.of(Coordinate.EAST, Coordinate.WEST)),
        NE('L', List.of(Coordinate.NORTH, Coordinate.EAST)),
        NW('J', List.of(Coordinate.NORTH, Coordinate.WEST)),
        SW('7', List.of(Coordinate.SOUTH, Coordinate.WEST)),
        SE('F', List.of(Coordinate.SOUTH, Coordinate.EAST));

        final Character symbol;
        final List<Coordinate> neighboursRelativeCoordinate;

        private static final Map<Character, Pipe> lookupBySymbol =
                Map.of(
                        NS.symbol, NS,
                        EW.symbol, EW,
                        NE.symbol, NE,
                        NW.symbol, NW,
                        SW.symbol, SW,
                        SE.symbol, SE);

        public static Pipe bySymbol(Character symbol) {
            return lookupBySymbol.get(symbol);
        }
    }
}
