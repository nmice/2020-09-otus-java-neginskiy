package ru.otus.structuralpatterns;

import ru.otus.structuralpatterns.handler.ComplexProcessor;
import ru.otus.structuralpatterns.listener.homework.ListenerHistorySaver;
import ru.otus.structuralpatterns.model.Message;
import ru.otus.structuralpatterns.model.ObjectForMessage;
import ru.otus.structuralpatterns.processor.homework.ProcessorExceptionEvenSec;
import ru.otus.structuralpatterns.processor.homework.ProcessorField11SwapField12;

import java.util.Arrays;
import java.util.List;

/**
 * Neginskiy M.B. 02.03.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Обработчик сообщений
 * <p>
 * Реализовать to do из модуля homework:
 * 1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
 * 2. Сделать процессор, который поменяет местами значения field11 и field12
 * 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
 *      Секунда должна определяьться во время выполнения.
 * 4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
 * <p>
 * Цель: Применить на практике шаблоны проектирования.
 */
public class HomeWork {

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        var processors = List.of(new ProcessorField11SwapField12(),
                new ProcessorExceptionEvenSec());

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });
        var listenerHistorySaver = new ListenerHistorySaver();
        complexProcessor.addListener(listenerHistorySaver);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .field13(ObjectForMessage.builder().data(Arrays.asList("Elem1", "Elem2")).build())
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result: " + result);
        System.out.println("History: " + listenerHistorySaver.getHistoryList());
        System.out.println("Exception: " + complexProcessor.getExceptionCount());

        complexProcessor.removeListener(listenerHistorySaver);
    }
}
