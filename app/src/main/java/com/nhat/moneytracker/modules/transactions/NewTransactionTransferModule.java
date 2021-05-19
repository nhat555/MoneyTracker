package com.nhat.moneytracker.modules.transactions;

import android.widget.CheckBox;

import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.others.AccountCurrentModule;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventSaveTemplate;

public class NewTransactionTransferModule {

    public static SoGiaoDich setNewSoGiaoDich(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                               DanhMuc danhMuc, ViCaNhan viCaNhan, TietKiem tietKiem,
                                               SuKien suKien, CheckBox checkBox, String remind, String repeat) {
        SoGiaoDich soGiaoDich = new SoGiaoDich();
        soGiaoDich.setSoTien(Double.parseDouble(money));
        soGiaoDich.setGhiChu(note);
        soGiaoDich.setNgayGiaoDich(sqlDate);
        soGiaoDich.setMasv(AccountCurrentModule.getSinhVienCurrent(dbHelper).getMasv());
        soGiaoDich.setMaDanhMuc(danhMuc.getMaDanhMuc());
        soGiaoDich.setLapLai(repeat);
        EventSaveTemplate.handlingRemind(remind, soGiaoDich);
        EventSaveTemplate.handlingSetProperty(soGiaoDich, viCaNhan, tietKiem, suKien);
        EventSaveTemplate.handlingSetStatus(soGiaoDich, checkBox);
        EventSaveTemplate.handlingCheckStatus(dbHelper, soGiaoDich);
        return soGiaoDich;
    }
}
