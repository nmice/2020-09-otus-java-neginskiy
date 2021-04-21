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
        // Получаем бин из мапы по ключу - имени класса
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
        // Получить экземпляр конфига
        Object config = getConfigClassInstance(configClass);
        // Получить список всех методов, аннотированных AppComponent, упорядочить по order
        List<Method> beansMethods = Stream.of(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        // Пройти по списку методов
        for (Method method : beansMethods) {
            // Для каждого метода получить массив аргументов
            var args = getArgsArrayForMethod(method);
            try {
                // Получить инстанс бина, вызвав метод с аргументами у инстанса конфига
                Object resultBean = method.invoke(config, args);
                // Добавить бин в коллекцию
                appComponents.add(resultBean);
                // Получить имя бина для мапы из аннотации
                String beanName = method.getDeclaredAnnotation(AppComponent.class).name();
                // Добавить бин в мапу
                appComponentsByName.put(beanName, resultBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object[] getArgsArrayForMethod(Method method) {
        // Получить массив типов параметров метода
        Class<?>[] parameterTypes = method.getParameterTypes();
        // Получить массив параметров из мапы по типам
        return Stream.of(parameterTypes)
                .map(this::getBeanByClassName)
                .toArray();
    }

    private Object getBeanByClassName(Class<?> componentClass) {
        // Сверяем классы бинов из коллекции с запрашиваемым классом
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
