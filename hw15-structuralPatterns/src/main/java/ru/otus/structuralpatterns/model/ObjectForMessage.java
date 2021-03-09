package ru.otus.structuralpatterns.model;

import java.util.List;

public class ObjectForMessage {

    private ObjectForMessage(List<String> data) {
        this.data = data;
    }

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data + '}';
    }

    public static ObjectForMessage.Builder builder() {
        return new ObjectForMessage.Builder();
    }

    public static class Builder {
        private List<String> data;

        private Builder() {
        }

        public ObjectForMessage.Builder data(List<String> data) {
            this.data = data;
            return this;
        }

        public ObjectForMessage build() {
            return new ObjectForMessage(data);
        }
    }
}
