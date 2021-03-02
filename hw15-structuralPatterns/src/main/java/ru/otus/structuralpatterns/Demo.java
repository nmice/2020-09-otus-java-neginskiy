package ru.otus.structuralpatterns;

import ru.otus.structuralpatterns.handler.ComplexProcessor;
import ru.otus.structuralpatterns.listener.ListenerPrinter;
import ru.otus.structuralpatterns.model.Message;
import ru.otus.structuralpatterns.processor.LoggerProcessor;
import ru.otus.structuralpatterns.processor.ProcessorConcatFields;
import ru.otus.structuralpatterns.processor.ProcessorUpperField10;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
