package ru.otus.customorm.core.dao;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException(Exception ex) {
        super(ex);
    }
}
