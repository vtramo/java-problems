# Stream Forker
One of the biggest limitations of the Stream API is that a stream can't be
executed more than once, and you can only get one result from the stream.
If you violate this constraint you get this:
```
java.lang.IllegalStateException: stream has already been operated upon or closed
```
Your job is to implement a `StreamForker` class that represents a `Stream`
on which you can perform multiple operations on it (in parallel). The class `StreamForker` has a
constructor that accepts a `Stream<T>` and offers the following methods:
- `StreamForker<T> fork(Object key, Function<Stream<T>, ?> f)`: this method accepts a
`key` that identifies an operation that will be performed on the stream
- `Results getResults():` this method returns the results of the all
operations. `Results` is an interface:
  ```
  public static interface Results {
    public <R> R get(Object key);
  }
  ```
  This interface has a single abstract method `<R> R get(Object key)` which
  returns the result of the operation identified by the supplied `key`.

Example of usage:
```
public static void main(String[] args) {
    final Stream<Integer> stream = Stream.<Integer>of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Results results = new StreamForker<Integer>(stream)
        .fork("x2", e -> e.map(i -> i * 2).toList())
        .fork("total", e -> e.mapToInt(i -> i).sum())
        .fork("theGreatest", e -> e.mapToInt(i -> i).max())
        .fork("onlyEven", e -> e.filter(i -> i % 2 == 0).toList())
        .getResults();
    System.out.println("x2 " + results.get("x2"));
    System.out.println("total " + results.get("total"));
    System.out.println("theGreatest " + ((OptionalInt) results.get("theGreatest")).getAsInt());
    System.out.println("onlyEven " + results.get("onlyEven"));
}

Output:

x2 [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
total 55
theGreatest 10
onlyEven [2, 4, 6, 8, 10]
```
## Hint
It is possible to implement such features using `Spliterator` and its
late-binding capability, together with `BlockingQueue`s and `Future`s.

Source: [Modern Java in Action - Manning](https://www.manning.com/books/modern-java-in-action)