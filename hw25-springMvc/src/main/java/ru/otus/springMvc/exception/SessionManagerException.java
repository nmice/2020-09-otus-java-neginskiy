package ru.otus.springMvc.exception;

public class SessionManagerException extends OtusHomeworkException {
    public SessionManagerException(String msg) {
        super(msg);
    }

    public SessionManagerException(Exception e) {
        super(e);
    }
}
