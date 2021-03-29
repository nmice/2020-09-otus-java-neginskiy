package ru.otus.customorm.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.customorm.exception.JdbcMapperException;
import ru.otus.customorm.jdbc.DbExecutor;
import ru.otus.customorm.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> executor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    public JdbcMapperImpl(SessionManagerJdbc sessionManager,
                          DbExecutor<T> executor,
                          EntitySQLMetaData entitySQLMetaData,
                          EntityClassMetaData<T> entityClassMetaData) {
        this.sessionManager = sessionManager;
        this.executor = executor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public void insert(T objectData) {
        logger.debug("insert --> objectData: {}", objectData);
        try {
            Object id = getValue(entityClassMetaData.getIdField(), objectData);
            if (id instanceof Number && (long) id == 0L) {
                insertAutoincrement(objectData);
                return;
            }
            String query = entitySQLMetaData.getInsertSql();
            logger.debug("insert - query: {}", query);
            List<Object> params = entityClassMetaData.getAllFields().stream()
                    .map(field -> getValue(field, objectData))
                    .collect(Collectors.toList());
            logger.debug("insert - params: {}", params);
            executor.executeInsert(getConnection(), query, params);
        } catch (Exception e) {
            logger.debug("insert threw an exception  ");
            throw new JdbcMapperException(e);
        }
        logger.debug("insert <-- ");
    }

    @Override
    public void update(T objectData) {
        logger.debug("update --> objectData: {}", objectData);
        try {
            String query = entitySQLMetaData.getUpdateSql();
            logger.debug("update - query: {}", query);
            List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                    .map(field -> getValue(field, objectData))
                    .collect(Collectors.toList());
            params.add(getValue(entityClassMetaData.getIdField(), objectData));
            logger.debug("update - params: {}", params);
            executor.executeInsert(getConnection(), query, params);
        } catch (Exception e) {
            logger.debug("update threw an exception  ");
            throw new JdbcMapperException(e);
        }
        logger.debug("update <-- ");
    }

    @Override
    public void insertOrUpdate(T objectData) {
        logger.debug("insertOrUpdate --> objectData: {}", objectData);
        try {
            Field idField = entityClassMetaData.getIdField();
            Object id = getValue(idField, objectData);
            if (id != null && existsById(id)) {
                update(objectData);
            } else {
                insert(objectData);
            }
        } catch (Exception e) {
            logger.debug("insertOrUpdate threw an exception  ");
            throw new JdbcMapperException(e);
        }
        logger.debug("insertOrUpdate <-- ");
    }

    @Override
    public T findById(Object id, Class<T> clazz) {
        logger.debug("findById --> id: {}, clazz: {}", id, clazz);
        try {
            T entity = executor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(), id,
                    rs -> {
                        try {
                            if (rs.next()) {
                                Object[] initArgs = entityClassMetaData.getAllFields().stream()
                                        .map(field -> getQueryResultValue(field, rs))
                                        .toArray();
                                return entityClassMetaData.getConstructor().newInstance(initArgs);
                            }
                        } catch (SQLException | ReflectiveOperationException e) {
                            throw new JdbcMapperException(e);
                        }
                        return null;
                    }).orElse(null);
            logger.debug("findById <-- : entity: {}", entity);
            return entity;
        } catch (Exception e) {
            logger.debug("findById threw an exception  ");
            throw new JdbcMapperException(e);
        }
    }

    private void insertAutoincrement(T objectData) throws Exception {
        String query = entitySQLMetaData.getInsertAutoincrementSql();
        logger.debug("insertAutoincrement - query: {}", query);
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> getValue(field, objectData))
                .collect(Collectors.toList());
        logger.debug("insertAutoincrement - params: {}", params);
        String insertedId = executor.executeInsert(getConnection(), query, params);
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        idField.set(objectData, Long.valueOf(insertedId));
    }

    private boolean existsById(Object id) throws SQLException {
        return executor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(), id,
                rs -> {
                    try {
                        return rs.next() ? (T) new Object() : null;
                    } catch (SQLException e) {
                        throw new JdbcMapperException(e);
                    }
                }).isPresent();
    }

    private Object getQueryResultValue(Field field, ResultSet rs) {
        try {
            return rs.getObject(field.getName());
        } catch (SQLException e) {
            throw new JdbcMapperException(e);
        }
    }

    private Object getValue(Field field, T objectData) {
        try {
            field.setAccessible(true);
            return field.get(objectData);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}