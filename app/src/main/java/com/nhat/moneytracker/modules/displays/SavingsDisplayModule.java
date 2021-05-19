package com.nhat.moneytracker.modules.displays;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.chooses.ChooseSavingsActivity;
import com.nhat.moneytracker.controllers.savings.DetailSavingsActivity;
import com.nhat.moneytracker.controllers.savings.HomeSavingsActivity;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.savings.MoneySavingsModule;
import com.nhat.moneytracker.sessions.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SavingsDisplayModule {
    private static Session session;

    public static void showListViewHome_Savings(final ArrayList<TietKiem> list, final Context context,
                                                ListView listView, DBHelper dbHelper) {
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            double total = MoneySavingsModule.getMoneySavings(dbHelper, list.get(i));
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("listView_name", list.get(i).getTenTietKiem());
            hashMap.put("listView_money", FormatMoneyModule.formatAmount(total) + " VND");
            hashMap.put("image", String.valueOf(R.drawable.ic_baseline_library_add_24));
            mapList.add(hashMap);
        }

        String[] from = {"image", "listView_name", "listView_money"};
        int[] to = {R.id.imageView_Savings, R.id.textView_Name_Savings, R.id.textView_Money_Savings};

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, mapList, R.layout.savings_list_item_layout, from, to);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idSavings = list.get(position).getMaTietKiem();
                Intent intent = new Intent(context.getApplicationContext(), DetailSavingsActivity.class);
                intent.putExtra("idSavings", idSavings);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
                HomeSavingsActivity.activity.onBackPressed();
            }
        });
    }

    public static void showListViewChoose_Savings(final ArrayList<TietKiem> list, final Context context,
                                                ListView listView, DBHelper dbHelper) {
        session = new Session(context);
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            double total = MoneySavingsModule.getMoneySavings(dbHelper, list.get(i));
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("listView_name", list.get(i).getTenTietKiem());
            hashMap.put("listView_money", FormatMoneyModule.formatAmount(total) + " VND");
            hashMap.put("image", String.valueOf(R.drawable.ic_baseline_library_add_24));
            mapList.add(hashMap);
        }

        String[] from = {"image", "listView_name", "listView_money"};
        int[] to = {R.id.imageView_Savings, R.id.textView_Name_Savings, R.id.textView_Money_Savings};

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, mapList, R.layout.savings_list_item_layout, from, to);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                session.clearSavings();
                String idSavings = list.get(position).getMaTietKiem();
                session.setIDSavings(idSavings);
                ChooseSavingsActivity.activity.onBackPressed();
            }
        });
    }
}
