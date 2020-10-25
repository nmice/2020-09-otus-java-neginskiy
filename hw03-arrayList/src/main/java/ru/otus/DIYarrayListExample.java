package ru.otus;

import java.util.*;
import java.util.stream.IntStream;

public class DIYarrayListExample {

    public static void main(String[] args) {
        List<String> arraylist = new ArrayList<>();
        List<String> diyArrayList = new DIYarrayList<>();
        System.out.println("0 arraylist size = " + arraylist.size());
        System.out.println("0 diyArrayList size = " + diyArrayList.size());

        addFiftyElements(arraylist);
        addFiftyElements(diyArrayList);
        System.out.println("1 arraylist size = " + arraylist.size());
        System.out.println("1 diyArrayList size = " + diyArrayList.size());

        System.out.println("2 Perform Collections.addAll");
        Collections.addAll(arraylist, "Four", "One", "Three", "Zero", "Two");
        Collections.addAll(diyArrayList, "Four", "Zero", "One", "Three", "Two");
        System.out.println("2.1 arraylist size = " + arraylist.size());
        System.out.println("2.1 diyArrayList size = " + diyArrayList.size());

        List<String> list = Arrays.asList("FIRST", "SECOND", "THIRD", "FOURTH", "FIFTH");
        List<String> destList = new ArrayList<>();
        destList.addAll(list);

        System.out.println("3 Perform Collections.copy");
        Collections.copy(arraylist, destList);
        Collections.copy(diyArrayList, destList);
        System.out.println("3.2 arraylist size = " + arraylist.size());
        System.out.println("3.2 diyArrayList size = " + diyArrayList.size());
        System.out.println("3.3 arraylist : " + arraylist);
        System.out.println("3.3 diyArrayList : " + diyArrayList);

        System.out.println("4 Perform Collections.sort");
        Collections.sort(arraylist, (o1, o2) -> o1.compareTo(o2));
        Collections.sort(diyArrayList, (o1, o2) -> o1.compareTo(o2));
        System.out.println("4.1 arraylist : " + arraylist);
        System.out.println("4.1 diyArrayList : " + diyArrayList);
    }

    private static void addFiftyElements(List<String> arraylist) {
        IntStream.range(0, 50).mapToObj(i -> "Element-" + i).forEach(arraylist::add);
    }
}
