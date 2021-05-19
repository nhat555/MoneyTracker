package com.nhat.moneytracker.modules.alarms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.notifications.NotifierAlarm_Remind;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmRemindModule {

    public static void handlingAlarmRemind_Date(String dateStr, String timeStr, Activity activity, SoGiaoDich soGiaoDich, DBHelper dbHelper) {
        java.sql.Date date = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        handlingAlarmRemind_Time(timeStr, year, month, dayOfMonth, activity, soGiaoDich, dbHelper);
    }

    private static void handlingAlarmRemind_Time(String timeStr, int year, int month, int dayOfMonth, Activity activity,
                                                 SoGiaoDich soGiaoDich, DBHelper dbHelper) {
        Time time = Time.valueOf(timeStr + ":00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        handlingAlarmRemind_TimeZone(year, month, dayOfMonth, hourOfDay, minute, activity, soGiaoDich, dbHelper);
    }

    private static void handlingAlarmRemind_TimeZone(int year, int month, int dayOfMonth, int hourOfDay, int minute,
                                                     Activity activity, SoGiaoDich soGiaoDich, DBHelper dbHelper) {

        final String TITLE = activity.getResources().getString(R.string.remind_title);
        final String MESSAGE = activity.getResources().getString(R.string.remind_message);
        final String DETAILS = activity.getResources().getString(R.string.alarm_detail);

        final String message = MESSAGE + " " + dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc()).getTenDanhMuc()
                + ": $ " + FormatMoneyModule.formatAmount(soGiaoDich.getSoTien()) + ". " + DETAILS;

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth, hourOfDay, minute,0);
        Date remind = new Date(newDate.getTime().toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(activity, NotifierAlarm_Remind.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id", soGiaoDich.getMaGiaoDich());
        intent.putExtra("title", TITLE);
        intent.putExtra("message", message);

        PendingIntent intent1 = PendingIntent.getBroadcast(activity, 5555,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
        }
    }
}
