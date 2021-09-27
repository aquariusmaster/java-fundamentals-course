package com.bobocode;

import java.util.concurrent.*;

public class Lesson {

    public static void main(String[] args) {

        Runnable hello = () -> System.out.println("Hello" + Thread.currentThread().getName());
        new Thread(hello).start(); //1

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(hello); //2
        executor.shutdown();

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.invoke(new HelloAction()); //3

        CompletableFuture.runAsync(hello); //4

    }

    static class HelloAction extends RecursiveAction {

        @Override
        protected void compute() {
            System.out.println("Hello" + Thread.currentThread().getName());
        }
    }
}
