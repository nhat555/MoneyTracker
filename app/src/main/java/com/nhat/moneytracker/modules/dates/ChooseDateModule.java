package com.nhat.moneytracker.modules.dates;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class ChooseDateModule {

    public static void chooseDate(Context context, final EditText editText, boolean isMinDate) {
        Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR);
        int nMonth = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = AddZeroDateTimeModule.addZero(dayOfMonth) + "/" +
                        AddZeroDateTimeModule.addZero(month + 1) + "/" + year;
                editText.setText(DateDisplayModule.displayDate(date));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, callback, nYear, nMonth, nDay);
        if(isMinDate) {
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        }
        else {
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        }
        datePickerDialog.show();
    }
}
