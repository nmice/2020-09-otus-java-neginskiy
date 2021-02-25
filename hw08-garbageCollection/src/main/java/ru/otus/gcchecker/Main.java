package ru.otus.gcchecker;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Neginskiy M.B. 18.01.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Сравнение разных сборщиков мусора
 * Цель: на примере простого приложения понять какое влияние оказывают сборщики мусора
 * Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа
 * (young, old) и время которое ушло на сборки в минуту.
 * <p>
 * Добиться OutOfMemory в этом приложении через медленное подтекание по памяти
 * (например добавлять элементы в List и удалять только половину).
 * <p>
 * <p>
 * Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало
 * с OOM примерно через 5 минут после начала работы.
 * <p>
 * Собрать статистику (количество сборок, время на сборки) по разным GC.
 * <p>
 * !!! Сделать выводы !!!
 * ЭТО САМАЯ ВАЖНАЯ ЧАСТЬ РАБОТЫ:
 * Какой gc лучше и почему?
 * <p>
 * Выводы оформить в файле Сonclusions.md в корне папки проекта.
 * Результаты измерений сведите в таблицу.
 * <p>
 * Попробуйте провести этот эксперимент на небольшом хипе порядка 256Мб, и на максимально возможном, который у вас может быть.
 * <p>
 */
public class Main {
    private static final long MILLIS_IN_MIN = 60000;
    private static final long MILLIS_IN_SEC = 1000;
    private static long currentSec = 0;
    private static long minorGcDurationSum = 0;
    private static long majorGcDurationSum = 0;
    private static long minorGcNumber = 0;
    private static long majorGcNumber = 0;
    private static long maxGcDuration = 0;
    private static boolean tenSecStatIsNotPrinted = false;

    public static void main(String[] args) throws Exception {
        System.out.println("***LETS GO***");
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int size = 38_200_000;
        //int loopCounter = 1000;
        int loopCounter = 100000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");

        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);
        mbean.run();

        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");

                    collectStatistics(gcName, duration);
                    long newSec = (startTime - MILLIS_IN_MIN * currentSec) / MILLIS_IN_SEC;

                    if (startTime / MILLIS_IN_MIN > currentSec) {
                        currentSec++;
                        System.err.println("TOTAL for " + currentSec + " min:\r\n" +
                                getStatString());
                        resetStat();
                    } else if (tenSecStatIsNotPrinted && newSec % 10 == 0) {
                        System.err.println(currentSec + " Min, " + newSec + " Sec:\r\n" +
                                getStatString());
                        tenSecStatIsNotPrinted = false;
                    }
                    if (newSec % 10 != 0) {
                        tenSecStatIsNotPrinted = true;
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static String getStatString() {
        return "minorNumber=" + minorGcNumber +
                ", minorTime=" + minorGcDurationSum +
                ", minorAvTime=" + (double) minorGcDurationSum / (double) minorGcNumber +
                "\r\nmajorNumber=" + majorGcNumber +
                ", majorTime=" + majorGcDurationSum +
                ", majorAvTime=" + (double) majorGcDurationSum / (double) majorGcNumber +
                ", maxGcDuration=" + maxGcDuration;
    }

    private static void resetStat() {
        minorGcNumber = 0;
        minorGcDurationSum = 0;
        majorGcNumber = 0;
        majorGcDurationSum = 0;
        tenSecStatIsNotPrinted = false;
    }

    private static void collectStatistics(String gcName, long duration) {
        if (gcName.equals("G1 Young Generation") ||
                gcName.equals("Copy") ||
                gcName.equals("PS Scavenge")) {
            minorGcNumber++;
            minorGcDurationSum += duration;
        } else if (gcName.equals("G1 Old Generation") ||
                gcName.equals("MarkSweepCompact") ||
                gcName.equals("PS MarkSweep")) {
            majorGcNumber++;
            majorGcDurationSum += duration;
        }
        if (duration > maxGcDuration) {
            maxGcDuration = duration;
        }
    }
}
