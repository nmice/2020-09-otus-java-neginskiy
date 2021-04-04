package ru.otus.cacheengine.exception;

public class OtusHomeworkException extends RuntimeException {
    public OtusHomeworkException(Exception e) {
        super(e);
    }

    public OtusHomeworkException(String msg) {
        super(msg);
    }
}
