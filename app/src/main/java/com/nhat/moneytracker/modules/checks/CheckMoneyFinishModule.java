package com.nhat.moneytracker.modules.checks;

import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.savings.MoneySavingsModule;

public class CheckMoneyFinishModule {

    public static boolean isFinish(DBHelper dbHelper, TietKiem tietKiem) {
        double total = MoneySavingsModule.getMoneySavings(dbHelper, tietKiem);
        return total >= tietKiem.getSoTien();
    }
}
