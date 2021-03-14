package ru.otus.customjsonobjecrwriter;

import com.google.gson.Gson;
import ru.otus.customjsonobjecrwriter.mygson.MyGson;

import java.util.Collections;

/**
 * Neginskiy M.B. 12.03.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Cвой json object writer
 * Цель: Научиться сериализовывать объект в json,
 * попрактиковаться в разборе структуры объекта.
 * Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.
 * <p>
 * Gson это делает так:
 * Gson gson = new Gson();
 * AnyObject obj = new AnyObject(22, "test", 10);
 * String json = gson.toJson(obj);
 * <p>
 * Сделайте так:
 * MyGson myGson = new MyGson();
 * AnyObject obj = new AnyObject(22, "test", 10);
 * String myJson = myGson.toJson(obj);
 * <p>
 * Должно получиться:
 * AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
 * System.out.println(obj.equals(obj2));
 * <p>
 * Поддержите:
 * - примитивные типы и Wrapper-ы (Integer, Float и т.д.)
 * - строки
 * - массивы примитивных типов
 * - коллекции (interface Collection)
 * <p>
 * Не забываться, что obj может быть null :)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("***LETS GO***");

        var myGson = new MyGson();
        var obj = new AnyObject(22, "test", new double[]{12.01, 11}, Collections.singletonList(10));
        String myJson = myGson.toJson(obj);
        System.out.println("customGson: " + myJson);

        var gson = new Gson();
        String gsonJson = gson.toJson(obj);
        System.out.println("originGson: " + gsonJson);
        AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
        System.out.println(obj.equals(obj2));
    }
}
