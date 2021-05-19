package com.nhat.moneytracker.modules.transactions;

import com.nhat.moneytracker.entities.SoGiaoDich;

import java.util.ArrayList;

public class TransactionFastModule {

    public static ArrayList<SoGiaoDich> getSoGiaoDichesStatus(ArrayList<SoGiaoDich> list)  {
        ArrayList<SoGiaoDich> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getStatus() == 1) {
                arrayList.add(list.get(i));
            }
        }
        return arrayList;
    }
}
