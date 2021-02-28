package ru.otus.aoplogger.proxy;

import ru.otus.aoplogger.model.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestLoggingInvocationHandler implements InvocationHandler {

    private final TestLoggingInterface testLogging;
    private final List<Method> annotatedMethodList;

    public TestLoggingInvocationHandler(TestLoggingInterface testLogging,
                                        List<Method> annotatedMethodList) {
        this.testLogging = testLogging;
        this.annotatedMethodList = annotatedMethodList;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (annotatedMethodList.contains(method)) {
            String params = getParamString(args);
            System.out.println("executed method: " + method.getName() + ", params: " + params);
        }
        return method.invoke(testLogging, args);
    }

    private String getParamString(Object[] args) {
        if (args == null) {
            return "";
        }
        String result = Arrays.deepToString(args);
        return result.substring(1, result.length() - 1);
    }
}
