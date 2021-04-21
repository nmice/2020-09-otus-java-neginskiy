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

/**
 * TODO имплементация
 */
public class AppComponentsContainerImpl implements AppComponentsContainer {
    /**
     * TODO сохранить 2 коллекции
     */
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        /**
         * TODO Просканировать папки на предмет реализаций
         * Найти реализацию - класс с аннотацией AppConfig
         * Создать экземпляр
         * Сохранить в мапу и лист
         */
        // You code here...
        final Object config = getConfigClassInstance(configClass);

        List<Method> beansMethods = Stream.of(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object getConfigClassInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Cannot instantiate config class");
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C component = null;
        if (componentClass.isInterface()) {
            /**
             * TODO Просканировать папки на предмет реализаций
             * Найти реализацию - класс с аннотацией AppConfig
             * Создать экземпляр
             * Сохранить в мапу и лист
             */
            Class<C> componentClassImpl;
            //component = componentClassImpl.getDeclaredConstructor().newInstance();

        } else {
            //component = componentClass.getDeclaredConstructor().newInstance();
        }
        return component;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        /**
         * TODO код здесь
         */
        return null;
    }
}
