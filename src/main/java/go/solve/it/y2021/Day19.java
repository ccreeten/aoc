package go.solve.it.y2021;

import go.solve.it.util.container.Collectionsβ;
import go.solve.it.util.container.Tuple;
import go.solve.it.util.container.Tuple.IntTuple;
import go.solve.it.util.input.Input;
import go.solve.it.util.math.Positions.Position3D;
import go.solve.it.util.stream.BiStream;
import go.solve.it.util.string.Regex;

import java.util.*;

import static go.solve.it.util.stream.Streams.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

// TODO: nothin, cause it is a mess :)
public final class Day19 {

    public static void main(final String... args) {
        System.out.println(part1(Input.scan("y2021/day19/input")));
        System.out.println(part2(Input.scan("y2021/day19/input")));
    }

    private static long part1(Scanner scan) {
        final var map = buildMap(scan);
        return BiStream.of(map.head())
                .map((id, beacons) -> beacons.stream().map(map.tail().get(IntTuple.ofInts(0, id))::add))
                .flatMap(identity())
                .distinct()
                .count();
    }

    private static long part2(final Scanner scan) {
        final var map = buildMap(scan);
        return BiStream.of(map.tail())
                .filter((link1, worldPos) -> link1.head() == 0 && link1.tail() != 0)
                .map((link1, worldPos) -> BiStream.of(map.tail())
                        .filter((sc2, p2) -> sc2.head() == 0 && sc2.tail() != 0)
                        .map((sc2, p2) -> worldPos.manhattanDistanceFrom(p2))
                )
                .flatMapToInt(x -> x.mapToInt(y -> y))
                .max()
                .getAsInt();
    }

    public static Tuple<Map<Integer, List<Position3D>>, Map<IntTuple, Position3D>> buildMap(Scanner scan) {
        List<BeaconScanner> scanners = new ArrayList<>();
        BeaconScanner current = BeaconScanner.withId(Integer.parseInt(Regex.findNext(scan.nextLine(), "(\\d+)")));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.isBlank()) {
                scanners.add(current);
                if (scan.hasNextLine()) {
                    current = BeaconScanner.withId(Integer.parseInt(Regex.findNext(scan.nextLine(), "(\\d+)")));
                }
                continue;
            }
            int[] tulpe = stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            current.addBeacon(Position3D.of(tulpe[0], tulpe[1], tulpe[2]));
        }
        scanners.add(current);

        final Map<Integer, List<Position3D>> FIXED_ORIENT = new HashMap<>();
        FIXED_ORIENT.put(0, scanners.get(0).beacons());

        Map<IntTuple, Position3D> offsets = new LinkedHashMap<>();
        offsets.put(IntTuple.ofInts(0, 0), Position3D.atOrigin());

        while (FIXED_ORIENT.size() != scanners.size()) {
            for (final BeaconScanner s1 : scanners) {
                for (final BeaconScanner s2 : scanners) {
                    if (s1.id() != s2.id() || !FIXED_ORIENT.containsKey(s1.id()) || !FIXED_ORIENT.containsKey(s2.id())) {
                        var detect = detect(s1, s2, FIXED_ORIENT);
                        if (detect != null) {
                            offsets.put(IntTuple.ofInts(s1.id(), s2.id()), detect);
                        }
                    }
                }
            }
        }

        for (var i = 0; i < scanners.size(); i++) {
            if (!offsets.containsKey(IntTuple.ofInts(0, i))) {
                var sid = scanners.get(i).id();
                Set<Integer> seen = new HashSet<>();
                x(sid, sid, offsets, seen, Position3D.atOrigin());

            }
        }

        return Tuple.of(FIXED_ORIENT, offsets);
    }

    private static void x(final int i, final int id, final Map<IntTuple, Position3D> offsets, final Set<Integer> seen, final Position3D p) {
        final var links = offsets.keySet().stream()
                .filter(e -> e.head() == id || e.tail() == id)
                .filter(e -> !seen.contains(e.head()) || !seen.contains(e.tail()))
                .collect(toList());
        if (links.isEmpty()) {
            return;
        }
        for (IntTuple link : links) {
            var nextId = link.head() == id ? link.tail() : link.head();
            var newSeen = new HashSet<>(seen);
            newSeen.add(link.head());
            newSeen.add(link.tail());
            var newP = link.head().equals(nextId) ? p.add(offsets.get(link)) : p.subtract(offsets.get(link));
            offsets.put(IntTuple.ofInts(nextId, i), newP);
            if (nextId == 0) {
                return;
            }
            x(i, nextId, offsets, newSeen, newP);
        }
    }

    private static Position3D detect(final BeaconScanner s1, final BeaconScanner s2, final Map<Integer, List<Position3D>> FIXED_ORIENT) {
        var b1 = new ArrayList<List<Position3D>>();
        var b2 = new ArrayList<List<Position3D>>();

        if (!FIXED_ORIENT.containsKey(s1.id()))
            b1.addAll(s1.orientedBeacons());
        else
            FIXED_ORIENT.get(s1.id()).forEach(b -> b1.add(List.of(b)));

        if (!FIXED_ORIENT.containsKey(s2.id()))
            b2.addAll(s2.orientedBeacons());
        else
            FIXED_ORIENT.get(s2.id()).forEach(b -> b2.add(List.of(b)));

        for (var i = 0; i < b1.get(0).size(); i++) {
            for (var i1 = 0; i1 < b2.get(0).size(); i1++) {
                final var fi = i;
                final var fi1 = i1;
                var left = b1.stream().map(x -> x.get(fi)).collect(toList());
                var right = b2.stream().map(x -> x.get(fi1)).toList();

                for (Position3D l : left) {
                    for (Position3D r : right) {
                        var ox = l.x() - r.x();
                        var oy = l.y() - r.y();
                        var oz = l.z() - r.z();
                        final var of = Position3D.of(ox, oy, oz);
                        var nr = right.stream().map(of::add).collect(toList());

                        final var intersection = Collectionsβ.intersection(left, nr);
                        if (intersection.size() >= 12) {
                            if (FIXED_ORIENT.containsKey(s1.id())) {
                                FIXED_ORIENT.put(s2.id(), right);
                            }
                            if (FIXED_ORIENT.containsKey(s2.id())) {
                                FIXED_ORIENT.put(s1.id(), left);
                            }
                            return of;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static List<Position3D> orientations(final Position3D beacon) {
        final var orientations = new ArrayList<Position3D>();
        var current = Position3D.atOrigin();
        // assume y is up, rotate around y
        current = beacon;
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.z(), current.y(), -current.x());
        }
        // assume -y is up (rotate around z to get there), rotate around y
        current = Position3D.of(beacon.y(), -beacon.x(), beacon.z());
        current = Position3D.of(current.y(), -current.x(), current.z());
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.z(), current.y(), -current.x());
        }
        // assume x is up (rotate around z to get there), rotate around x
        current = Position3D.of(-beacon.y(), beacon.x(), beacon.z());
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.x(), current.z(), -current.y());
        }
        // assume -x is up (rotate around z to get there), rotate around x
        current = Position3D.of(beacon.y(), -beacon.x(), beacon.z());
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.x(), current.z(), -current.y());
        }
        // assume z is up (rotate around x to get there), rotate around z
        current = Position3D.of(beacon.x(), beacon.z(), -beacon.y());
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.y(), -current.x(), current.z());
        }
        // assume -z is up (rotate around x to get there), rotate around z
        current = Position3D.of(beacon.x(), -beacon.z(), beacon.y());
        for (var i = 0; i < 4; i++) {
            orientations.add(current);
            current = Position3D.of(current.y(), -current.x(), current.z());
        }
        return orientations;
    }

    static final class BeaconScanner {

        private final int id;
        private final List<Position3D> beacons = new ArrayList<>();
        private final List<List<Position3D>> oriented = new ArrayList<>();

        private BeaconScanner(final int id) {
            this.id = id;
        }

        static BeaconScanner withId(int id) {
            return new BeaconScanner(id);
        }

        BeaconScanner addBeacon(Position3D beacon) {
            beacons.add(beacon);
            oriented.clear();
            beacons.forEach(b -> oriented.add(orientations(b)));
            return this;
        }

        int id() {
            return id;
        }

        List<Position3D> beacons() {
            return beacons;
        }

        List<List<Position3D>> orientedBeacons() {
            return oriented;
        }
    }
}