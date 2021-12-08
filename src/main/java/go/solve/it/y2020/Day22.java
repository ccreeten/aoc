package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static go.solve.it.util.container.Collectionsβ.copy;
import static go.solve.it.util.stream.Collectorsβ.toDeque;
import static go.solve.it.util.string.Regex.findAll;
import static go.solve.it.util.string.Regex.findNext;
import static java.lang.Long.parseLong;
import static java.util.stream.LongStream.range;

public final class Day22 {

    public static void main(final String... args) {
        System.out.println(part1(Input.string("y2020/day22/input")));
        System.out.println(part2(Input.string("y2020/day22/input")));
    }

    private static long part1(final String input) {
        final var parts = input.split("\n\n");
        final var player1 = Player.parse(parts[0]);
        final var player2 = Player.parse(parts[1]);
        final var winner = playCombat(player1, player2);
        return range(1, winner.deck().size() + 1).map(position -> position * winner.deck().pollLast()).sum();
    }

    private static Player playCombat(final Player player1, final Player player2) {
        while (!player1.deck().isEmpty() && !player2.deck().isEmpty()) {
            final var card1 = player1.deck().poll();
            final var card2 = player2.deck().poll();
            if (card1 > card2) {
                player1.deck().offer(card1);
                player1.deck().offer(card2);
            } else {
                player2.deck().offer(card2);
                player2.deck().offer(card1);
            }
        }
        return player1.deck().isEmpty() ? player2 : player1;
    }

    private static long part2(final String input) {
        final var parts = input.split("\n\n");
        final var player1 = Player.parse(parts[0]);
        final var player2 = Player.parse(parts[1]);
        final var winner = playRecursiveCombat(player1, player2);
        return range(1, winner.deck().size() + 1).map(position -> position * winner.deck().pollLast()).sum();
    }

    private static Player playRecursiveCombat(final Player player1, final Player player2) {
        return playRecursiveCombat(player1, player2, new HashSet<>());
    }

    private static Player playRecursiveCombat(final Player player1, final Player player2, final Set<Long> seen) {
        if (player1.deck().isEmpty()) {
            return player2;
        }
        if (player2.deck().isEmpty()) {
            return player1;
        }
        if (!seen.add((player1.hash() * 31) + player2.hash())) {
            return player1;
        }
        final var card1 = player1.deck().poll();
        final var card2 = player2.deck().poll();
        final var winner = player1.deck().size() >= card1 && player2.deck().size() >= card2
                ? playRecursiveCombat(player1.sub(card1), player2.sub(card2), copy(seen))
                : card1 > card2 ? player1 : player2;

        if (winner.id() == 1) {
            player1.deck().offer(card1);
            player1.deck().offer(card2);
        } else {
            player2.deck().offer(card2);
            player2.deck().offer(card1);
        }
        return playRecursiveCombat(player1, player2, seen);
    }

    static record Player(long id, Deque<Long> deck) {

        static Player parse(final String deck) {
            final var id = parseLong(findNext(deck, "(\\d)"));
            final var cards = findAll(deck.split(":")[1], "(\\d+)").stream().map(Long::parseLong).collect(toDeque());
            return new Player(id, cards);
        }

        Player sub(final long count) {
            return new Player(id, deck.stream().limit(count).collect(toDeque()));
        }

        long hash() {
            return (id * 31) + Arrays.hashCode(deck.toArray());
        }
    }
}