package com.nhat.moneytracker.modules.transactions;

import android.content.Context;

import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.alarms.AlarmRepeatModule;
import com.nhat.moneytracker.modules.dates.PlusMinusDayModule;

import java.sql.Date;

public class RepeatTransactionModule {

    public static void handlingRepeatDay(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        handlingSaveRepeat(soGiaoDich, dbHelper, 1, activity);
    }

    public static void handlingRepeatWeek(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        handlingSaveRepeat(soGiaoDich, dbHelper, 7, activity);
    }

    public static void handlingRepeatMonth(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        handlingSaveRepeat(soGiaoDich, dbHelper, 30, activity);
    }

    public static void handlingRepeatQuarter(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        handlingSaveRepeat(soGiaoDich, dbHelper, 90, activity);
    }

    public static void handlingRepeatYear(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        handlingSaveRepeat(soGiaoDich, dbHelper, 365, activity);
    }

    private static void handlingSaveRepeat(SoGiaoDich soGiaoDich, DBHelper dbHelper, int day, Context activity) {
        Date dateOld = soGiaoDich.getNgayGiaoDich();
        Date dateNew = addTimeRepeat(dateOld, day);
        AlarmRepeatModule.handlingAlarmRepeat_Date(dateNew, activity, soGiaoDich, dbHelper);
    }

    private static Date addTimeRepeat(Date dateOld, int day) {
        switch (day) {
            case 1:
                return PlusMinusDayModule.addDays(dateOld, 1);
            case 7:
                return PlusMinusDayModule.addDays(dateOld, 7);
            case 30:
                return PlusMinusDayModule.addMonths(dateOld, 1);
            case 90:
                return PlusMinusDayModule.addMonths(dateOld, 3);
            default:
                return PlusMinusDayModule.addYears(dateOld, 1);
        }
    }
}
