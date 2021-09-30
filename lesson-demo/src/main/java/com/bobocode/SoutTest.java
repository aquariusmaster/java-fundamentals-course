package com.bobocode;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class SoutTest {
    private static boolean stopRequested;

    @SneakyThrows
    public static void main(String[] args) {
        var back = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                if (i == 0) {
                    System.out.println(Thread.currentThread().getName() + " in run() i=" + i + ", time=" + System.currentTimeMillis());
                }
                i++;
            }
            System.out.println(i);
        });
        back.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }

}
