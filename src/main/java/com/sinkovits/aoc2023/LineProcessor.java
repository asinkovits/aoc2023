package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class LineProcessor<T> {

    private final Path path;
    private final T context;

    @SneakyThrows(IOException.class)
    public T processLines(BiConsumer<String, T> processor) {
        try(InputStreamReader isr = new InputStreamReader(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream(path.toString())));
            BufferedReader br = new BufferedReader(isr)) {
            String line;
            while((line = br.readLine()) != null) {
                processor.accept(line, context);
            }
        }
        return context;
    }
}
