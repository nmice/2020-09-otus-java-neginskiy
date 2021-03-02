package ru.otus.aoplogger.model;

import ru.otus.aoplogger.annotation.LogMethodParameters;

public interface TestLoggingInterface {

    @LogMethodParameters
    void calculation();

    @LogMethodParameters
    void calculation(int param1);

    void calculation(int param1, int param2);

    @LogMethodParameters
    void calculation(int param1, int param2, String param3);
}
