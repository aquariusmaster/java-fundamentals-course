package com.bobocode;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
public class MergeSortAction extends RecursiveAction {
    private final int[] arr;

    @Override
    protected void compute() {
        if (arr.length < 2) return;
        int n = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, n);
        int[] right = Arrays.copyOfRange(arr, n, arr.length);

        MergeSortAction leftAction = new MergeSortAction(left);
        leftAction.fork();
        MergeSortAction rightAction = new MergeSortAction(right);
        rightAction.compute();
        leftAction.join();
        merge(arr, left, right);
    }

    private void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0;
        while (i < left.length && j < right.length) {
            if (left[i] > right[j]) {
                arr[i + j] = right[j++];
            } else {
                arr[i + j] = left[i++];
            }
        }
        System.arraycopy(left, i, arr, i + j, left.length - i);
        System.arraycopy(right, j, arr, i + j, right.length - j);
    }
}