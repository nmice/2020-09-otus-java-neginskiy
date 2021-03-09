package ru.otus.structuralpatterns.processor.homework;

import ru.otus.structuralpatterns.model.Message;
import ru.otus.structuralpatterns.processor.Processor;

import java.util.function.Supplier;

public class ProcessorExceptionEvenSec implements Processor {

    private final Supplier<Long> timeProvider = System::currentTimeMillis;

    public Supplier<Long> getTimeProvider() {
        return timeProvider;
    }

    @Override
    public Message process(Message message) {
        long sec = getTimeProvider().get() / 1000;
        boolean isEvenSec = sec % 2 == 0;
        if (isEvenSec) {
            System.out.println("Is Even Second: " + sec);
            throw new IllegalStateException();
        }
        System.out.println("Is Not Even Second: " + sec);
        return message;
    }
}
