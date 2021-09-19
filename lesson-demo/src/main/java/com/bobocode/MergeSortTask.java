package com.bobocode;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

@RequiredArgsConstructor
public class MergeSortTask extends RecursiveTask<Long> {
    private final static int THRESHOLD = 10_000;

    private final int[] arr;

    @Override
    protected Long compute() {
        var start = System.currentTimeMillis();
        if (arr.length < 2) return 0L;
        var mid = arr.length / 2;
        var left = Arrays.copyOfRange(arr, 0, mid);
        var right = Arrays.copyOfRange(arr, mid, arr.length);

        var leftTask = new MergeSortTask(left);
        var rightTask = new MergeSortTask(right);
        if (arr.length > THRESHOLD) {
            leftTask.fork();
            rightTask.compute();
            leftTask.join();
        } else {
            leftTask.compute();
            rightTask.compute();
        }
        merge(arr, left, right);
        return System.currentTimeMillis() - start;
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