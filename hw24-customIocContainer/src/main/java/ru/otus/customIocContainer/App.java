package ru.otus.customIocContainer;

import ru.otus.customIocContainer.appcontainer.AppComponentsContainerImpl;
import ru.otus.customIocContainer.appcontainer.api.AppComponentsContainer;
import ru.otus.customIocContainer.config.AppConfig;
import ru.otus.customIocContainer.services.GameProcessor;
import ru.otus.customIocContainer.services.GameProcessorImpl;

/**
 * Neginskiy M.B. 19.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Собственный IoC контейнер
 * Цель:
 * В процессе создания своего контекста понять как работает основная часть Spring framework.
 * <p>
 * Обязательная часть:
 * Скачать заготовку приложения тренажера таблицы умножения из репозитория с примерами
 * В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
 * основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent
 * В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl
 * Дополнительное задание (можно не делать):
 * <p>
 * Разделить AppConfig на несколько классов и распределить по ним создание компонентов.
 * В AppComponentsContainerImpl добавить конструктор, который обрабатывает несколько классов-конфигураций
 * <p>
 * Дополнительное задание (можно не делать):
 * В AppComponentsContainerImpl добавить конструктор, который принимает на вход имя пакета,
 * и обрабатывает все имеющиеся там классы-конфигурации (см. зависимости в pom.xml)
 */
public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        //AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");

        // Обязательный вариант
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);//TODO по интерфейсу
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);//TODO по реализации
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");//TODO по имени

        gameProcessor.startGame();
    }
}
