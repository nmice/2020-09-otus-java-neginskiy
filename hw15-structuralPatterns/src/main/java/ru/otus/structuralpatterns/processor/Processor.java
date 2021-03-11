package ru.otus.structuralpatterns.processor;

import ru.otus.structuralpatterns.model.Message;

public interface Processor {

    Message process(Message message);
}
