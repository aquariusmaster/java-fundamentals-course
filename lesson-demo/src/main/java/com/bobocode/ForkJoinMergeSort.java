package com.bobocode;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ForkJoinMergeSort {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Supplier<int[]> arraySupplier = () -> ThreadLocalRandom.current().ints().limit(20_000_000).toArray();
        System.out.println("Concurrent merge sort");
        LongSummaryStatistics actionStat =
                Stream.generate(arraySupplier)
                        .limit(10)
                        .mapToLong(arr -> {
                            long start = System.currentTimeMillis();
                            MergeSortAction main = new MergeSortAction(arr);
                            forkJoinPool.invoke(main);
                            return System.currentTimeMillis() - start;
                        })
                        .peek(duration -> System.out.println(duration + "ms"))
                        .summaryStatistics();

        System.out.println(actionStat);
        LongSummaryStatistics taskStat =
                Stream.generate(arraySupplier)
                        .limit(10)
                        .map(MergeSortTask::new)
                        .mapToLong(forkJoinPool::invoke)
                        .peek(duration -> System.out.println(duration + "ms"))
                        .summaryStatistics();
        System.out.println(taskStat);
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

    @RequiredArgsConstructor
    static class MergeSortTask extends RecursiveTask<Long> {
        private final int[] arr;

        @Override
        protected Long compute() {
            var start = System.currentTimeMillis();
            if (arr.length < 2) return System.currentTimeMillis() - start;
            var mid = arr.length / 2;
            var left = Arrays.copyOfRange(arr, 0, mid);
            var right = Arrays.copyOfRange(arr, mid, arr.length);

            var leftAction = new MergeSortAction(left);
            leftAction.fork();
            var rightAction = new MergeSortAction(right);
            rightAction.compute();
            leftAction.join();
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
}
