package ru.otus.customorm.jdbc.mapper;

import ru.otus.customorm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    public static final Predicate<Field> ID_FILTER = (field -> field.isAnnotationPresent(Id.class));

    private final String className;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;
    private final Field idField;
    private final Constructor<T> constructor;

    @SuppressWarnings("unchecked")
    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.className = clazz.getSimpleName().toLowerCase();
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = allFields.stream().filter(ID_FILTER).findAny().orElse(null);
        this.fieldsWithoutId = allFields.stream().filter(ID_FILTER.negate()).collect(Collectors.toList());
        this.constructor = (Constructor<T>) clazz.getConstructors()[0];
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
