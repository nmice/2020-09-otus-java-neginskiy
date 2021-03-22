package ru.otus.customorm.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(Class<?> clazz) {
        this.entityClassMetaData = new EntityClassMetaDataImpl<>(clazz);
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from " + entityClassMetaData.getName() +
                " where " + entityClassMetaData.getIdField().getName().toLowerCase() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldList = entityClassMetaData.getAllFields();
        return getInsertQuery(fieldList);
    }

    private String getInsertQuery(List<Field> fieldList) {
        List<String> fields = fieldList.stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        String queryFields = String.join(", ", fields);
        String queryValues = fields.stream()
                .map((s) -> "?")
                .collect(Collectors.joining(", "));
        return String.format("insert into %s (%s) values (%s)",
                entityClassMetaData.getName(), queryFields, queryValues);
    }

    @Override
    public String getInsertAutoincrementSql() {
        return getInsertQuery(entityClassMetaData.getFieldsWithoutId());
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String idStatement = entityClassMetaData.getIdField().getName().toLowerCase() + " = ?";
        String fieldsStatement = fieldsWithoutId.stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .map(fieldName -> fieldName + " = ?")
                .collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s",
                entityClassMetaData.getName(), fieldsStatement, idStatement);
    }
}
