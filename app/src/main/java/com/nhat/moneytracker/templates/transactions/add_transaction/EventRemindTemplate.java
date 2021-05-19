package com.nhat.moneytracker.templates.transactions.add_transaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.modules.dates.AddZeroDateTimeModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;

import java.util.Calendar;
import java.util.Date;

public class EventRemindTemplate {

    private static final String NOW = "Hôm nay";

    public static void displayRemind(final Context context, final EditText editText) {
        Calendar c = Calendar.getInstance();
        final int nYear = c.get(Calendar.YEAR);
        final int nMonth = c.get(Calendar.MONTH);
        final int nDay = c.get(Calendar.DAY_OF_MONTH);
        final int nHour = c.get(Calendar.HOUR_OF_DAY);
        final int nMinute = c.get(Calendar.MINUTE);
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dates = AddZeroDateTimeModule.addZero(dayOfMonth) + "/" +
                        AddZeroDateTimeModule.addZero(month + 1) + "/" + year;
                final String date = DateDisplayModule.displayDate(dates);
                TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = AddZeroDateTimeModule.addZero(hourOfDay) + ":" + AddZeroDateTimeModule.addZero(minute);
                        if(checkTime(date, hourOfDay, nHour, minute, nMinute)) {
                            editText.setText(date + " - " + time);
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                    R.drawable.ic_backspace_black_24dp, 0);
                        } else Toast.makeText(context, R.string.remind_error, Toast.LENGTH_SHORT).show();
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        callback, nHour, nMinute, false);
                timePickerDialog.show();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, callback, nYear, nMonth, nDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private static boolean checkTime(String date, int hourOfDay, int nHour, int minute, int nMinute) {
        if(date.equals(NOW)) {
            if(hourOfDay < nHour) {
                return false;
            }
            else if(hourOfDay == nHour){
                return minute > nMinute;
            }
        }
        return true;
    }
}
