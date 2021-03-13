package ru.otus.customjsonobjecrwriter;

import java.util.List;

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
}
