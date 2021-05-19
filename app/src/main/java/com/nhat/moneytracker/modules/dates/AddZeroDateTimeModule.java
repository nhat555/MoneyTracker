package com.nhat.moneytracker.modules.dates;

public class AddZeroDateTimeModule {

    public static String addZero(int index) {
        String result = String.valueOf(index);
        if(index < 10) {
            result = "0" + index;
        }
        return result;
    }
}
