package com.nhat.moneytracker.modules.others;

import com.nhat.moneytracker.modules.dates.AddZeroDateTimeModule;

import java.util.ArrayList;

public class GetYearMonthChooseModule {

    public static ArrayList<String> getArrayYear() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 2020; i < 2101; i++) {
            list.add("               " + i);
        }
        return list;
    }

    public static ArrayList<String> getArrayMonth() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            list.add("                  " + AddZeroDateTimeModule.addZero(i));
        }
        return list;
    }
}
