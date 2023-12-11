package com.sinkovits.aoc2023;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;

@RequiredArgsConstructor
public class LineProcessor<T> {

    private final Path path;
    private final T context;

    public T processLines(TriConsumer<Integer, String, T> processor) {
        try (InputStreamReader isr =
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        getClass()
                                                .getClassLoader()
                                                .getResourceAsStream(path.toString())));
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                processor.accept(i++, line, context);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return context;
    }
}
