package ru.otus.aoplogger;

/**
 * Neginskiy M.B. 26.02.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Автоматическое логирование.
 * Цель: Понять как реализуется AOP, какие для этого есть технические средства.
 * Разработайте такой функционал:
 * метод класса можно пометить самодельной аннотацией @Log, например, так:
 *
 * class TestLogging {
 * @Log
 * public void calculation(int param) {};
 * }
 *
 * При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
 * Например так.
 *
 * class Demo {
 * public void action() {
 * new TestLogging().calculation(6);
 * }
 * }
 *
 * В консоле дожно быть:
 * executed method: calculation, param: 6
 *
 * Обратите внимание: явного вызова логирования быть не должно.
 *
 * Учтите, что аннотацию можно поставить, например, на такие методы:
 * public void calculation(int param1)
 * public void calculation(int param1, int param2)
 * public void calculation(int param1, int param2, String param3)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("***LETS GO***");
    }
}
