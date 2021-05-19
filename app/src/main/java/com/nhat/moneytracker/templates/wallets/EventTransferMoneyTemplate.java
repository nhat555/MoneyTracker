package com.nhat.moneytracker.templates.wallets;

import android.app.Activity;

import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.others.AccountCurrentModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;
import com.nhat.moneytracker.sessions.Session;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventSaveTemplate;

public class EventTransferMoneyTemplate {

    public static void handlingSaveTrans(final String money, final String note, final java.sql.Date sqlDate, DBHelper dbHelper,
                                         DanhMuc danhMuc, ViCaNhan viCaNhan, ViCaNhan viCaNhanReceive, Activity activity, Session session,
                                         SoGiaoDich soGiaoDichNew) {
        SoGiaoDich soGiaoDich = new SoGiaoDich();
        soGiaoDich.setMaGiaoDich(RandomIDModule.getTransID(dbHelper));
        soGiaoDich.setSoTien(Double.parseDouble(money));
        soGiaoDich.setGhiChu(note);
        soGiaoDich.setNgayGiaoDich(sqlDate);
        soGiaoDich.setMasv(AccountCurrentModule.getSinhVienCurrent(dbHelper).getMasv());
        soGiaoDich.setMaDanhMuc(danhMuc.getMaDanhMuc());
        soGiaoDich.setMaVi(viCaNhan.getMaVi());
        soGiaoDich.setMaSuKien("null");
        soGiaoDich.setMaTietKiem("null");
        soGiaoDich.setStatus(0);
        soGiaoDich.setNhacNho("");
        soGiaoDich.setLapLai("");
        dbHelper.insert_SoGiaoDich(soGiaoDich);
        session.clearWallet();
        session.clearCate();
        handlingUpdateWallet(money, viCaNhan, viCaNhanReceive, dbHelper);
        EventSaveTemplate.handlingSaveTrans_Transfer(dbHelper, activity, soGiaoDichNew);
    }

    private static void handlingUpdateWallet(String money, ViCaNhan viCaNhan, ViCaNhan viCaNhanReceive, DBHelper dbHelper) {
        viCaNhan.rutTien(money);
        dbHelper.update_ViCaNhan(viCaNhan);
        viCaNhanReceive.napTien(money);
        dbHelper.update_ViCaNhan(viCaNhanReceive);
    }
}
