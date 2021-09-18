package com.bobocode;

import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Sorting {
    public static void main(String[] args) {
        System.out.println("CPU number: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Regular merge sort");
        Supplier<int[]> arraySupplier = () -> ThreadLocalRandom.current().ints().limit(20_000_000).toArray();
        LongSummaryStatistics stat =
                Stream.generate(arraySupplier)
                        .limit(10)
                        .mapToLong(arr -> {
                            long start = System.currentTimeMillis();
                            mergeSort(arr);
                            return System.currentTimeMillis() - start;
                        })
                        .peek(duration -> System.out.println(duration + "ms"))
                        .summaryStatistics();

        System.out.println(stat);
    }

    public static void mergeSort(int[] arr) {
        if (arr.length < 2) return;
        int n = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, n);
        int[] right = Arrays.copyOfRange(arr, n, arr.length);

        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
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
