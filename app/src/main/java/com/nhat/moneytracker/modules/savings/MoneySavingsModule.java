package com.nhat.moneytracker.modules.savings;

import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;

import java.util.ArrayList;

public class MoneySavingsModule {

    public static double getMoneySavings(DBHelper dbHelper, TietKiem tietKiem) {
        double total = 0;
        ArrayList<SoGiaoDich> list = dbHelper.getBySavings_SoGiaoDich(tietKiem.getMaTietKiem());
        for (int i = 0; i < list.size(); i++) {
            String idCate = list.get(i).getMaDanhMuc();
            DanhMuc danhMuc = dbHelper.getByID_DanhMuc(idCate);
            if(danhMuc.getLoaiDanhMuc().equals("doanhthu")) {
                total += list.get(i).getSoTien();
            }
            else {
                total -= list.get(i).getSoTien();
            }
        }
        return total;
    }

    public static double getAverageMoney(DBHelper dbHelper, TietKiem tietKiem) {
        ArrayList<SoGiaoDich> list = dbHelper.getBySavings_SoGiaoDich(tietKiem.getMaTietKiem());
        int count = list.size();
        double total = getMoneySavings(dbHelper, tietKiem);
        return total / count;
    }
}
