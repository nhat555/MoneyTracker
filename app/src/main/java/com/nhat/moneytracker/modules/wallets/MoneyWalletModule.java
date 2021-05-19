package com.nhat.moneytracker.modules.wallets;

import com.nhat.moneytracker.entities.ViCaNhan;

import java.util.ArrayList;

public class MoneyWalletModule {

    public static double getMoneyWallet(ArrayList<ViCaNhan> list) {
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getSoTien();
        }
        return total;
    }
}
