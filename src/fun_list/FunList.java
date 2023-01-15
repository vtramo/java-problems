package fun_list;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunList<T> {
    final Node<T> first;

    protected record Node<U>(U item, FunList.Node<U> next) { }

    public FunList(Node<T> node) { this.first = node; }

    public T get(int i) { return getNodeRecursive(i, first).item; }

    protected static <T> Node<T> getNodeRecursive(int i, Node<T> node) {
        return i == 0 ? node : getNodeRecursive(i - 1, node.next);
    }

    public static <T> FunList<T> cons(T item, FunList<T> list) { return list.insert(0, item); }

    public FunList<T> insert(int i, T item) { return new FunList<>(insert(i, item, this.first)); }

    protected static <T> Node<T> insert(int i, T item, Node<T> node) {
        return i == 0 ? new Node<T>(item, node) : new Node<T>(node.item, insert(i-1, item, node.next));
    }

    public FunList<T> removeAt(int i) { return new FunList<T>(removeAt(i, this.first)); }

    protected static <T> Node<T> removeAt(int i, Node<T> node) {
        return i == 0 ? node.next : new Node<T>(node.item, removeAt(i-1, node.next));
    }

    public FunList<T> remove(T x) { return new FunList<>(remove(x, this.first)); }

    protected static <T> Node<T> remove(T x, Node<T> node) {
        return node == null ? null :
            node.item == x ?
                remove(x, node.next) :
                new Node<>(node.item, remove(x, node.next));
    }

    public FunList<T> reverse() { return new FunList<>(reverse(this.first, null)); }

    protected Node<T> reverse(Node<T> curr, Node<T> prev) {
       return curr == null ? prev : reverse(curr.next, new Node<>(curr.item, prev));
    }

    @Override
    public String toString() {
        var punt = this.first;
        var result = new StringBuilder("[");
        while (punt != null)
            result.append(punt.item.toString())
                  .append((punt = punt.next) == null ? "]" : " ");
        return result.toString();
    }

    public FunList<T> append(FunList<T> list2) { return new FunList<>(append(this.first, list2.first)); }

    protected Node<T> append(Node<T> node1, Node<T> node2) {
        return (node1 == null && node2 == null) ? null :
            node1 != null ?
                new Node<>(node1.item, append(node1.next, node2)) :
                new Node<>(node2.item, append(null, node2.next));
    }

    public <U> FunList<U> map(Function<T,U> f) { return new FunList<U>(map(f, first)); }

    protected static <T,U> Node<U> map(Function<T,U> f, Node<T> node) {
        return node == null ? null : new Node<U>(f.apply(node.item), map(f, node.next));
    }

    public <U> U reduce(U accumulator, BiFunction<U,T,U> op) { return reduce(accumulator, op, this.first); }

    private <U> U reduce(U accumulator, BiFunction<U,T,U> op, Node<T> node) {
        return node == null ? accumulator : reduce(op.apply(accumulator, node.item), op, node.next);
    }

    public int count(Predicate<T> p) { return count(p, this.first); }

    protected static <T> int count(Predicate<T> p, Node<T> node) {
        return node == null ? 0 : count(p, node.next) + (p.test(node.item) ? 1 : 0);
    }

    public FunList<T> removeWithFilter(T x) { return filter(Predicate.not(item -> item == x)); }

    public FunList<T> filter(Predicate<T> p) { return new FunList<>(filter(p, this.first)); }

    protected static <T> Node<T> filter(Predicate<T> p, Node<T> node) {
        return node == null ? null :
                p.test(node.item) ?
                        new Node<>(node.item, filter(p, node.next)) :
                        filter(p, node.next);
    }

    public static <T> FunList<T> flatten(FunList<FunList<T>> xss) {
        return new FunList<>(flatten(xss, xss.first.item.first));
    }

    private static <T> Node<T> flatten(FunList<FunList<T>> xss, Node<T> node) {
        return (xss == null || xss.first == null) ? null :
            node == null ?
                flatten(new FunList<>(xss.first.next), xss.first.next == null ? null : xss.first.next.item.first) :
                new Node<>(node.item, flatten(xss, node.next));
    }

    public static <T> FunList<T> flattenWithReduce(FunList<FunList<T>> xss) {
        return xss.reduce(new FunList<>(null), FunList::append);
    }

    public <U> FunList<U> flatMap(Function<T, FunList<U>> f) {
        return new FunList<>(flatMap(f, this.first, null));
    }

    private <U> Node<U> flatMap(Function<T, FunList<U>> f, Node<T> tNode, Node<U> uNode) {
        return tNode == null && uNode == null ? null :
            uNode == null ?
                    flatMap(f, tNode.next, f.apply(tNode.item).first) :
                    new Node<>(uNode.item, flatMap(f, tNode, uNode.next));
    }

    public FunList<T> scan(BinaryOperator<T> f) {
        return new FunList<>(scan(f, this.first, null));
    }

    private Node<T> scan(BinaryOperator<T> f, Node<T> curr, T prevItem) {
        return curr != null && prevItem == null ?
            new Node<>(curr.item, scan(f, curr.next, curr.item)) :
                curr == null ?
                    null :
                    new Node<>(f.apply(prevItem, curr.item), scan(f, curr.next, f.apply(prevItem, curr.item)));
    }

    public static void main(String[] args) {
        FunList<Integer> empty = new FunList<>(null),
            list1 = cons(9, cons(13, cons(0, empty))), // 9 13 0
            list2 = cons(7, list1), // 7 9 13 0
            list3 = cons(8, list1), // 8 9 13 0
            list4 = list1.insert(1, 12), // 9 12 13 0
            list5 = list2.removeAt(3), // 7 9 13
            list6 = list5.reverse(), // 13 9 7
            list7 = list6.append(list5), // 13 9 7 7 9 13
            list8 = list7.remove(7), // 13 9 9 13
            list9 = list7.removeWithFilter(7), // 13 9 9 13
            list10 = list8.filter(i -> i == 9); // 9 9
        int sum = list7.reduce(0, Integer::sum); // 58
        int count = list4.count(i -> i == 12 || i == 0); // 2
        FunList<FunList<Integer>> empty2 = new FunList<>(null),
            list11 = cons(list1, cons(list2, cons(list5, empty2))); // [[9, 13, 0], [7, 9, 13, 0], [7, 9, 3]]
        FunList<Integer> list12 = flatten(list11), // 9 13 0 7 9 13 0 7 9 13
            list13 = flattenWithReduce(list11), // 9 13 0 7 9 13 0 7 9 13
            list14 = list9.flatMap(i -> cons(i, cons(i, empty))), // 13 13 9 9 9 9 13 13
            list15 = list14.scan(Integer::sum); // 13 26 35 44 53 62 75 88
        System.out.println(list15);
    }
}
