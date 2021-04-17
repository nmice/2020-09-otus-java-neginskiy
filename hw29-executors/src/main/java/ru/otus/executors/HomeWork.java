package ru.otus.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Neginskiy M.B. 14.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Последовательность чисел
 * Цель:
 * Освоить базовые механизмы синхронизации.
 * <p>
 * Два потока печатают числа от 1 до 10, потом от 10 до 1. Надо сделать так, чтобы числа чередовались,
 * т.е. получился такой вывод: Поток 1: 1 3 5 7 9 9 7 5 3 1 3 5..... Поток 2: 2 4 6 8 10 8 6 4 2 2 4 6....
 * Всегда должен начинать Поток 1.
 */
public class HomeWork {
    private static final AtomicInteger ATOMIC_INT = new AtomicInteger(1);
    private static final AtomicBoolean INCREMENT_FLAG = new AtomicBoolean(true);
    private static final AtomicBoolean SLEEP_1_FLAG = new AtomicBoolean(false);
    private static final AtomicBoolean SLEEP_2_FLAG = new AtomicBoolean(true);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        HomeWork homeWork = new HomeWork();
        ExecutorService executor1 = Executors.newFixedThreadPool(2);
        Future<?> f1 = executor1.submit(() -> homeWork.algorithm("FIRST", SLEEP_1_FLAG, SLEEP_2_FLAG));
        Future<?> f2 = executor1.submit(() -> homeWork.algorithm("SECOND", SLEEP_2_FLAG, SLEEP_1_FLAG));
        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(() -> {
            f1.cancel(true);
            f2.cancel(true);
            //executor1.shutdownNow(); - 2 вариант
        }, 30, 1, TimeUnit.SECONDS);
    }

    /**
     * Если направление на увеличение печатаем i, прибавляем 1
     * Если i=10, меняем направление
     * Если направление на уменьшение, печатаем i, отнимаем 1
     * Если i=1, меняем направление
     */
    private synchronized void algorithm(String threadName, AtomicBoolean iMustSleep, AtomicBoolean anotherMustSleep) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (iMustSleep.get()) {
                    this.wait();
                }
                if (INCREMENT_FLAG.get()) {
                    System.out.println(threadName + " : " + ATOMIC_INT.getAndIncrement());
                    if (ATOMIC_INT.get() == 10) {
                        INCREMENT_FLAG.set(false);
                    }
                } else {
                    System.out.println(threadName + " : " + ATOMIC_INT.getAndDecrement());
                    if (ATOMIC_INT.get() == 1) {
                        INCREMENT_FLAG.set(true);
                    }
                }
                iMustSleep.set(true);
                anotherMustSleep.set(false);
                notifyAll();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
