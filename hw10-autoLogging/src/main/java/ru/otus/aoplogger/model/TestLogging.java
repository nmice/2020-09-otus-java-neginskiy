package ru.otus.aoplogger.model;


public class TestLogging implements TestLoggingInterface {

    @Override
    public void calculation() {
        System.out.println("GET RESULT WO PARAMS");
    }

    @Override
    public void calculation(int param1) {
        System.out.println("GET RESULT WITH 1 PARAM");
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("GET RESULT WITH 2 PARAM");
    }

    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("GET RESULT WITH 3 PARAM");
    }
}
