package com.bobocode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.bobocode.MergeSort.mergeSort;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class SortingRunner {
    public static void main(String[] args) {
        System.out.println("CPU number: " + Runtime.getRuntime().availableProcessors());
        Supplier<int[]> arraySupplier = () -> ThreadLocalRandom.current().ints(20_000_000).toArray();

        System.out.println("Regular merge sort by MergeSort:");
        var stat = Stream.generate(arraySupplier)
                .limit(10)
                .mapToLong(arr -> {
                    long start = System.nanoTime();
                    mergeSort(arr);
                    return System.nanoTime() - start;
                })
                .map(elapsedNanos -> MILLISECONDS.convert(elapsedNanos, NANOSECONDS))
                .peek(elapsedTime -> System.out.println(elapsedTime + "ms"))
                .summaryStatistics();
        System.out.println(stat);

        System.out.println("Concurrent merge sort by MergeSortAction with threshold 10000:");
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        var actionStat = Stream.generate(arraySupplier)
                .limit(10)
                .map(MergeSortAction::new)
                .mapToLong(task -> {
                    long start = System.nanoTime();
                    forkJoinPool.invoke(task);
                    return System.nanoTime() - start;
                })
                .map(elapsedNanos -> MILLISECONDS.convert(elapsedNanos, NANOSECONDS))
                .peek(elapsedTime -> System.out.println(elapsedTime + "ms"))
                .summaryStatistics();
        System.out.println(actionStat);

        System.out.println("Concurrent merge sort by MergeSortTask with threshold 20000:");
        var taskStat = Stream.generate(arraySupplier)
                .limit(10)
                .map(MergeSortTask::new)
                .mapToLong(MergeSortTask::invoke) //implicitly run on ForkJoin Common pool don't need to call ForkJoin directly
                .map(elapsedNanos -> MILLISECONDS.convert(elapsedNanos, NANOSECONDS))
                .peek(elapsedTime -> System.out.println(elapsedTime + "ms"))
                .summaryStatistics();
        System.out.println(taskStat);
    }
}
