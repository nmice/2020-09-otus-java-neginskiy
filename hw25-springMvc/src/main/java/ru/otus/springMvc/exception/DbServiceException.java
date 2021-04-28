package ru.otus.springMvc.exception;

public class DbServiceException extends OtusHomeworkException {
    public DbServiceException(Exception e) {
        super(e);
    }
}
