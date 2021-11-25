package go.solve.it.util.node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class GraphNode<T> extends Node<GraphNode<T>, T> {

    private List<GraphNode<T>> neighbours = new ArrayList<>();

    public static <T> GraphNode<T> withName(final String name) {
        return new GraphNode<T>().name(name);
    }

    @Override
    protected GraphNode<T> self() {
        return this;
    }

    public List<GraphNode<T>> neighbours() {
        return neighbours;
    }

    public int size() {
        return 1 + neighbours.stream().mapToInt(GraphNode::size).sum();
    }

    public boolean contains(final Predicate<? super GraphNode<T>> predicate) {
        return predicate.test(this) || neighbours().stream().anyMatch(predicate);
    }

    // not adding reverse, in case of directed graph
    public GraphNode<T> addNeighbour(final GraphNode<T> neighbour) {
        this.neighbours.add(neighbour);
        return this;
    }
}
