package com.bobocode;

import lombok.SneakyThrows;

public class ThreadStates {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(new Thread(()->{}).getState()); //NEW
        System.out.println(Thread.currentThread().getState()); //RUNNABLE
        System.out.println(runningThread(() -> wait(args)).getState()); //WAITING
        notify(args);
        Thread blocker = runningThread(() -> lock(args, 100));
        System.out.println(runningThread(() -> lock(args, 20)).getState()); //BLOCKED
        blocker.join();
        System.out.println(blocker.getState()); //TERMINATED
    }

    @SneakyThrows
    public static void wait(Object monitor) {
        synchronized (monitor) {
            monitor.wait();
        }
    }

    @SneakyThrows
    public static void notify(Object monitor) {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    @SneakyThrows
    private static void lock(Object monitor, int mills) {
        synchronized (monitor) {
            Thread.sleep(mills);
        }
    }

    @SneakyThrows
    public static Thread runningThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(10);
        return thread;
    }
}
