# Binary Search Tree

Insert and search for numbers in a binary tree.

Write a class that represents a binary search tree and offers the following methods:

- `getRoot()`: returns the root of the tree
- `insert(T value)`: inserts the specified element into this tree
- `getAsSortedList()`: returns all the elements of the tree in the form of an ordered list. For example:
    ```    
              4
            /   \
           2     6       ==>    [1, 2, 3, 4, 5, 6, 7]
          / \   / \
         1   3 5   7
    ```
- `getAsLevelOrderList()`: returns items sorted by level in a list. For example:
    ```    
              4
            /   \
           2     6       ==>    [4, 2, 6, 1, 3, 5, 7]
          / \   / \
         1   3 5   7
    ```