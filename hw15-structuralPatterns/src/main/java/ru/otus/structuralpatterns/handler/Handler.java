package ru.otus.structuralpatterns.handler;

import ru.otus.structuralpatterns.listener.Listener;
import ru.otus.structuralpatterns.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
