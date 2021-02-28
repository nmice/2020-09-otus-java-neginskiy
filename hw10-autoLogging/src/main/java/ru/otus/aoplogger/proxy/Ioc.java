package ru.otus.aoplogger.proxy;

import ru.otus.aoplogger.annotation.LogMethodParameters;
import ru.otus.aoplogger.model.TestLogging;
import ru.otus.aoplogger.model.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    public static TestLoggingInterface createTestLogging() {
        List<Method> annotatedMethodList = Arrays.stream(TestLoggingInterface.class.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(LogMethodParameters.class))
                .collect(Collectors.toList());
        InvocationHandler handler = new TestLoggingInvocationHandler(new TestLogging(), annotatedMethodList);
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }
}
