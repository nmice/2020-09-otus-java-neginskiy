package ru.otus.customIocContainer.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.customIocContainer.appcontainer.api.AppComponent;
import ru.otus.customIocContainer.appcontainer.api.AppComponentsContainer;
import ru.otus.customIocContainer.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) getBeanByClassName(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private Object getConfigClassInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Cannot instantiate config class");
            throw new IllegalArgumentException(e);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object config = getConfigClassInstance(configClass);
        List<Method> orderedAppComponentMethods = Stream.of(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        for (Method method : orderedAppComponentMethods) {
            var methodArgs = getArgsArrayForMethod(method);
            try {
                Object resultBean = method.invoke(config, methodArgs);
                appComponents.add(resultBean);
                String beanName = method.getDeclaredAnnotation(AppComponent.class).name();
                appComponentsByName.put(beanName, resultBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object[] getArgsArrayForMethod(Method method) {
        Class<?>[] methodParametersTypes = method.getParameterTypes();
        return Stream.of(methodParametersTypes)
                .map(this::getBeanByClassName)
                .toArray();
    }

    private Object getBeanByClassName(Class<?> componentClass) {
        return appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Bean " + componentClass.getName() + "not found"));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
