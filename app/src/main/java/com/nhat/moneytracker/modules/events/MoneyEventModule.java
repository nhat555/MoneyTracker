package com.nhat.moneytracker.modules.events;

import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.helper.DBHelper;

import java.util.ArrayList;

public class MoneyEventModule {

    public static double getMoneyEvent(DBHelper dbHelper, SuKien suKien) {
        double total = 0;
        ArrayList<SoGiaoDich> list = dbHelper.getByEvent_SoGiaoDich(suKien.getMaSuKien());
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
}
