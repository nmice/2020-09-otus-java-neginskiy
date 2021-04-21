package ru.otus.springMvc.exception;

public class DaoException extends RuntimeException {
    public DaoException(Exception ex) {
        super(ex);
    }
}
