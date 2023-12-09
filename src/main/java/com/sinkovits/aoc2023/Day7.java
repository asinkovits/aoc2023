package com.sinkovits.aoc2023;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.sinkovits.aoc2023.Day7.Context;

@Slf4j
public class Day7 extends AbstractDay<Context> {

    public Day7() {
        super("input_day7", Context.class);
    }

    @Override
    protected void parseFirst(Integer lineNumber, String line, Context context) {
        String[] split = line.split(StringUtils.SPACE);
        Hand hand = new Hand(split[0]);
        long bid = Long.parseLong(split[1]);
        context.addHand(hand);
        context.addBid(hand, bid);
    }

    @Override
    protected long calculateFirst(Context context) {
        return calculateResult(context);
    }

    @Override
    protected void parseSecond(Integer lineNumber, String line, Context context) {
        String lineWithJokers = line.replaceAll(Card.JACK.code, Card.JOKER.code);
        parseFirst(lineNumber, lineWithJokers, context);
    }

    @Override
    protected long calculateSecond(Context context) {
        return calculateResult(context);
    }

    private enum Card {
        JOKER("G"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("T"),
        JACK("J"),
        QUEEN("Q"),
        KING("K"),
        ACE("A");

        private final String code;

        Card(String code) {
            this.code = code;
        }

        public static Card parse(String code) {
            for (Card card : values()) {
                if (card.code.equals(code)) {
                    return card;
                }
            }
            throw new IllegalArgumentException("Unknown card code: " + code);
        }
    }

    protected static class Hand {
        UUID id = UUID.randomUUID();
        private final EnumMap<Card, Integer> cards = new EnumMap<>(Card.class);
        private final Card[] cardArray = new Card[5];

        @Getter private HandType type;

        public Hand(String hand) {
            char[] cardCodes = hand.toCharArray();
            for (int i = 0; i < cardCodes.length; i++) {
                Card card = Card.parse(String.valueOf(cardCodes[i]));
                cards.put(card, cards.getOrDefault(card, 0) + 1);
                cardArray[i] = card;
                calculateHandType(card);
            }
            applyJokers();
        }

        private void applyJokers() {
            Integer jokerCount = cards.getOrDefault(Card.JOKER, 0);
            if (jokerCount == 5) {
                type = HandType.FIVE_OF_A_KIND;
                return;
            }
            for (int i = 0; i < jokerCount; i++) {
                if (type == HandType.HIGH_CARD) {
                    type = HandType.ONE_PAIR;
                } else if (type == HandType.ONE_PAIR) {
                    type = HandType.THREE_OF_A_KIND;
                } else if (type == HandType.TWO_PAIRS) {
                    type = HandType.FULL_HOUSE;
                } else if (type == HandType.THREE_OF_A_KIND) {
                    type = HandType.FOUR_OF_A_KIND;
                } else if (type == HandType.FOUR_OF_A_KIND) {
                    type = HandType.FIVE_OF_A_KIND;
                }
            }
        }

        private void calculateHandType(Card card) {
            if (card == Card.JOKER) {
                return;
            }
            if (type == null) {
                type = HandType.HIGH_CARD;
            } else {
                Integer count = cards.get(card);
                if (count == 5) {
                    type = HandType.FIVE_OF_A_KIND;
                } else if (count == 4) {
                    type = HandType.FOUR_OF_A_KIND;
                } else if (count == 3) {
                    if (type == HandType.TWO_PAIRS) {
                        type = HandType.FULL_HOUSE;
                    } else {
                        type = HandType.THREE_OF_A_KIND;
                    }
                } else if (count == 2) {
                    if (type == HandType.ONE_PAIR) {
                        type = HandType.TWO_PAIRS;
                    } else if (type == HandType.THREE_OF_A_KIND) {
                        type = HandType.FULL_HOUSE;
                    } else {
                        type = HandType.ONE_PAIR;
                    }
                }
            }
        }

        public int compare(Hand other) {
            int compareByHandType = Comparator.comparing(Hand::getType).compare(this, other);
            if (compareByHandType != 0) {
                return compareByHandType;
            }
            for (int i = 0; i < cardArray.length; i++) {
                int compareByCard =
                        Comparator.comparing(Card::ordinal)
                                .compare(cardArray[i], other.cardArray[i]);
                if (compareByCard != 0) {
                    return compareByCard;
                }
            }
            throw new IllegalStateException("Two hands are equal: " + this + " and " + other);
        }
    }

    private enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    protected static class Context {
        List<Hand> hands = new ArrayList<>();
        Map<UUID, Long> bidByHandId = new HashMap<>();

        public void addHand(Hand hand) {
            hands.add(hand);
        }

        public void addBid(Hand hand, long bid) {
            bidByHandId.put(hand.id, bid);
        }

        public long getBid(Hand hand) {
            return bidByHandId.get(hand.id);
        }
    }

    private long calculateResult(Context context) {
        context.hands.sort(Hand::compare);
        return IntStream.range(0, context.hands.size())
                .mapToObj(i -> Pair.of(i + 1, context.hands.get(i)))
                .map(pair -> Pair.of(pair.getLeft(), context.getBid(pair.getRight())))
                .mapToLong(pair -> pair.getLeft() * pair.getRight())
                .sum();
    }
}
