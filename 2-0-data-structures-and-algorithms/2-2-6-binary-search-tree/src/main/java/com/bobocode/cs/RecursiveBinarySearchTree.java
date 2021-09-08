package com.bobocode.cs;

import java.util.Objects;
import java.util.function.Consumer;

public class RecursiveBinarySearchTree<T extends Comparable<T>> implements BinarySearchTree<T> {

    private static class Node<T> {
        private T element;
        private Node<T> left;
        private Node<T> right;

        public Node(T element) {
            this.element = element;
        }
    }

    private Node<T> root;
    private int size;

    public static <T extends Comparable<T>> RecursiveBinarySearchTree<T> of(T... elements) {
        RecursiveBinarySearchTree<T> tree = new RecursiveBinarySearchTree<>();
        for (T el : elements) {
            tree.insert(el);
        }
        return tree;
    }

    @Override
    public boolean insert(T element) {
        if (root == null) {
            root = new Node<>(element);
            size++;
            return true;
        }
        if (insertToTree(root, element)) {
            size++;
            return true;
        }
        return false;
    }

    private boolean insertToTree(Node<T> node, T element) {
        if (node.element.compareTo(element) > 0) {
            if (node.left == null) {
                node.left = new Node<>(element);
                return true;
            } else {
                return insertToTree(node.left, element);
            }
        }
        if (node.element.compareTo(element) < 0) {
            if (node.right == null) {
                node.right = new Node<>(element);
                return true;
            } else {
                return insertToTree(node.right, element);
            }
        }
        return false;
    }

    @Override
    public boolean contains(T element) {
        Objects.requireNonNull(element);
        return getChildNodeRecursively(root, element) != null;
    }

    private Node<T> getChildNodeRecursively(Node<T> node, T element) {
        if (node == null) return null;
        if (node.element.compareTo(element) > 0) {
            return getChildNodeRecursively(node.left, element);
        }
        if (node.element.compareTo(element) < 0) {
            return getChildNodeRecursively(node.right, element);
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        if (root == null || (root.left == null && root.right == null)) return 0;
        return findDepth(root) - 1;
    }

    private int findDepth(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(findDepth(node.left), findDepth(node.right));
    }

    @Override
    public void inOrderTraversal(Consumer<T> consumer) {
        traverseRecursively(root, consumer);
    }

    private void traverseRecursively(Node<T> node, Consumer<T> consumer) {
        if (node == null) return;
        traverseRecursively(node.left, consumer);
        consumer.accept(node.element);
        traverseRecursively(node.right, consumer);
    }
}
