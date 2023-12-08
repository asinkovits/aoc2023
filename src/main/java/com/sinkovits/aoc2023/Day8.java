package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.*;


@Slf4j
public class Day8 implements AdventOfCodeDailyExercise {

    @RequiredArgsConstructor
    private class Node {
        private final String name;
        private Node left;
        private Node right;
    }

    private static class Context {
        String instructions;
        Map<String, Node> nodeLookup = new HashMap<>();
    }

    @Override
    public long solveFirst() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::parseLine);
        Node start = context.nodeLookup.values().stream().filter(node -> node.name.equals("AAA")).findFirst().orElseThrow();
        long result = calculateResult(context, start, "ZZZ") * context.instructions.length();
        log.info("Solution for the first exercise: {}", result);
        return result;
    }

    @Override
    public long solveSecond() {
        LineProcessor<Context> lineProcessor = getContextLineReader();
        Context context = lineProcessor.processLines(this::parseLine);

        long iterationsRequired = context.nodeLookup
                .values()
                .stream()
                .filter(node -> node.name.matches("..A"))
                .map(node -> calculateResult(context, node, "..Z"))
                .reduce(1L, (a, b) -> a * b);

        long result = iterationsRequired * context.instructions.length();
        log.info("Solution for the second exercise: {}", result);
        return result;
    }

    private void parseLine(Integer lineNumber, String line, Context context) {
        if (lineNumber == 0) {
            context.instructions = line;
        }
        if (lineNumber > 1) {
            String[] row = line.split("=");
            String nodeName = row[0].trim();
            String[] children = row[1].replaceAll("[() ]", "").split(",");
            Node parent = context.nodeLookup.computeIfAbsent(nodeName, Node::new);
            Node leftChild = context.nodeLookup.computeIfAbsent(children[0], Node::new);
            Node rightChild = context.nodeLookup.computeIfAbsent(children[1], Node::new);
            parent.left = leftChild;
            parent.right = rightChild;
        }
    }

    private long calculateResult(Context context, Node startNode, String endNodePattern) {
        char[] charArray = context.instructions.toCharArray();
        Node currentNode = startNode;
        for (long i = 1; ; i++) {
            for (char c : charArray) {
                if (c == 'L') {
                    currentNode = currentNode.left;
                }
                if (c == 'R') {
                    currentNode = currentNode.right;
                }
            }
            if (currentNode.name.matches(endNodePattern)) {
                return i;
            }
        }
    }

    private static LineProcessor<Context> getContextLineReader() {
        return new LineProcessor<>(
                Path.of("input_day8"),
                //Path.of("test"),
                new Context()
        );
    }
}
