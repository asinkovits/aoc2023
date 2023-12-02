package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class LineProcessor<T> {

    private final Path path;
    private final T context;

    public T processLines(BiConsumer<String, T> processor) {
        try(var lines = java.nio.file.Files.lines(path)) {
            lines.forEach(line -> {
                try {
                    processor.accept(line, context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    return context;
    }
}
