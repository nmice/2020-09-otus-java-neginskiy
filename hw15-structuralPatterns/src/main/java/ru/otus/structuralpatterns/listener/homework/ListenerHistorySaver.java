package ru.otus.structuralpatterns.listener.homework;

import ru.otus.structuralpatterns.listener.Listener;
import ru.otus.structuralpatterns.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ListenerHistorySaver implements Listener {

    private List<History> historyList = new ArrayList<>();

    public List<History> getHistoryList() {
        return historyList;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyList.add(new History(oldMsg, newMsg));
    }

    private static class History {
        Message oldMsg;
        Message newMsg;

        public History(Message oldMsg,
                       Message newMsg) {
            this.oldMsg = oldMsg;
            this.newMsg = newMsg;
        }

        @Override
        public String toString() {
            return "History{\r\n" +
                    "oldMsg=" + oldMsg + ",\r\n" +
                    "newMsg=" + newMsg +
                    '}';
        }
    }
}
