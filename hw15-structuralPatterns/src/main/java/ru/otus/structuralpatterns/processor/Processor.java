package ru.otus.structuralpatterns.processor;

import ru.otus.structuralpatterns.model.Message;

public interface Processor {

    Message process(Message message);

    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field12
}
