package ru.otus.customorm.service;

import java.util.Optional;

/**
 * Сервис для работы с базой данных.
 *
 * @param <T> тип объекта (entity) в базе
 * @param <K> тип первичного ключа (id)
 */
public interface DBService<T, K> {

    K saveEntity(T entity);

    Optional<T> getEntityById(K id);
}
