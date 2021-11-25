package go.solve.it.util.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class TreeNode<T> extends Node<TreeNode<T>, T> {

    private TreeNode<T> parent;
    private List<TreeNode<T>> children = new ArrayList<>();

    public static <T> TreeNode<T> empty() {
        return new TreeNode<T>();
    }

    public static <T> TreeNode<T> withName(final String name) {
        return new TreeNode<T>().name(name);
    }

    @Override
    protected TreeNode<T> self() {
        return this;
    }

    public TreeNode<T> parent() {
        return parent;
    }

    public List<TreeNode<T>> children() {
        return children;
    }

    public int height() {
        return 1 + children.stream().mapToInt(TreeNode::height).max().orElse(0);
    }

    public int size() {
        return 1 + children.stream().mapToInt(TreeNode::size).sum();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Optional<TreeNode<T>> find(final Predicate<? super TreeNode<T>> predicate) {
        return predicate.test(this)
                ? Optional.of(this)
                : children.stream().flatMap(child -> child.find(predicate).stream()).findAny();
    }

    public TreeNode<T> parent(final TreeNode<T> parent) {
        this.parent = parent;
        return this;
    }

    public TreeNode<T> addChild(final TreeNode<T> child) {
        child.parent(this);
        this.children.add(child);
        return this;
    }
}
