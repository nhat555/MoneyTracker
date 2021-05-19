package com.nhat.moneytracker.modules.budgets;

import com.nhat.moneytracker.entities.ChiTietNganSach;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;

import java.util.ArrayList;

public class MoneyBudgetModule {

    public static double getMoneyUseBudget(DBHelper dbHelper, NganSach nganSach) {
        double total = 0;
        ArrayList<ChiTietNganSach> list = dbHelper.getByIDBudget_ChiTietNganSach(nganSach.getMaNganSach());
        for (int i = 0; i < list.size(); i++) {
            SoGiaoDich soGiaoDich = dbHelper.getByID_SoGiaoDich(list.get(i).getMaGiaoDich());
            total -= soGiaoDich.getSoTien();
        }
        return Math.abs(total);
    }

    public static double getMoneyRestBudget(DBHelper dbHelper, NganSach nganSach) {
        double total = nganSach.getSoTien();
        double moneyUse = getMoneyUseBudget(dbHelper, nganSach);
        double moneyRest = total - moneyUse;
        return Math.abs(moneyRest);
    }

    public static double getTotalMoneyExpenses(DBHelper dbHelper, String idBudgets) {
        final String KHOANCHI = "khoanchi";
        double total = 0;
        ArrayList<ChiTietNganSach> listDetail = dbHelper.getByIDBudget_ChiTietNganSach(idBudgets);
        for (int i = 0; i < listDetail.size(); i++) {
            SoGiaoDich soGiaoDich = dbHelper.getByID_SoGiaoDich(listDetail.get(i).getMaGiaoDich());
            DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc());
            if(danhMuc.getLoaiDanhMuc().equals(KHOANCHI)) {
                total += soGiaoDich.getSoTien();
            }
        }
        return total;
    }
}
