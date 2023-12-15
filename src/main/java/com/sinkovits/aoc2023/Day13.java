package com.sinkovits.aoc2023;

import com.sinkovits.aoc2023.Day13.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Day13 extends AbstractDay<Context> {

    public Day13() {
        super("test", Context.class);
        //        super("input_day13", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        if (lineNumber == 0) {
            context.active = new Mirror();
            context.mirrors.add(context.active);
        }
        if (line.equals(StringUtils.EMPTY)) {
            context.active = new Mirror();
            context.mirrors.add(context.active);
        } else {
            context.active.rows.add(line);
        }
    }

    @Override
    protected long calculateFirst(Context context) {
        context.mirrors.forEach(Mirror::calculateColumns);
        return context.mirrors.stream().mapToLong(Day13::extracted).sum();
    }

    private static long extracted(Mirror mirror) {
        long v = mirror.tryFindVerticalReflection();
        long h = mirror.tryFindHorizontalReflection();
        return h * 100 + v;
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {}

    @Override
    protected long calculateSecond(Context context) {
        return 0;
    }

    protected static class Context {
        Mirror active;
        List<Mirror> mirrors = new ArrayList<>();
    }

    protected static class Mirror {
        List<String> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        void calculateColumns() {
            StringBuilder columnBuilder = new StringBuilder();
            for (int i = 0; i < rows.get(0).length(); i++) {
                for (String row : rows) {
                    columnBuilder.append(row.charAt(i));
                }
                columns.add(columnBuilder.toString());
                columnBuilder = new StringBuilder();
            }
        }

        long tryFindVerticalReflection() {
            String prev = columns.get(0);
            for (int i = 1; i < columns.size(); i++) {
                String next = columns.get(i);
                if (prev.equals(next)) {
                    if (tryExpand(i, columns)) {
                        return i;
                    }
                } else {
                    prev = next;
                }
            }
            return 0;
        }

        long tryFindHorizontalReflection() {
            String prev = rows.get(0);
            for (int i = 1; i < rows.size(); i++) {
                String next = rows.get(i);
                if (prev.equals(next)) {
                    if (tryExpand(i, rows)) {
                        return i;
                    }
                } else {
                    prev = next;
                }
            }
            return 0;
        }

        private boolean tryExpand(int mirrorPoint, List<String> data) {
            for (int i = mirrorPoint - 1, j = mirrorPoint; i >= 0 && j < data.size(); i--, j++) {
                if (!data.get(i).equals(data.get(j))) {
                    return false;
                }
            }
            return true;
        }
    }
}
