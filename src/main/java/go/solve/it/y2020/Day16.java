package go.solve.it.y2020;

import go.solve.it.util.container.Collectionsβ;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Range;
import go.solve.it.util.math.Range.IntRange;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static go.solve.it.util.container.Arraysβ.tail;
import static go.solve.it.util.container.Collectionsβ.difference;
import static go.solve.it.util.string.Regex.match;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public final class Day16 {

    public static void main(final String... args) {
        System.out.println(part1(Input.string("y2020/day16/input")));
        System.out.println(part2(Input.string("y2020/day16/input")));
    }

    private static long part1(final String notes) {
        final var parts = notes.split("\n\n");
        final var fields = stream(parts[0].split("\n")).map(Field::parse).collect(toList());
        final var tickets = stream(parts[2].split(":\n")[1].split("\n")).map(Ticket::parse).collect(toList());

        return tickets.stream().flatMapToInt(ticket -> stream(ticket.values()))
                .filter(value -> fields.stream().noneMatch(field -> field.contains(value)))
                .sum();
    }

    private static long part2(final String notes) {
        final var parts = notes.split("\n\n");
        final var fields = stream(parts[0].split("\n")).map(Field::parse).collect(toList());
        final var yourTicket = parts[1].split(":\n")[1].split(",");
        final var tickets = stream(parts[2].split(":\n")[1].split("\n")).map(Ticket::parse).collect(toList());

        final var fieldOrder = inferFieldOrder(fields, tickets);
        return range(0, fieldOrder.size())
                .filter(index -> fieldOrder.get(index).startsWith("departure"))
                .mapToLong(index -> parseLong(yourTicket[index]))
                .reduce(1, Math::multiplyExact);
    }

    private static List<String> inferFieldOrder(final List<Field> fields, final List<Ticket> tickets) {
        final var validTickets = tickets.stream().filter(ticket -> ticket.conformsTo(fields)).collect(toList());
        final var possibleFields = filterPossibleFields(fields, validTickets);
        final var indexOrder = range(0, possibleFields.size()).boxed().sorted(comparing(index -> possibleFields.get(index).size())).mapToInt(Integer::intValue).toArray();
        return inferFieldOrder(possibleFields, new HashSet<>(), indexOrder);
    }

    private static List<Set<String>> filterPossibleFields(final List<Field> fields, final List<Ticket> tickets) {
        final var fieldNames = fields.stream().map(Field::name).collect(toSet());
        final var fieldCount = tickets.get(0).values().length;
        final var inferred = range(0, fieldCount).mapToObj(__ -> new HashSet<>(fieldNames)).collect(toList());

        final var result = inferred.stream().map(Collectionsβ::copy).collect(toList());
        range(0, tickets.get(0).values().length).forEachOrdered(index -> tickets.forEach(ticket -> {
            final var fieldValue = ticket.values()[index];
            fields.forEach(field -> {
                if (!field.contains(fieldValue)) {
                    final var fieldPredictions = result.get(index);
                    fieldPredictions.remove(field.name());
                }
            });
        }));
        return result;
    }

    // does not seem to need backtracking...
    private static List<String> inferFieldOrder(final List<Set<String>> inferred, final Set<String> taken, final int... indices) {
        if (indices.length == 0) {
            return inferred.stream().map(list -> list.iterator().next()).collect(toList());
        }
        final var index = indices[0];
        final var currentPrediction = inferred.get(index);
        final var toTake = difference(currentPrediction, taken).iterator().next();
        taken.add(toTake);
        inferred.set(index, Set.of(toTake));
        return inferFieldOrder(inferred, taken, tail(indices));
    }

    static final class Field {

        private final String name;
        private final List<IntRange> ranges;

        private Field(final String name, final IntRange... ranges) {
            this.name = name;
            this.ranges = asList(ranges);
        }

        static Field parse(final String field) {
            final var match = match(field, "(.*): (.*) or (.*)");
            return new Field(match.group(1), parseRange(match.group(2)), parseRange(match.group(3)));
        }

        String name() {
            return name;
        }

        boolean contains(final int value) {
            return ranges.stream().anyMatch(range -> range.contains(value));
        }

        private static IntRange parseRange(final String range) {
            return Range.between(parseInt(range.substring(0, range.indexOf('-'))), parseInt(range.substring(range.indexOf('-') + 1)));
        }
    }

    static record Ticket(int... values) {

        static Ticket parse(final String ticket) {
            return new Ticket(stream(ticket.split(",")).mapToInt(Integer::parseInt).toArray());
        }

        boolean conformsTo(final Collection<Field> fields) {
            return stream(values()).allMatch(value -> fields.stream().anyMatch(field -> field.contains(value)));
        }
    }
}