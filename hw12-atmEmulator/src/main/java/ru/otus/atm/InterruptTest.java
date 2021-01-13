package ru.otus.atm;

public class InterruptTest {

    private static int сount = 1;

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()){
                    System.out.println("ON " + сount + " STEP MOTHERF**KERS TRYING TO INTERRUPT ME...I RESET THE FLAG");
                }
                System.out.println(сount++ + " STEP OK");
                int b = 0;
                for (int i = 0; i < 1000000000; i++) {
                    b += ++i;
                }
            }
        });

        Thread threadStopper = new Thread(() -> {
            thread.interrupt();
            System.out.println("STOP THREAD!!!!!!!!");
        });

        int b = 0;
        thread.start();
        System.out.println("START THREAD-STEPMAKER...");
        for (int i = 0; i < 10000000; i++) {
            b += ++i;
        }
        threadStopper.start();
        System.out.println("START THREAD-STOPPER...");
        System.out.println("_0 IS INTERRUPTED THREAD = " + thread.isInterrupted());
        for (int i = 0; i < 10000000; i++) {
            b += ++i;
        }
        System.out.println("_1 IS INTERRUPTED THREAD = " + thread.isInterrupted());
        System.out.println("JUST WAIT A SECOND...");
        for (int i = 0; i < 1000000000; i++) {
            b += ++i;
        }
        System.out.println("_2 IS INTERRUPTED THREAD = " + thread.isInterrupted());
        System.out.println("_3 IS INTERRUPTED THREAD = " + thread.isInterrupted());
    }
}
