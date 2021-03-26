package ru.otus.customorm.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String insertAutoincrementSql;
    private final String updateSql;

    private EntitySQLMetaDataImpl(String selectAllSql, String selectByIdSql, String insertSql,
                                  String insertAutoincrementSql, String updateSql) {
        this.selectAllSql = selectAllSql;
        this.selectByIdSql = selectByIdSql;
        this.insertSql = insertSql;
        this.insertAutoincrementSql = insertAutoincrementSql;
        this.updateSql = updateSql;
    }

    public static EntitySQLMetaData of(EntityClassMetaData<?> entityClassMetaData) {
        String tableName = entityClassMetaData.getName();
        String selectAllSql = String.format("select * from %s", tableName);

        String idFieldName = entityClassMetaData.getIdField().getName().toLowerCase();
        String selectByIdSql = String.format("select * from %s where %s = ? ", tableName, idFieldName);

        List<String> allFieldsNames = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        String insertSql = getInsertQuery(allFieldsNames, tableName);

        List<String> fieldsWoIdNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        String insertAutoincrementSql = getInsertQuery(fieldsWoIdNames, tableName);

        String updateSql = getUpdateQuery(fieldsWoIdNames, tableName, idFieldName);

        return new EntitySQLMetaDataImpl(selectAllSql, selectByIdSql, insertSql, insertAutoincrementSql, updateSql);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getInsertAutoincrementSql() {
        return insertAutoincrementSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private static String getInsertQuery(List<String> fieldsNames, String tableName) {
        String queryFields = String.join(", ", fieldsNames);
        String queryValues = Stream.generate(() -> "?")
                .limit(fieldsNames.size())
                .collect(Collectors.joining(", "));
        return String.format("insert into %s (%s) values (%s)",
                tableName, queryFields, queryValues);
    }

    private static String getUpdateQuery(List<String> fieldsWoIdNames, String tableName, String idFieldName) {
        String fieldsStatement = fieldsWoIdNames.stream()
                .map(fieldName -> fieldName + " = ?")
                .collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s = ?", tableName, fieldsStatement, idFieldName);
    }
}
