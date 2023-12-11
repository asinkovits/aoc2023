package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sinkovits.aoc2023.Day10.Context;

@Slf4j
public class Day10 extends AbstractDay<Context> {

    public Day10() {
        //super("input_day10", Context.class);
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

        List<Pair<Coordinate, Character>> path = calculatePathOfTheLoop(start, firstSection, map);
        Set<Coordinate> pathCoordinateSet = path.stream().map(Pair::getLeft).collect(Collectors.toSet());
        List<Coordinate> pathCoordinateList = path.stream().map(Pair::getLeft).toList();
        Set<Coordinate> outsExcludedByFill = markSectionsOutOfTheLoop(Coordinate.of(0, 0), pathCoordinateSet, max);

        List<Coordinate> leftover = map.keySet()
                .stream()
                .filter(Predicate.not(outsExcludedByFill::contains))
                .filter(Predicate.not(pathCoordinateSet::contains))
                .filter(coordinate -> isInPath(coordinate, pathCoordinateList))
                .sorted(Comparator.comparing(Coordinate::getX).thenComparing(Coordinate::getY))
                .toList();


        List<Pair<Coordinate, Long>> list1 = leftover
                .stream()
                .map(coordinate -> Pair.of(coordinate, countPathCrossingOnX(coordinate, pathCoordinateSet, map)))
                .filter(pair -> pair.getRight() % 2 != 0)
                .toList();

        List<Pair<Coordinate, Long>> list2 = leftover
                .stream()
                .map(coordinate -> Pair.of(coordinate, countPathCrossingOnY(coordinate, pathCoordinateSet, map)))
                .filter(pair -> pair.getRight() % 2 != 0)
                .toList();

        List<Coordinate> ins = Stream.concat(list2.stream(), list1.stream()).map(Pair::getLeft).toList();

        draw(path, outsExcludedByFill, leftover, max, "path.out");
        //draw(path, List.of(), List.of(), max, "path.out");

//        Set<Coordinate> s1 = list.stream().map(Pair::getLeft).collect(Collectors.toSet());
//        Set<Coordinate> s2 = list2.stream().map(Pair::getLeft).collect(Collectors.toSet());
//
//        List<Coordinate> list1 = leftover.stream()
//                .filter(s1::contains)
//                .filter(s2::contains)
//                .sorted(Comparator.comparing(Coordinate::getX).thenComparing(Coordinate::getY))
//                .toList();


        return leftover.size();
    }

    private boolean isInPath(Coordinate inspectedCoordinate, List<Coordinate> path) {
        int j = path.size() - 1;
        boolean c = false;
        for (int i = 0; i < path.size(); i++) {
            if(inspectedCoordinate.equals(path.get(i))){
                return true;
            }

            if ((path.get(i).y > inspectedCoordinate.y) != (path.get(j).y > inspectedCoordinate.y)) {
                int slope = (inspectedCoordinate.x - path.get(i).x) * (path.get(j).y - path.get(i).y) - (path.get(j).x - path.get(i).x) * (inspectedCoordinate.y - path.get(i).y);
                if (slope == 0) {
                    //point is on boundary
                    return true;
                }
            if ((slope < 0) != (path.get(j).y < path.get(i).y)) {
                    c = !c;
                }
            }
            j = i;
        }

        return c;
    }

    private long countPathCrossingOnX(Coordinate coordinate, Set<Coordinate> pathCoordinateSet, Map<Coordinate, Character> map) {
        long count = IntStream.range(0, coordinate.getX())
                .mapToObj(x -> Coordinate.of(x, coordinate.y))
                .filter(pathCoordinateSet::contains)
                .filter(c -> !isHorizontal(c, map))
                .count();
        return count;
    }

    private long countPathCrossingOnY(Coordinate coordinate, Set<Coordinate> pathCoordinateSet, Map<Coordinate, Character> map) {
        long count = IntStream.range(0, coordinate.getY())
                .mapToObj(y -> Coordinate.of(coordinate.x, y))
                .filter(pathCoordinateSet::contains)
                .filter(c -> !isVertical(c, map))
                .count();
        return count;
    }

    private boolean isVertical(Coordinate coordinate, Map<Coordinate, Character> map) {
        Character symbol = map.get(coordinate);
        Pipe pipe = Pipe.bySymbol(symbol);
        return pipe == Pipe.NS;
    }

    private boolean isHorizontal(Coordinate coordinate, Map<Coordinate, Character> map) {
        Character symbol = map.get(coordinate);
        Pipe pipe = Pipe.bySymbol(symbol);
        return pipe == Pipe.EW;
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

    private void draw(
            List<Pair<Coordinate, Character>> path,
            Collection<Coordinate> out,
            Collection<Coordinate> in,
            Coordinate max,
            String filename) {
        // Create a 2D array to store the map
        char[][] map = new char[max.y][max.x];

        // Fill the map with '.'
        for (char[] row : map) {
            Arrays.fill(row, '.');
        }

        // Update the map with the path
        for (Pair<Coordinate, Character> pair : path) {
            Coordinate coordinate = pair.getLeft();
            map[coordinate.y][coordinate.x] = pair.getRight();
        }

        for (Coordinate coordinate : out) {
            map[coordinate.y][coordinate.x] = 'O';
        }

        for (Coordinate coordinate : in) {
            map[coordinate.y][coordinate.x] = 'I';
        }

        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if('.' == map[i][j]){
                    count++;
                }
            }
        }

        // Write the map to the file
        try (FileWriter writer = new FileWriter(filename)) {
            for (char[] row : map) {
                writer.write(row);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("Failed to write to file: " + filename, e);
        }
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
            Coordinate next = nextPipeSection.get();
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
