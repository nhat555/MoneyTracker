package com.nhat.moneytracker.modules.dates;

import java.sql.Date;
import java.util.Calendar;

public class PlusMinusDayModule {

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return new Date(c.getTimeInMillis());
    }

    public static Date minusDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        return new Date(c.getTimeInMillis());
    }

    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return new Date(c.getTimeInMillis());
    }

    public static Date addYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return new Date(c.getTimeInMillis());
    }
}
