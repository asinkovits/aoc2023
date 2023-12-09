package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.sinkovits.aoc2023.Day8.Context;


@Slf4j
public class Day8 extends AbstractDay<Context> {

    public Day8() {
        super("input_day8", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        parseLine(lineNumber, line, context);
    }

    @Override
    protected long calculateFirst(Context context) {
        Node start = context.nodeLookup.values().stream()
                .filter(node -> node.name.equals("AAA"))
                .findFirst()
                .orElseThrow();
        return calculateIterationsRequired(context, start, "ZZZ") * context.instructions.length();
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        parseLine(lineNumber, line, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        long iterationsRequired = context.nodeLookup
                .values()
                .stream()
                .filter(node -> node.name.matches("..A"))
                .map(node -> calculateIterationsRequired(context, node, "..Z"))
                .reduce(1L, (a, b) -> a * b);

        return iterationsRequired * context.instructions.length();
    }

    @RequiredArgsConstructor
    private static class Node {
        private final String name;
        private Node left;
        private Node right;
    }

    protected static class Context {
        String instructions;
        Map<String, Node> nodeLookup = new HashMap<>();
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

    private long calculateIterationsRequired(Context context, Node startNode, String endNodePattern) {
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
}
