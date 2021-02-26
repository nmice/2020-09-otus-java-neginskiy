package ru.otus.aoplogger.proxy;

import ru.otus.aoplogger.annotation.LogMethodParameters;
import ru.otus.aoplogger.model.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestLoggingInvocationHandler implements InvocationHandler {

    private final TestLoggingInterface testLogging;

    public TestLoggingInvocationHandler(TestLoggingInterface testLogging) {
        this.testLogging = testLogging;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(LogMethodParameters.class)) {
            String params = getParamString(args);
            System.out.println("executed method: " + method.getName() + ", params: " + params);
        }
        return method.invoke(testLogging, args);
    }

    private String getParamString(Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder paramBuilder = new StringBuilder();
        int paramsNumber = args.length;
        for (int i = 0; i < paramsNumber; i++) {
            if (i < paramsNumber - 1) {
                paramBuilder.append(args[i]).append(", ");
            } else {
                paramBuilder.append(args[i]);
            }
        }
        return paramBuilder.toString();
    }
}
