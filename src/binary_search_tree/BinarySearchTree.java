package binary_search_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BinarySearchTree<T extends Comparable<T>> {
    private enum TreeTraversal { INORDER, PREORDER, POSTORDER }

    private record TreeVisitBehavior<T>(
        TreeTraversal treeTraversal,
        Consumer<Node<T>> nodeConsumer,
        Predicate<Node<T>> goLeftPredicate,
        Predicate<Node<T>> goRightPredicate
    ) {}

    private final List<Node<T>> tree = new ArrayList<>();

    Node<T> getRoot() { return (tree.size() == 0) ? null : tree.get(0); }

    void insert(final T value) {
        if (tree.size() == 0) {
            tree.add(new Node<>(value));
            return;
        }
        final Consumer<Node<T>> insertLogic = node -> {
            Node<T> newNode;
            final int cmp = node.value.compareTo(value);
            if (cmp >= 0 && node.left == null) {
                newNode = node.left = new Node<>(value);
                tree.add(newNode);
            } else if (cmp < 0 && node.right == null) {
                newNode = node.right = new Node<>(value);
                tree.add(newNode);
            }
        };
        final Predicate<Node<T>> isGreater = node -> node.value.compareTo(value) >= 0;
        final Predicate<Node<T>> isMinor   = node -> node.value.compareTo(value) < 0;
        final TreeVisitBehavior<T> visitBehavior = new TreeVisitBehavior<>(
            TreeTraversal.INORDER,
            insertLogic,
            isGreater,
            isMinor
        );
        visit(getRoot(), visitBehavior);
    }

    List<T> getAsSortedList() {
        final List<T> list = new ArrayList<>(tree.size());
        final TreeVisitBehavior<T> visitBehavior = new TreeVisitBehavior<>(
            TreeTraversal.INORDER,
            node -> list.add(node.value),
            node -> true,
            node -> true
        );
        visit(getRoot(), visitBehavior);
        return list;
    }

    private void visit(final Node<T> node, final TreeVisitBehavior<T> visitBehavior) {
        if (node == null) return;
        if (visitBehavior.treeTraversal == TreeTraversal.PREORDER) visitBehavior.nodeConsumer.accept(node);
        if (visitBehavior.goLeftPredicate.test(node)) visit(node.left, visitBehavior);
        if (visitBehavior.treeTraversal == TreeTraversal.INORDER) visitBehavior.nodeConsumer.accept(node);
        if (visitBehavior.goRightPredicate.test(node)) visit(node.right, visitBehavior);
        if (visitBehavior.treeTraversal == TreeTraversal.POSTORDER) visitBehavior.nodeConsumer.accept(node);
    }

    List<T> getAsLevelOrderList() { return tree.stream().map(Node::getData).toList(); }

    private static class Node<T> {
        private final T value;
        private Node<T> left, right;

        Node(T value) { this.value = value; }

        Node<T> getLeft() { return left; }

        Node<T> getRight() { return right; }

        T getData() { return value; }
    }
}