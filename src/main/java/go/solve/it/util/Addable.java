package go.solve.it.util;

public interface Addable<T extends Addable<T>> {

    T add(T other);
}
