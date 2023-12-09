package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sinkovits.aoc2023.Day4.Context;
import static com.sinkovits.aoc2023.ParsingUtil.parseNumbers;
import static com.sinkovits.aoc2023.ParsingUtil.removeNonNumeric;

@Slf4j
public class Day4 extends AbstractDay<Context> {

    public Day4() {
        super("input_day4", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        String[] split = line.split(":");
        String[] split1 = split[1].split("\\|");
        List<Long> winingNumbers = parseNumbers(split1[0].trim());
        List<Long> ownNumbers = parseNumbers(split1[1].trim());
        long count = ownNumbers.stream()
                .distinct()
                .filter(winingNumbers::contains)
                .count();
        if (count == 1) {
            context.add(1);
        } else if (count > 1) {
            context.add((long) Math.pow(2, count - 1));
        }
    }

    @Override
    protected long calculateFirst(Context context) {
        return context.getSum();
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        String[] split = line.split(":");
        long cardId = Long.parseLong(removeNonNumeric(split[0]));
        String[] split1 = split[1].split("\\|");
        List<Long> winingNumbers = parseNumbers(split1[0].trim());
        List<Long> ownNumbers = parseNumbers(split1[1].trim());
        long count = ownNumbers.stream()
                .filter(winingNumbers::contains)
                .count();
        context.addWingNumbersByCardId(cardId, count);
    }

    @Override
    protected long calculateSecond(Context context) {
        return context.getSum();
    }

    @Getter
    protected static final class Context extends CountingContext {
        private final Map<Long, Integer> cardIdMultiplier = new HashMap<>();

        public void addWingNumbersByCardId(long cardId, long count) {
            Integer multiplier = cardIdMultiplier.getOrDefault(cardId, 1);
            if (count != 0) {
                for (long i = cardId + 1; i <= cardId + count; i++) {
                    Integer otherMultiplier = cardIdMultiplier.getOrDefault(i, 1);
                    cardIdMultiplier.put(i, otherMultiplier + multiplier);
                }
            }
            add(multiplier);
        }
    }
}
