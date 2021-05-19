package com.nhat.moneytracker.modules.savings;

import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;

import java.util.ArrayList;

public class SavingsFinishModule {

    public static ArrayList<TietKiem> getSavingsFinish(DBHelper dbHelper) {
        ArrayList<TietKiem> list = new ArrayList<>();
        ArrayList<TietKiem> all = dbHelper.getAll_TietKiem();
        for (int i = 0; i < all.size(); i++) {
            double total = MoneySavingsModule.getMoneySavings(dbHelper, all.get(i));
            if(total >= all.get(i).getSoTien()) {
                list.add(all.get(i));
            }
        }
        return list;
    }
}
