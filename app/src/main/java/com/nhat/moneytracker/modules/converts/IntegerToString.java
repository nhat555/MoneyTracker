package com.nhat.moneytracker.modules.converts;

import java.util.ArrayList;

public class IntegerToString {

    public static ArrayList<String> convertIntegerToStringArrayList(ArrayList<Integer> listInteger) {
        ArrayList<String> strings = new ArrayList<>();
        for (Integer i : listInteger) {
            strings.add(String.valueOf(i));
        }
        return strings;
    }
}
