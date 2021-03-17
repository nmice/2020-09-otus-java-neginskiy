package ru.otus.customjsonobjecrwriter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnyObject {

    private final int field1;
    private final String field2;
    private final double[] field3;
    private final List<Integer> field4;

    public AnyObject(int field1, String field2, double[] field3, List<Integer> field4) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnyObject anyObject = (AnyObject) o;
        return field1 == anyObject.field1 &&
                Objects.equals(field2, anyObject.field2) &&
                Arrays.equals(field3, anyObject.field3) &&
                Objects.equals(field4, anyObject.field4);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(field1, field2, field4);
        result = 31 * result + Arrays.hashCode(field3);
        return result;
    }
}
