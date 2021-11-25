package go.solve.it.util.math;

public interface  Addable<T extends Addable<T>> {

    T add(T other);
}
