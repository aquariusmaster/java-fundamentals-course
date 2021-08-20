package com.bobocode.cs;

import com.bobocode.cs.exception.EmptyStackException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * {@link LinkedStack} is a stack implementation that is based on singly linked generic nodes.
 * A node is implemented as inner static class {@link Node<T>}.
 *
 * @param <T> generic type parameter
 */
public class LinkedStack<T> implements Stack<T> {

    private Node<T> head;
    private int size;

    /**
     * This method creates a stack of provided elements
     *
     * @param elements elements to add
     * @param <T>      generic type
     * @return a new stack of elements that were passed as method parameters
     */
    public static <T> LinkedStack<T> of(T... elements) {
        LinkedStack<T> stack = new LinkedStack<>();
        for (T element : elements) {
            stack.push(element);
        }
        return stack;
    }

    /**
     * The method pushes an element onto the top of this stack. This has exactly the same effect as:
     * addElement(item)
     *
     * @param element elements to add
     */
    @Override
    public void push(T element) {
        Objects.requireNonNull(element);
        if (head == null) {
            head = new Node<>(element, null);
        } else {
            head = new Node<>(element, head);
        }
        size++;
    }

    /**
     * This method removes the object at the top of this stack
     * and returns that object as the value of this function.
     *
     * @return The object at the top of this stack
     * @throws EmptyStackException - if this stack is empty
     */
    @Override
    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T value = head.getValue();
        if (size-- == 1) {
            head = null;
        } else {
            head = head.getNext();
        }
        return value;
    }

    /**
     * Returns the number of elements in the stack
     *
     * @return number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if a stack is empty
     *
     * @return {@code true} if a stack is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class Node<T> {
        private T value;
        private Node<T> next;
    }

}
