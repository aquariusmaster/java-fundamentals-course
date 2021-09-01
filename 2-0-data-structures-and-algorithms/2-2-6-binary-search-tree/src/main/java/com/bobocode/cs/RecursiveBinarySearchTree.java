package com.bobocode.cs;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.Consumer;

public class RecursiveBinarySearchTree<T extends Comparable<T>> implements BinarySearchTree<T> {

    private int size;
    private int depth;
    private Node<T> root;

    @Setter
    @Getter
    private static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;

        public Node(T value) {
            this.value = value;
        }
    }

    public static <T extends Comparable<T>> RecursiveBinarySearchTree<T> of(T... elements) {
        RecursiveBinarySearchTree<T> tree = new RecursiveBinarySearchTree<>();
        for (T element : elements) {
            tree.insert(element);
        }
        return tree;
    }

    @Override
    public boolean insert(T element) {
        Objects.requireNonNull(element);
        if (root == null) {
            root = new Node<>(element);
            size++;
            return true;
        }
        int currentDepth = insert(root, element, 0);
        depth = Math.max(depth, currentDepth);
        return currentDepth > 0;
    }

    private int insert(Node<T> current, T element, int currentDepth) {
        int compareResult = current.value.compareTo(element);
        if (compareResult == 0) {
            return 0;
        } else if (compareResult > 0) {
            currentDepth++;
            if (current.left == null) {
                current.left = new Node<>(element);
                size++;
                return currentDepth;
            }
            return insert(current.left, element, currentDepth);
        } else {
            currentDepth++;
            if (current.right == null) {
                current.right = new Node<>(element);
                size++;
                return currentDepth;
            }
            return insert(current.right, element, currentDepth);
        }
    }

    @Override
    public boolean contains(T element) {
        Objects.requireNonNull(element);
        if (root == null) return false;
        Node<T> current = root;
        while (current != null) {
            int compareResult = current.value.compareTo(element);
            if (compareResult == 0) {
                return true;
            } else if (compareResult > 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
//        return depth; //to make tests work
        if (root == null || size == 1) return 0;
        return calculateDepth(root);
    }

    private int calculateDepth(Node<T> node) {
        if (node.left == null && node.right == null) {
            return 0;
        } else if (node.left != null && node.right == null) {
            return calculateDepth(node.left) + 1;
        } else if (node.left == null) {
            return calculateDepth(node.right) + 1;
        } else {
            return Math.max(calculateDepth(node.left), calculateDepth(node.right)) + 1;
        }
    }

    @Override
    public void inOrderTraversal(Consumer<T> consumer) {
        traverse(root, consumer);
    }

    private void traverse(Node<T> root, Consumer<T> consumer) {
        if (root == null) {
            return;
        }
        traverse(root.left, consumer);
        consumer.accept(root.value);
        traverse(root.right, consumer);
    }
}
