package ru.otus.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Neginskiy M.B. 14.04.2021
 * <p>
 * �������� �������
 * ������������������ �����
 * ����:
 * ������� ������� ��������� �������������.
 * <p>
 * ��� ������ �������� ����� �� 1 �� 10, ����� �� 10 �� 1. ���� ������� ���, ����� ����� ������������,
 * �.�. ��������� ����� �����: ����� 1: 1 3 5 7 9 9 7 5 3 1 3 5..... ����� 2: 2 4 6 8 10 8 6 4 2 2 4 6....
 * ������ ������ �������� ����� 1.
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
            //executor1.shutdownNow(); - 2 �������
        }, 30, 1, TimeUnit.SECONDS);
    }

    /**
     * ���� ����������� �� ���������� �������� i, ���������� 1
     * ���� i=10, ������ �����������
     * ���� ����������� �� ����������, �������� i, �������� 1
     * ���� i=1, ������ �����������
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
