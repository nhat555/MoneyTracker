package com.nhat.moneytracker.modules.events;

import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.helper.DBHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class EventFinishModule {

    public static ArrayList<SuKien> getEventFinish(DBHelper dbHelper) {
        java.util.Date uNow = new java.util.Date(Calendar.getInstance().getTime().getTime());
        Date sNow = new Date(uNow.getTime());
        ArrayList<SuKien> list = new ArrayList<>();
        ArrayList<SuKien> all = dbHelper.getAll_SuKien();
        for (int i = 0; i < all.size(); i++) {
            Date date = all.get(i).getNgayKetThuc();
            if(sNow.compareTo(date) > 0) {
                list.add(all.get(i));
            }
        }
        return list;
    }
}
