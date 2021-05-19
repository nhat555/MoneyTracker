package com.nhat.moneytracker.modules.alarms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.notifications.NotifierAlarm_First;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmFirstModule {

    public static void handlingAlarmFirst_Date(String dateStr, Activity activity) {
        java.sql.Date date = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        handlingAlarmFirst_TimeZone(year, month, dayOfMonth, activity);
    }

    private static void handlingAlarmFirst_TimeZone(int year, int month, int dayOfMonth, Activity activity) {

        final String TITLE = activity.getResources().getString(R.string.first_title);
        final String MESSAGE = activity.getResources().getString(R.string.first_message);

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);
        Date remind = new Date(newDate.getTime().toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(activity, NotifierAlarm_First.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("title", TITLE);
        intent.putExtra("message", MESSAGE);

        PendingIntent intent1 = PendingIntent.getBroadcast(activity, 1111,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
        }
    }
}
