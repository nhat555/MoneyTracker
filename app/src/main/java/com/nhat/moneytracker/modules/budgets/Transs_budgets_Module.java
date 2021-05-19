package com.nhat.moneytracker.modules.budgets;

import com.nhat.moneytracker.entities.ChiTietNganSach;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;

import java.util.ArrayList;

public class Transs_budgets_Module {

    public static ArrayList<SoGiaoDich> gettranssBudget(DBHelper dbHelper, NganSach nganSach) {
      ArrayList<SoGiaoDich>listtrans=new ArrayList<>();
        ArrayList<ChiTietNganSach> list = dbHelper.getByIDBudget_ChiTietNganSach(nganSach.getMaNganSach());
        for (int i = 0; i < list.size(); i++) {
            SoGiaoDich sogiaodich = dbHelper.getByID_SoGiaoDich(list.get(i).getMaGiaoDich());
          listtrans.add(sogiaodich);
        }
        return listtrans;
    }
}