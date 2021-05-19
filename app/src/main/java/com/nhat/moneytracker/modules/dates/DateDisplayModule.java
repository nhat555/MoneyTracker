package com.nhat.moneytracker.modules.dates;

import android.annotation.SuppressLint;
import android.util.Log;

import com.nhat.moneytracker.modules.formats.DateFormatModule;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateDisplayModule {

    private static final String NOW = "Hôm nay";
    private static final String YESTERDAY = "Hôm qua";
    private static final String TOMORROW = "Ngày mai";

    public static String displayDate(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = DateFormatModule.getDateSQL(dateStr);
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        Date yesterday = PlusMinusDayModule.minusDays(now, 1);
        Date tomorrow = PlusMinusDayModule.addDays(now, 1);
        if(date.toString().equals(now.toString())) {
            Log.d(null, "NOW");
            return NOW;
        }
        else if(date.toString().equals(yesterday.toString())) {
            Log.d(null, "YESTERDAY");
            return YESTERDAY;
        }
        else if(date.toString().equals(tomorrow.toString())) {
            Log.d(null, "TOMORROW");
            return TOMORROW;
        }
        return dateStr;
    }

    public static String getDateByDisplay(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        Date yesterday = PlusMinusDayModule.minusDays(now, 1);
        Date tomorrow = PlusMinusDayModule.addDays(now, 1);
        switch (dateStr) {
            case NOW:
                return NOW;
            case YESTERDAY:
                return formatter.format(yesterday);
            case TOMORROW:
                return formatter.format(tomorrow);
            default:
                return dateStr;
        }
    }

    public static String getDateByStringRemind(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        Date yesterday = PlusMinusDayModule.minusDays(now, 1);
        Date tomorrow = PlusMinusDayModule.addDays(now, 1);
        switch (dateStr) {
            case NOW:
                return formatter.format(now);
            case YESTERDAY:
                return formatter.format(yesterday);
            case TOMORROW:
                return formatter.format(tomorrow);
            default:
                return dateStr;
        }
    }
}
