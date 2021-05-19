package com.nhat.moneytracker.templates.transactions.add_transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.chooses.ChooseWalletActivity;
import com.nhat.moneytracker.entities.ChiTietNganSach;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.alarms.AlarmRemindModule;
import com.nhat.moneytracker.modules.alarms.AlarmbudgetModule;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.checks.CheckPropertyRepeatModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.others.AccountCurrentModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;
import com.nhat.moneytracker.modules.savings.MoneySavingsModule;
import com.nhat.moneytracker.modules.transactions.DialogTransfersModule;
import com.nhat.moneytracker.modules.transactions.NewTransactionTransferModule;
import com.nhat.moneytracker.modules.transactions.RepeatTransactionModule;
import com.nhat.moneytracker.sessions.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventSaveTemplate {
    private static final String EVERY_DAY = "(Hằng ngày)";
    private static final String EVERY_WEEK = "(Hằng tuần)";
    private static final String EVERY_MONTH = "(Hằng tháng)";
    private static final String EVERY_QUARTER = "(Hằng quý)";

    public static void handlingSaveTrans_Transfer(DBHelper dbHelper, Activity activity, SoGiaoDich soGiaoDich) {
        DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc());
        ViCaNhan viCaNhan = CheckPropertyRepeatModule.checkWallet(soGiaoDich.getMaVi(), dbHelper);
        TietKiem tietKiem = CheckPropertyRepeatModule.checkSavings(soGiaoDich.getMaTietKiem(), dbHelper);
        soGiaoDich.setMaGiaoDich(RandomIDModule.getTransID(dbHelper));
        dbHelper.insert_SoGiaoDich(soGiaoDich);
        handlinAlamrRemind(soGiaoDich, activity, dbHelper);
        handlingAlarmRepeat(soGiaoDich, activity, dbHelper);
        handlingUpdateWallet(String.valueOf(soGiaoDich.getSoTien()), dbHelper, viCaNhan, danhMuc, tietKiem);
        handlingCheckBudget(soGiaoDich, dbHelper,activity);
        activity.onBackPressed();
        Toast.makeText(activity, R.string.success_add_trans, Toast.LENGTH_SHORT).show();
    }

    public static void handlingSaveTransFast(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                             DanhMuc danhMuc, Activity activity, ViCaNhan viCaNhan, TietKiem tietKiem, SuKien suKien,
                                             String remind, String repeat) {
        SoGiaoDich soGiaoDich = new SoGiaoDich();
        soGiaoDich.setMaGiaoDich(RandomIDModule.getTransID(dbHelper));
        soGiaoDich.setSoTien(Double.parseDouble(money));
        soGiaoDich.setGhiChu(note);
        soGiaoDich.setNgayGiaoDich(sqlDate);
        soGiaoDich.setMasv(AccountCurrentModule.getSinhVienCurrent(dbHelper).getMasv());
        soGiaoDich.setMaDanhMuc(danhMuc.getMaDanhMuc());
        soGiaoDich.setLapLai(repeat);
        soGiaoDich.setNhacNho(remind);
        soGiaoDich.setStatus(0);
        handlingSetProperty(soGiaoDich, viCaNhan, tietKiem, suKien);
        handlingCheckStatus(dbHelper, soGiaoDich);
        dbHelper.insert_SoGiaoDich(soGiaoDich);
        handlingUpdateWallet(money, dbHelper, viCaNhan, danhMuc, tietKiem);
        handlingCheckBudget(soGiaoDich, dbHelper,activity);
        activity.onBackPressed();
        Toast.makeText(activity, R.string.success_add_trans, Toast.LENGTH_SHORT).show();
    }

    public static void handlingSaveTransRepeat(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                               DanhMuc danhMuc, ViCaNhan viCaNhan, TietKiem tietKiem, SuKien suKien,
                                               String remind, String repeat, Context activity, String id, SoGiaoDich soGiaoDich) {
        soGiaoDich.setMaGiaoDich(id);
        soGiaoDich.setSoTien(Double.parseDouble(money));
        soGiaoDich.setGhiChu(note);
        soGiaoDich.setNgayGiaoDich(sqlDate);
        soGiaoDich.setMasv(AccountCurrentModule.getSinhVienCurrent(dbHelper).getMasv());
        soGiaoDich.setMaDanhMuc(danhMuc.getMaDanhMuc());
        soGiaoDich.setLapLai(repeat);
        soGiaoDich.setNhacNho(remind);
        soGiaoDich.setStatus(0);
        handlingSetProperty(soGiaoDich, viCaNhan, tietKiem, suKien);
        handlingCheckStatus(dbHelper, soGiaoDich);
        dbHelper.insert_SoGiaoDich(soGiaoDich);
        handlingAlarmRepeat(soGiaoDich, activity, dbHelper);
        handlingUpdateWallet(money, dbHelper, viCaNhan, danhMuc, tietKiem);
        handlingCheckBudget(soGiaoDich, dbHelper,activity);
    }

    public static void handlingInput(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                     DanhMuc danhMuc, Activity activity, ViCaNhan viCaNhan, TietKiem tietKiem, SuKien suKien,
                                     CheckBox checkBox, Session session, String remind, String repeat) {
        if(CheckEmptyModule.isEmpty(money, money, money)) {
            if(money.length() > 3 && Double.parseDouble(money) > 0 && money.substring(money.length() - 3).equals("000")) {
                if(viCaNhan != null) {
                    if(danhMuc.getLoaiDanhMuc().equals("khoanchi")) {
                        if(viCaNhan.getSoTien() >= Double.parseDouble(money)) {
                            handlingSaveTrans(dbHelper, money, note, sqlDate, danhMuc, activity,
                                    viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
                        }
                        else {
                            handlingDialogMoneyIsNotEnough(dbHelper, money, note, sqlDate, danhMuc, activity,
                                    viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
                        }
                    } else handlingSaveTrans(dbHelper, money, note, sqlDate, danhMuc, activity,
                            viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
                }
                else {
                    double total = MoneySavingsModule.getMoneySavings(dbHelper, tietKiem);
                    if(danhMuc.getLoaiDanhMuc().equals("khoanchi")) {
                        if(total >= Double.parseDouble(money)) {
                            handlingSaveTrans(dbHelper, money, note, sqlDate, danhMuc, activity,
                                    viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
                        } else Toast.makeText(activity, R.string.savings_money, Toast.LENGTH_SHORT).show();
                    } else handlingSaveTrans(dbHelper, money, note, sqlDate, danhMuc, activity,
                            viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
                }
            } else Toast.makeText(activity, R.string.invalid_money, Toast.LENGTH_SHORT).show();
        } else Toast.makeText(activity, R.string.empty_money, Toast.LENGTH_SHORT).show();
    }

    private static void handlingDialogMoneyIsNotEnough(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                                       DanhMuc danhMuc, final Activity activity, ViCaNhan viCaNhan, TietKiem tietKiem,
                                                       SuKien suKien, CheckBox checkBox, String remind, String repeat) {
        final String WALLET_FROM = "from";
        SoGiaoDich soGiaoDich = NewTransactionTransferModule.setNewSoGiaoDich(dbHelper, money, note, sqlDate,
                danhMuc, viCaNhan, tietKiem, suKien, checkBox, remind, repeat);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, ChooseWalletActivity.class);
                intent.putExtra("name", WALLET_FROM);
                activity.startActivity(intent);
            }
        };
        DialogTransfersModule.displayDialogTransfers(activity, runnable, soGiaoDich);
    }

    private static void handlingSaveTrans(DBHelper dbHelper, String money, String note, java.sql.Date sqlDate,
                                          DanhMuc danhMuc, Activity activity, ViCaNhan viCaNhan, TietKiem tietKiem, SuKien suKien,
                                          CheckBox checkBox, String remind, String repeat) {
        SoGiaoDich soGiaoDich = new SoGiaoDich();
        soGiaoDich.setMaGiaoDich(RandomIDModule.getTransID(dbHelper));
        soGiaoDich.setSoTien(Double.parseDouble(money));
        soGiaoDich.setGhiChu(note);
        soGiaoDich.setNgayGiaoDich(sqlDate);
        soGiaoDich.setMasv(AccountCurrentModule.getSinhVienCurrent(dbHelper).getMasv());
        soGiaoDich.setMaDanhMuc(danhMuc.getMaDanhMuc());
        soGiaoDich.setLapLai(repeat);
        handlingRemind(remind, soGiaoDich);
        handlingSetProperty(soGiaoDich, viCaNhan, tietKiem, suKien);
        handlingSetStatus(soGiaoDich, checkBox);
        handlingCheckStatus(dbHelper, soGiaoDich);
        dbHelper.insert_SoGiaoDich(soGiaoDich);
        handlinAlamrRemind(soGiaoDich, activity, dbHelper);
        handlingAlarmRepeat(soGiaoDich, activity, dbHelper);
        handlingUpdateWallet(money, dbHelper, viCaNhan, danhMuc, tietKiem);
        handlingCheckBudget(soGiaoDich, dbHelper, activity);
        activity.onBackPressed();
        Toast.makeText(activity, R.string.success_add_trans, Toast.LENGTH_SHORT).show();
    }

    public static void handlingAlarmRepeat(SoGiaoDich soGiaoDich, Context activity, DBHelper dbHelper) {
        String repeat = soGiaoDich.getLapLai();
        if(!repeat.isEmpty() && !repeat.equals("")) {
            switch (soGiaoDich.getLapLai()) {
                case EVERY_DAY:
                    RepeatTransactionModule.handlingRepeatDay(soGiaoDich, dbHelper, activity);
                    break;
                case EVERY_WEEK:
                    RepeatTransactionModule.handlingRepeatWeek(soGiaoDich, dbHelper, activity);
                    break;
                case EVERY_MONTH:
                    RepeatTransactionModule.handlingRepeatMonth(soGiaoDich, dbHelper, activity);
                    break;
                case EVERY_QUARTER:
                    RepeatTransactionModule.handlingRepeatQuarter(soGiaoDich, dbHelper, activity);
                    break;
                default: //EVERY_YEAR
                    RepeatTransactionModule.handlingRepeatYear(soGiaoDich, dbHelper, activity);
                    break;
            }
        }
    }

    public static void handlinAlamrRemind(SoGiaoDich soGiaoDich, Activity activity, DBHelper dbHelper) {
        String remind = soGiaoDich.getNhacNho();
        if(!remind.isEmpty() && !remind.equals("")) {
            String[] str = remind.split("\\s-\\s");
            String dateStr = str[0];
            String timeStr = str[1];
            AlarmRemindModule.handlingAlarmRemind_Date(dateStr, timeStr, activity, soGiaoDich, dbHelper);
        }
    }

    public static void handlingRemind(String remind, SoGiaoDich soGiaoDich) {
        if(!remind.isEmpty() && !remind.equals("")) {
            String[] str = remind.split("\\s-\\s");
            String dateStr = str[0];
            String timeStr = str[1];
            String dateRs = DateDisplayModule.getDateByStringRemind(dateStr);
            soGiaoDich.setNhacNho(dateRs + " - " + timeStr);
        }
        else {
            soGiaoDich.setNhacNho("");
        }
    }

    public static void handlingSetProperty(SoGiaoDich soGiaoDich, ViCaNhan viCaNhan, TietKiem tietKiem, SuKien suKien) {
        if(viCaNhan != null) {
            soGiaoDich.setMaVi(viCaNhan.getMaVi());
        }
        else {
            soGiaoDich.setMaVi("null");
        }
        if(tietKiem != null) {
            soGiaoDich.setMaTietKiem(tietKiem.getMaTietKiem());
        }
        else {
            soGiaoDich.setMaTietKiem("null");
        }
        if(suKien != null) {
            soGiaoDich.setMaSuKien(suKien.getMaSuKien());
        }
        else {
            soGiaoDich.setMaSuKien("null");
        }
    }

    public static void handlingCheckStatus(DBHelper dbHelper, SoGiaoDich soGiaoDich) {
        ArrayList<SoGiaoDich> all = dbHelper.getByStatus_SoGiaoDich(1);
        for (int i = 0; i < all.size(); i++) {
            if(all.get(i).toStringAnother().equals(soGiaoDich.toStringAnother())) {
                dbHelper.delete_SoGiaoDich(all.get(i));
            }
        }
    }

    public static void handlingSetStatus(SoGiaoDich soGiaoDich, CheckBox checkBox) {
        if(checkBox.isChecked()) {
            soGiaoDich.setStatus(1);
        }
        else {
            soGiaoDich.setStatus(0);
        }
    }

    private static void handlingUpdateWallet(String money, DBHelper dbHelper, ViCaNhan viCaNhan, DanhMuc danhMuc, TietKiem tietKiem) {
        final String DATE = "01/01/2100";
        if(viCaNhan != null) {
            if(danhMuc.getLoaiDanhMuc().equals("doanhthu")) {
                viCaNhan.napTien(money);
            }
            else {
                viCaNhan.rutTien(money);
            }
            dbHelper.update_ViCaNhan(viCaNhan);
        }
        else {
            double total = MoneySavingsModule.getMoneySavings(dbHelper, tietKiem);
            if(total == 0) {
                tietKiem.setNgayKetThuc(new java.sql.Date((DateFormatModule.getDate(DATE).getTime())));
            }
            else {
                try {
                    double goal = tietKiem.getSoTien();
                    double average = MoneySavingsModule.getAverageMoney(dbHelper, tietKiem);
                    int num_day = (int) (goal / average);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date(Calendar.getInstance().getTime().getTime());
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(now);
                    calendar.add(GregorianCalendar.DAY_OF_MONTH, num_day);
                    tietKiem.setNgayKetThuc(java.sql.Date.valueOf(df.format(calendar.getTime())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dbHelper.update_TietKiem(tietKiem);
        }
    }

    private static void handlingCheckBudget(SoGiaoDich soGiaoDich, DBHelper dbHelper, Context activity) {
        final String KHOANCHI = "khoanchi";
        int tongtien = 0;
        DanhMuc danhmuc = dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc());
        if(danhmuc.getLoaiDanhMuc().equals(KHOANCHI)) {
            ChiTietNganSach chiTietNganSach = new ChiTietNganSach();
            java.sql.Date dateSGD = soGiaoDich.getNgayGiaoDich();
            ArrayList<NganSach> allNganSach = dbHelper.getAll_NganSach();

            for (int i = 0; i < allNganSach.size(); i++) {
                NganSach nganSach = allNganSach.get(i);
                if(dateSGD.equals(nganSach.getNgayBatDau())
                        || dateSGD.equals(nganSach.getNgayKetThuc())
                        || dateSGD.after(nganSach.getNgayBatDau())
                        && dateSGD.before(nganSach.getNgayKetThuc())) {
                    chiTietNganSach.setMaGiaoDich(soGiaoDich.getMaGiaoDich());
                    chiTietNganSach.setMaNganSach(nganSach.getMaNganSach());
                    dbHelper.insert_ChiTietNganSach(chiTietNganSach);

                    ArrayList<ChiTietNganSach> listbudget = dbHelper.getByIDBudget_ChiTietNganSach(nganSach.getMaNganSach());
                    for (int i1 = 0; i1 < listbudget.size(); i1++) {
                        SoGiaoDich giaoDich = dbHelper.getByID_SoGiaoDich(listbudget.get(i1).getMaGiaoDich());
                        int tien = (int) giaoDich.getSoTien();
                        tongtien += tien;

                    }
                    if(tongtien > nganSach.getSoTien()){
                        java.sql.Date now = new java.sql.Date(new Date(Calendar.getInstance().getTime().getTime()).getTime());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(now);
                        int year=calendar.get(Calendar.YEAR);
                        int month=calendar.get(Calendar.MONTH)+1;
                        int date=calendar.get(Calendar.DAY_OF_MONTH);
                        String dateStr=year+"-"+month+"-"+date;
                        Log.d("time", tongtien+"");
                        Log.d("money", nganSach.getSoTien()+"");
                        AlarmbudgetModule.handlingAlarmRepeat_Date_budget(java.sql.Date.valueOf(dateStr), activity, nganSach, dbHelper,0);
                    }
                }
            }


        }
    }
}
