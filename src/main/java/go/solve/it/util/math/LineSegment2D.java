package go.solve.it.util.math;

import go.solve.it.util.math.Positions.Position2D;
import go.solve.it.util.math.Range.IntRange;

import static java.lang.Math.sqrt;

public record LineSegment2D(Position2D p1, Position2D p2) {

    public static LineSegment2D between(final Position2D p1, final Position2D p2) {
        return new LineSegment2D(p1, p2);
    }

    public IntRange xRange() {
        return IntRange.between(p1().x(), p2().x());
    }

    public IntRange yRange() {
        return IntRange.between(p1().y(), p2().y());
    }

    public int floorLength() {
        return (int) length();
    }

    public double length() {
        return sqrt((p1.x() - p2.x()) * (p1.x() - p2.x()) + (p1.y() - p2.y()) * (p1.y() - p2.y()));
    }
}
