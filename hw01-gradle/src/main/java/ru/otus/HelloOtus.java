package ru.otus;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class HelloOtus {
    public static void main(String[] args) {
        Map<Integer, String> numByNameMap = ImmutableMap.of(1,"One");
        System.out.println(numByNameMap);
    }
}
