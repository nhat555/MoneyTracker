package com.nhat.moneytracker.modules.others;

import com.nhat.moneytracker.entities.SinhVien;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;

public class AccountCurrentModule {

    public static SinhVien getSinhVienCurrent(DBHelper dbHelper) {
        TaiKhoan taiKhoan = dbHelper.getByCode_TaiKhoan(1);
        return dbHelper.getByID_SinhVien(taiKhoan.getEmail());
    }
}
