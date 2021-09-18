package com.bobocode;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ForkJoinMergeSort {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        System.out.println("Concurrent merge sort");
        LongSummaryStatistics stat =
                Stream.iterate(1, i -> i <= 10, i -> ++i)
                        .mapToLong(i -> {
                            int[] arr = ThreadLocalRandom.current().ints().limit(20_000_000).toArray();
                            long start = System.currentTimeMillis();
                            MergeSortAction main = new MergeSortAction(arr);
                            forkJoinPool.invoke(main);
                            return System.currentTimeMillis() - start;
                        })
                        .peek(System.out::println)
                        .summaryStatistics();

        System.out.println(stat);
    }

    @RequiredArgsConstructor
    static class MergeSortAction extends RecursiveAction {
        private final int[] arr;

        @Override
        protected void compute() {
            if (arr.length < 2) return;
            int n = arr.length / 2;
            int[] left = Arrays.copyOfRange(arr, 0, n);
            int[] right = Arrays.copyOfRange(arr, n, arr.length);

            MergeSortAction leftAction = new MergeSortAction(left);
            MergeSortAction rightAction = new MergeSortAction(right);
            leftAction.fork();
            rightAction.compute();
            leftAction.join();
            merge(arr, left, right);
        }
    }

    private static void merge(int[] arr, int[] left, int[] right) {
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
