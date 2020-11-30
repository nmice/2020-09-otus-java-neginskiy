import ru.otus.runner.TestRunner;
import ru.otus.runner.TestRunnerImpl;

/**
 * Neginskiy M.B. 26.11.2020
 *
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Свой тестовый фреймворк
 * Цель: научиться работать с reflection и аннотациями,
 * понять принцип работы фреймворка junit.
 * Написать свой тестовый фреймворк.
 * <p>
 * Поддержать свои аннотации @ru.otus.annotations.Test, @ru.otus.annotations.Before, @ru.otus.annotations.After.
 * <p>
 * Запускать вызовом статического метода с именем класса с тестами.
 * <p>
 * Т.е. надо сделать:
 * 1) создать три аннотации - @ru.otus.annotations.Test, @ru.otus.annotations.Before, @ru.otus.annotations.After.
 * 2) Создать класс-тест, в котором будут методы, отмеченные аннотациями.
 * 3) Создать "запускалку теста". На вход она должна получать имя класса с тестами, в котором следует найти и запустить методы отмеченные аннотациями и пункта 1.
 * 4) Алгоритм запуска должен быть следующий::
 * метод(ы) ru.otus.annotations.Before
 * текущий метод ru.otus.annotations.Test
 * метод(ы) ru.otus.annotations.After
 * для каждой такой "тройки" надо создать СВОЙ объект класса-теста.
 * 5) Исключение в одном тесте не должно прерывать весь процесс тестирования.
 * 6) На основании возникших во время тестирования исключений вывести статистику выполнения тестов (сколько прошло успешно, сколько упало, сколько было всего)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("***Custom Test Framework***\r\n");
        TestRunner testRunner = new TestRunnerImpl();
            testRunner.runTestsForClass("ru.otus.test.TestClass");
    }
}
