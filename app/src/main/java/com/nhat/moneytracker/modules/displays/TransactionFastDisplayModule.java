package com.nhat.moneytracker.modules.displays;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.transactions.AddTransactionActivity;
import com.nhat.moneytracker.controllers.transactions.DetailTransactionActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.checks.CheckPropertyRepeatModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.icons.IconsDrawableModule;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventSaveTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionFastDisplayModule {
    private static final String NOW = "Hôm nay";

    public static void showListViewHomeFast_Transaction(final ArrayList<SoGiaoDich> list, final FragmentActivity context,
                                                        ListView listView, final DBHelper dbHelper) {
        List<HashMap<String, String>> mapList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < list.size(); i++) {
            DanhMuc danhMuc = dbHelper.getByID_DanhMuc(list.get(i).getMaDanhMuc());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("listView_name", danhMuc.getTenDanhMuc());
            hashMap.put("listView_note", list.get(i).getGhiChu());
            hashMap.put("listView_date", formatter.format(list.get(i).getNgayGiaoDich()));
            if(danhMuc.getLoaiDanhMuc().equals("doanhthu")) {
                hashMap.put("listView_money", "+" + FormatMoneyModule.formatAmount(list.get(i).getSoTien()) + " VND");
            }
            else {
                hashMap.put("listView_money", "-" + FormatMoneyModule.formatAmount(list.get(i).getSoTien()) + " VND");
            }
            hashMap.put("image", String.valueOf(IconsDrawableModule.getResourcesDrawble(context, danhMuc.getBieuTuong())));
            mapList.add(hashMap);
        }

        String[] from = {"image", "listView_name", "listView_note", "listView_date", "listView_money"};
        int[] to = {R.id.imageView_Trans_book, R.id.textViewNameCate_Trans_book, R.id.textViewNote_Trans_book,
                R.id.textViewDate_Trans_book, R.id.textViewMoney_Trans_book};

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, mapList, R.layout.transaction_list_item_layout, from, to);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idTrans = list.get(position).getMaGiaoDich();
                SoGiaoDich soGiaoDich = dbHelper.getByID_SoGiaoDich(idTrans);
                DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc());
                ViCaNhan viCaNhan = CheckPropertyRepeatModule.checkWallet(soGiaoDich.getMaVi(), dbHelper);
                SuKien suKien = CheckPropertyRepeatModule.checkEvent(soGiaoDich.getMaSuKien(), dbHelper);
                TietKiem tietKiem = CheckPropertyRepeatModule.checkSavings(soGiaoDich.getMaTietKiem(), dbHelper);
                if(danhMuc.getLoaiDanhMuc().equals("khoanchi") && soGiaoDich.getSoTien() > viCaNhan.getSoTien()) {
                    Toast.makeText(context, R.string.fast_money_error, Toast.LENGTH_SHORT).show();
                }
                else {
                    EventSaveTemplate.handlingSaveTransFast(dbHelper, String.valueOf(soGiaoDich.getSoTien()), soGiaoDich.getGhiChu(),
                            DateFormatModule.getDateSQL(NOW), danhMuc, context, viCaNhan, tietKiem, suKien,
                            soGiaoDich.getNhacNho(), soGiaoDich.getLapLai());
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String idTrans = list.get(position).getMaGiaoDich();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.transaction_fast_dialog);
                Button buttonCheck = dialog.findViewById(R.id.buttonCheck_transaction_fast_dialog);
                Button buttonDetails = dialog.findViewById(R.id.buttonDetails_transaction_fast_dialog);
                dialog.show();

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                eventCheck(buttonCheck, context, idTrans, dialog);
                eventDetails(buttonDetails, context, idTrans, dialog);

                return true;
            }
        });
    }

    private static void eventDetails(Button button, final FragmentActivity context, final String idTrans, final Dialog dialog) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DetailTransactionActivity.class);
                intent.putExtra("idTrans", idTrans);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private static void eventCheck(Button button, final FragmentActivity context, final String idTrans, final Dialog dialog) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), AddTransactionActivity.class);
                intent.putExtra("idTrans", idTrans);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
