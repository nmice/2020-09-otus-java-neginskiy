package ru.otus.test;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass {

    @Before
    public void setUp1() {
        System.out.println("SetUp1");
    }

    @Before
    public void setUp2() {
        System.out.println("SetUp2");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
    }

    @Test
    public void test2() {
        System.out.println("Test2");
        throw new IllegalArgumentException();
    }

    @After
    public void tearDown1() {
        System.out.println("TearDown1");
    }

    @After
    public void tearDown2() {
        System.out.println("TearDown2");
    }
}
