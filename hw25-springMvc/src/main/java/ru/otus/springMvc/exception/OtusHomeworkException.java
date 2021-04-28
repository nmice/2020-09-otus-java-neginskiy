package ru.otus.springMvc.exception;

public class OtusHomeworkException extends RuntimeException {
    public OtusHomeworkException(Exception e) {
        super(e);
    }

    public OtusHomeworkException(String msg) {
        super(msg);
    }
}
