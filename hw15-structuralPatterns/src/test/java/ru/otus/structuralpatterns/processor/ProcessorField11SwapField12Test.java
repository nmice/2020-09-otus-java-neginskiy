package ru.otus.structuralpatterns.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.structuralpatterns.model.Message;
import ru.otus.structuralpatterns.processor.homework.ProcessorField11SwapField12;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorField11SwapField12Test {

    @Test
    @DisplayName("Тестируем результат работы процессора перестановки полей")
    void processorSwapTest() {
        //given
        var oldMessage = new Message.Builder(1L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .build();
        var newMessage = new ProcessorField11SwapField12().process(oldMessage);

        //then
        assertThat(oldMessage.getField1()).isEqualTo(newMessage.getField1());
        assertThat(oldMessage.getField11()).isEqualTo(newMessage.getField12());
        assertThat(oldMessage.getField12()).isEqualTo(newMessage.getField11());
    }
}