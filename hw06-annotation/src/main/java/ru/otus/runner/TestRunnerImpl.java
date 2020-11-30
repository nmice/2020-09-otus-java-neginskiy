package ru.otus.runner;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunnerImpl implements TestRunner {
    @Override
    public void runTestsForClass(String className) throws Exception {
        Class<?> classToTest = Class.forName(className);
        List<Method> classMethods = getClassMethods(classToTest);

        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        classMethods.forEach(method -> {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        });

        int testCounter = 0;
        int failedTestsCounter = 0;
        int successfulTestsCounter = 0;
        for (Method testMethod : testMethods) {
            //создать экземпляр класса-теста
            Object classToTestInstance = getClassToTestInstance(classToTest);
            try {
                System.out.println("***TEST " + ++testCounter + " BEGIN***");
                //выполнить все before
                invokeMethods(beforeMethods, classToTestInstance);
                //выполнить тестовый метод
                invokeMethod(testMethod, classToTestInstance);
                System.out.println("***TEST " + testCounter + " END***");
                //увеличить счетчик успешных тестов
                successfulTestsCounter++;
            } catch (Exception e) {
                System.out.println("***TEST " + testCounter + " FAILED***");
                //увеличить счетчик неуспешных тестов
                failedTestsCounter++;
            } finally {
                //выполнить все after
                invokeMethods(afterMethods, classToTestInstance);
                System.out.println();
            }
        }
        printStat(testCounter, failedTestsCounter, successfulTestsCounter);
    }

    private List<Method> getClassMethods(Class<?> classToTest) {
        return Arrays.asList(classToTest.getDeclaredMethods());
    }

    private Object getClassToTestInstance(Class<?> classToTest) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<?> classToTestConstructor = classToTest.getConstructor();
        return classToTestConstructor.newInstance();
    }

    private void invokeMethods(List<Method> beforeMethods, Object classToTestInstance) throws IllegalAccessException,
            InvocationTargetException {
        for (Method beforeMethod : beforeMethods) {
            invokeMethod(beforeMethod, classToTestInstance);
        }
    }

    private void invokeMethod(Method testMethod, Object classToTestInstance) throws IllegalAccessException, InvocationTargetException {
        testMethod.invoke(classToTestInstance);
    }

    private void printStat(int testCounter, int failedTestsCounter, int successfulTestsCounter) {
        System.out.printf("\r\n***RESULTS***\r\nTOTAL TESTS: %s\r\nSUCCESSFUL TESTS: %s\r\n" +
                "FAILED TESTS: %s\r\n", testCounter, successfulTestsCounter, failedTestsCounter);
    }
}