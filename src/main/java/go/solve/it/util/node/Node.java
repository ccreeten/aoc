package go.solve.it.util.node;

public abstract class Node<S extends Node<S, T>, T> {

    private String name;
    private T value;

    protected abstract S self();

    public String name() {
        return name;
    }

    public T value() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public S name(final String name) {
        this.name = name;
        return self();
    }

    public S value(final T value) {
        this.value = value;
        return self();
    }

    @Override
    public String toString() {
        return "(name=" + name() + ", value=" + value + ")";
    }
}
