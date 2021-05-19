package com.nhat.moneytracker.modules.budgets;

import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.helper.DBHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BudgetsApplyModule {

    public static ArrayList<NganSach> getBudgetsApply(DBHelper dbHelper) {
        java.util.Date uNow = new java.util.Date(Calendar.getInstance().getTime().getTime());
        Date sNow = new Date(uNow.getTime());
        ArrayList<NganSach> list = new ArrayList<>();
        ArrayList<NganSach> all = dbHelper.getAll_NganSach();
        for (int i = 0; i < all.size(); i++) {
            Date date = all.get(i).getNgayKetThuc();
            if(sNow.compareTo(date) < 0) {
                list.add(all.get(i));
            }
        }
        return list;
    }
}
