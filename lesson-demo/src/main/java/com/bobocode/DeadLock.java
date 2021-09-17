package com.bobocode;

import lombok.SneakyThrows;

public class DeadLock {

    @SneakyThrows
    public static void main(String[] args) {
        Thread t1 = new Thread(DeadLock::method1);
        Thread t2 = new Thread(DeadLock::method2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("end");
    }

    @SneakyThrows
    public synchronized static void method1() {
        Thread.sleep(1000);
        method2();
    }

    @SneakyThrows
    public synchronized static void method2() {
        Thread.sleep(1000);
        method1();
    }
}
