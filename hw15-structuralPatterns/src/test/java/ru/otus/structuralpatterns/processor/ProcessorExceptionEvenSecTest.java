package ru.otus.structuralpatterns.processor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.structuralpatterns.model.Message;
import ru.otus.structuralpatterns.processor.homework.ProcessorExceptionEvenSec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ProcessorExceptionEvenSecTest {

    private ProcessorExceptionEvenSec spyTestObject;

    @BeforeEach
    public void buildSpy() {
        spyTestObject = spy(new ProcessorExceptionEvenSec());
    }

    @AfterEach
    public void resetSpy() {
        Mockito.reset(spyTestObject);
    }

    @Test
    @DisplayName("Тестируем вызов процессора исключений в нечетную секунду")
    void processorExceptionTestFail() {
        //given
        when(spyTestObject.getTimeProvider()).thenReturn(() -> 48000232L);
        var oldMessage = getMessage();
        assertThatThrownBy(() -> spyTestObject.process(oldMessage)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Тестируем вызов процессора исключений в четную секунду")
    void processorExceptionTestSuccess() {
        //given
        when(spyTestObject.getTimeProvider()).thenReturn(() -> 11001232L);
        var oldMessage = getMessage();

        var newMessage = spyTestObject.process(oldMessage);
        assertThat(oldMessage).isEqualTo(newMessage);
    }

    private Message getMessage() {
        return new Message.Builder(1L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .build();
    }
}