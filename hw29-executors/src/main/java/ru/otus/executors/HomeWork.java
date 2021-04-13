package ru.otus.executors;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Neginskiy M.B. 04.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Последовательность чисел
 * Цель:
 * Освоить базовые механизмы синхронизации.
 * <p>
 * Два потока печатают числа от 1 до 10, потом от 10 до 1. Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод: Поток 1: 1 3 5 7 9 9 7 5 3 1 3 5..... Поток 2: 2 4 6 8 10 8 6 4 2 2 4 6....
 * Всегда должен начинать Поток 1.
 */
public class HomeWork {
    static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {
        Runnable runnable = () -> {
            while (1 == 1) System.out.println("Thread" + Thread.currentThread().getId() + ":" +
                    (atomicInteger.get() < 10 ? atomicInteger.getAndIncrement() : atomicInteger.getAndDecrement())
            );
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }
}
