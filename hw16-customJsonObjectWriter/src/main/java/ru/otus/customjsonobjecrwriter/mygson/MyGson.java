package ru.otus.customjsonobjecrwriter.mygson;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyGson {

    // 1) Проанализировать объект, который передали
    // 2) На основе анализа сформировать строку
    public String toJson(Object o) {
        var result = new StringBuilder();
        if (o == null) {
            return result.append("null").toString();
        }
        Class<?> objClass = o.getClass();
        if (ClassUtils.isPrimitiveOrWrapper(objClass)) {
            result.append(o);
        } else if (String.class.equals(objClass)) {
            result.append("\"")
                    .append(o)
                    .append("\"");
        } else if (objClass.isArray()) {
            result.append("[");
            Object[] array = (Object[]) o;
            Collection<String> strCol = Arrays.stream(array).map(this::toJson).collect(Collectors.toList());
            result.append(String.join(",", strCol));
            result.append("]");
        } else if (Collection.class.isAssignableFrom(objClass)) {
            result.append("[");
            Collection<Object> col = (Collection<Object>) o;
            Collection<String> strCol = col.stream().map(this::toJson).collect(Collectors.toList());
            result.append(String.join(",", strCol));
            result.append("]");
        } else {
            result.append("{");
            Field[] fields = objClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Object fieldValue = null;
                try {
                    fields[i].setAccessible(true);
                    fieldValue = fields[i].get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fieldValue != null) {
                    result.append("\"")
                            .append(fields[i].getName())
                            .append("\"")
                            .append(":")
                            .append(toJson(fieldValue));
                    if (i < fields.length - 1) {
                        result.append(",");
                    }
                }
            }
            result.append("}");
        }
        return result.toString();
    }
}
