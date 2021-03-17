package ru.otus.customorm.core.service;

public class DbServiceException extends RuntimeException {
    public DbServiceException(Exception e) {
        super(e);
    }
}
