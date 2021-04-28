package ru.otus.springMvc.exception;

public class DaoException extends OtusHomeworkException {
    public DaoException(Exception ex) {
        super(ex);
    }
}
