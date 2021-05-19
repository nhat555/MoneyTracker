package com.nhat.moneytracker.modules.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.notifications.NotifierAlarm_budget;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmbudgetModule {
    public static void handlingAlarmRepeat_Date_budget(java.sql.Date dateNew, Context activity, NganSach nganSach, DBHelper dbHelper,int code) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(dateNew);
        int year = calendarDate.get(Calendar.YEAR);
        int month = calendarDate.get(Calendar.MONTH) ;
        int dayOfMonth = calendarDate.get(Calendar.DAY_OF_MONTH);
        handlingAlarmRepeat_TimeZonebudget(year, month, dayOfMonth, activity, nganSach, dbHelper, dateNew,code);
    }
    private static void handlingAlarmRepeat_TimeZonebudget(int year, int month, int dayOfMonth, Context activity,
                                                           NganSach nganSach, DBHelper dbHelper, java.sql.Date dateNew,int code) {
        String TITLE=" ",MESSAGE=" ",message=" ";
        final String DETAILS = activity.getResources().getString(R.string.alarm_detail);
        if(code==0){
              TITLE = activity.getResources().getString(R.string.repeat_title_budget);
              MESSAGE = activity.getResources().getString(R.string.repeat_message_budget);
            message = MESSAGE + ". " + DETAILS;
        }else if(code==1){
            TITLE = activity.getResources().getString(R.string.repeat_budget);
            MESSAGE = activity.getResources().getString(R.string.repeat_mess_budget);
            message = MESSAGE + ": $ " + FormatMoneyModule.formatAmount(nganSach.getSoTien()) + ". " + DETAILS;
        }

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);

        Log.d("time", "handlingAlarmRepeat_TimeZonebudget: "+year+month+dayOfMonth);
        Date remind = new Date(newDate.getTime().toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remind);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(activity, NotifierAlarm_budget.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id",nganSach.getMaNganSach());
        intent.putExtra("title", TITLE);
        intent.putExtra("message", message);
        intent.putExtra("dateNew", dateNew.toString());

        PendingIntent intent1 = PendingIntent.getBroadcast(activity, 2222,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
        }
    }
}
