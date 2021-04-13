package ru.otus.executors;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Neginskiy M.B. 04.04.2021
 * <p>
 * �������� �������
 * ������������������ �����
 * ����:
 * ������� ������� ��������� �������������.
 * <p>
 * ��� ������ �������� ����� �� 1 �� 10, ����� �� 10 �� 1. ���� ������� ���, ����� ����� ������������, �.�. ��������� ����� �����: ����� 1: 1 3 5 7 9 9 7 5 3 1 3 5..... ����� 2: 2 4 6 8 10 8 6 4 2 2 4 6....
 * ������ ������ �������� ����� 1.
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
