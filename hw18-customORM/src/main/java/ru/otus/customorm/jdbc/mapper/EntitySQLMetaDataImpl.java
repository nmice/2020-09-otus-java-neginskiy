package ru.otus.customorm.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String tableName;
    private final List<String> allFieldsNames;
    private final List<String> fieldsWoIdNames;
    private final String idFieldName;

    private EntitySQLMetaDataImpl(String tableName, List<String> allFieldsNames, List<String> fieldsWoIdNames,
                                  String idFieldName) {
        this.tableName = tableName;
        this.allFieldsNames = allFieldsNames;
        this.fieldsWoIdNames = fieldsWoIdNames;
        this.idFieldName = idFieldName;
    }

    public static EntitySQLMetaData of(EntityClassMetaData<?> entityClassMetaData) {
        String tableName = entityClassMetaData.getName();
        List<String> fieldNames = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        List<String> fieldsWoIdNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        String idFieldName = entityClassMetaData.getIdField().getName().toLowerCase();
        return new EntitySQLMetaDataImpl(tableName, fieldNames, fieldsWoIdNames, idFieldName);
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ? ", tableName,
                idFieldName);
    }

    @Override
    public String getInsertSql() {
        return getInsertQuery(allFieldsNames);
    }

    private String getInsertQuery(List<String> fieldsNames) {
        String queryFields = String.join(", ", fieldsNames);
        String queryValues = fieldsNames.stream()
                .map((s) -> "?")
                .collect(Collectors.joining(", "));
        return String.format("insert into %s (%s) values (%s)",
                tableName, queryFields, queryValues);
    }

    @Override
    public String getInsertAutoincrementSql() {
        return getInsertQuery(fieldsWoIdNames);
    }

    @Override
    public String getUpdateSql() {
        String fieldsStatement = fieldsWoIdNames.stream()
                .map(fieldName -> fieldName + " = ?")
                .collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s = ?", tableName, fieldsStatement, idFieldName);
    }
}
