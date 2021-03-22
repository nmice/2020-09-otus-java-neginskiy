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

    private final Class<T> clazz;
    private final List<Field> allFields;
    private final Field idField;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = allFields.stream().filter(ID_FILTER).findAny().orElse(null);
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Constructor<T> getConstructor() {
            return (Constructor<T>) clazz.getConstructors()[0];
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
        return allFields.stream()
                .filter(ID_FILTER.negate())
                .collect(Collectors.toList());
    }
}
