package com.nhat.moneytracker.modules.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.notifications.NotifierAlarm_Repeat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmRepeatModule {

    public static void handlingAlarmRepeat_Date(java.sql.Date dateNew, Context activity, SoGiaoDich soGiaoDich, DBHelper dbHelper) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(dateNew);
        int year = calendarDate.get(Calendar.YEAR);
        int month = calendarDate.get(Calendar.MONTH);
        int dayOfMonth = calendarDate.get(Calendar.DAY_OF_MONTH);
        handlingAlarmRepeat_TimeZone(year, month, dayOfMonth, activity, soGiaoDich, dbHelper, dateNew);
    }

    private static void handlingAlarmRepeat_TimeZone(int year, int month, int dayOfMonth, Context activity,
                                                     SoGiaoDich soGiaoDich, DBHelper dbHelper, java.sql.Date dateNew) {

        final String TITLE = activity.getResources().getString(R.string.repeat_title);
        final String MESSAGE = activity.getResources().getString(R.string.repeat_message);
        final String DETAILS = activity.getResources().getString(R.string.alarm_detail);

        final String message = MESSAGE + " " + dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc()).getTenDanhMuc()
                + ": $ " + FormatMoneyModule.formatAmount(soGiaoDich.getSoTien()) + ". " + DETAILS;

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);
        Date remind = new Date(newDate.getTime().toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(activity, NotifierAlarm_Repeat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id", soGiaoDich.getMaGiaoDich());
        intent.putExtra("title", TITLE);
        intent.putExtra("message", message);
        intent.putExtra("dateNew", dateNew.toString());

        PendingIntent intent1 = PendingIntent.getBroadcast(activity, 6666,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
        }
    }
}
