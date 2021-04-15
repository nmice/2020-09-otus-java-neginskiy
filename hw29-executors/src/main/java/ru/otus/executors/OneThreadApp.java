package ru.otus.executors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class OneThreadApp {
    private static AtomicInteger atomicInteger = new AtomicInteger(1);
    private static AtomicBoolean incrementFlag = new AtomicBoolean(true);

    public static void main(String[] args) {
        algorithm();
    }

    private static void algorithm() {
        while (true) {
            if (incrementFlag.get()) {
                System.out.println(atomicInteger.getAndIncrement());
                if (atomicInteger.get() == 10) {
                    incrementFlag.set(false);
                }
            }
            else {
                System.out.println(atomicInteger.getAndDecrement());
                if (atomicInteger.get() == 1) {
                    incrementFlag.set(true);
                }
            }
        }
    }
}
