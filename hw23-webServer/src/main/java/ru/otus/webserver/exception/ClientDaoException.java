package ru.otus.webserver.exception;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException(Exception ex) {
        super(ex);
    }
}
