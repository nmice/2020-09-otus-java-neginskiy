package ru.otus.aoplogger;

import ru.otus.aoplogger.proxy.Ioc;
import ru.otus.aoplogger.model.TestLoggingInterface;

/**
 * Neginskiy M.B. 26.02.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Автоматическое логирование.
 * Цель: Понять как реализуется AOP, какие для этого есть технические средства.
 * Разработайте такой функционал:
 * метод класса можно пометить самодельной аннотацией @Log, например, так:
 * <p>
 * class TestLogging {
 *
 * @Log public void calculation(int param) {};
 * }
 * <p>
 * При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
 * Например так.
 * <p>
 * class Demo {
 * public void action() {
 * new TestLogging().calculation(6);
 * }
 * }
 * <p>
 * В консоле дожно быть:
 * executed method: calculation, param: 6
 * <p>
 * Обратите внимание: явного вызова логирования быть не должно.
 * <p>
 * Учтите, что аннотацию можно поставить, например, на такие методы:
 * public void calculation(int param1)
 * public void calculation(int param1, int param2)
 * public void calculation(int param1, int param2, String param3)
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("***LETS GO***");
        TestLoggingInterface testLogging = Ioc.createTestLogging();
        testLogging.calculation();
        testLogging.calculation(6);
        testLogging.calculation(6, 7);
        testLogging.calculation(6,7,"Ok");
    }
}
