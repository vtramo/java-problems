# Fun List

Class `FunList<T>` represents immutable lists of `T` values, using immutable `Node<T>` objects. All operations
produce a new list instead of updating existing ones, so any number of operations on the same list could
proceed concurrently without synchronization. A new list may share nodes with existing ones; 
This is harmless because nodes are immutable.

The `Node<T>` inner class looks like this:
```
protected record Node<U>(U item, FunList.Node<U> next) { }
```
The `FunList<T>` class has only one instance field `final Node<T> first` and one constructor:
```
public FunList(Node<T> node) { this.first = node; }
```

Implement the following methods **using only the functional paradigm**:
- `public T get(int i)`: returns the `i`-th node of the list;
- `public static <T> FunList<T> cons(T item, FunList<T> list)`: creates a new list with a new head node with the 
value `item`; 
- `public FunList<T> insert(int i, T item)`: creates a new list with a new node at position `i`;
- `public FunList<T> removeAt(int i)`: creates a new list with the `i`-th node removed;
- `public FunList<T> remove(T x)`: creates a new list in which all occurrences of `x` have
  been removed;
- `public FunList<T> append(FunList<T> list2)`: creates a single list containing all items in
  `this` list and all items in `list2` in order of appearance;
- `public <U> U reduce(U accumulator, BiFunction<U,T,U> op)`: implements the reduce operation;
- `public int count(Predicate<T> p)`: returns the number of elements that satisfy predicate `p`;
- Show how to implement an alternative version of `remove`, called `removeWithFilter`, by a call to `filter` and
  using a lambda expression, and no explicit recursion;
- `public <U> FunList<U> map(Function<T,U> f)`: creates a new list with the elements mapped by the function `f`;
- `public static <T> FunList<T> flatten(FunList<FunList<T>> xss)`: creates a single
  list containing all the elements of the individual lists in `xss`, in order of appearance;
- Show how to implement an alternative version of `flatten`, called `flattenWithReduce`, using `reduce`, an
  empty list, `FunList::append` (or a lambda expression), and no explicit recursion;
- `public <U> FunList<U> flatMap(Function<T, FunList<U>> f)`: computes the lists `f.apply(x1), f.apply(x2), ... ,f.apply(xn)`
and then returns the concatenation or flattening of these lists;
- `public FunList<T> scan(BinaryOperator<T> f)`: return a list `y1, y2, ..., yn` of the same length as the
given list `x1, x2, ..., xn`. Here `y1` equals `x1`, and for `i > 1` it should hold that `yi` equals
`f.apply(y(i-1), xi)`.

Usage examples:
```
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
  }
```

Sources:
- https://thomasahle.com/teaching/pcpp2019/Week_4/exercise.pdf (last accessed 15 Jan 2023)
- Java Precisely, third edition - Example 154