package com.nhat.moneytracker.modules.checks;

import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;

public class CheckPropertyRepeatModule {

    public static ViCaNhan checkWallet(String id, DBHelper dbHelper) {
        try {
            return dbHelper.getByID_ViCaNhan(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TietKiem checkSavings(String id, DBHelper dbHelper) {
        try {
            return dbHelper.getByID_TietKiem(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SuKien checkEvent(String id, DBHelper dbHelper) {
        try {
            return dbHelper.getByID_SuKien(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
