package com.nhat.moneytracker.modules.displays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.budgets.DetailBudgetsActivity;
import com.nhat.moneytracker.controllers.budgets.HomeBudgetActivity;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.budgets.MoneyBudgetModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetDisplayModule {

    public static void showListViewHome_Budget(final ArrayList<NganSach> list, final Context context,
                                               ListView listView, DBHelper dbHelper) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("listView_date", formatter.format(list.get(i).getNgayBatDau()) + " - " + formatter.format(list.get(i).getNgayKetThuc()));
            hashMap.put("listView_goal", FormatMoneyModule.formatAmount(list.get(i).getSoTien()) + " VND");
            hashMap.put("listView_money_use", FormatMoneyModule.formatAmount
                    (MoneyBudgetModule.getMoneyUseBudget(dbHelper, list.get(i))) + " VND");
            hashMap.put("image", String.valueOf(R.drawable.ic_markunread_mailbox_black_24dp));
            mapList.add(hashMap);
        }

        String[] from = {"image", "listView_date", "listView_goal", "listView_money_use"};
        int[] to = {R.id.imageView_Budget, R.id.textView_Date_Budget, R.id.textView_Goal_Budget,
                R.id.textView_MoneyUse_Budget};

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, mapList, R.layout.budget_list_item_layout, from, to);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idBudgets = list.get(position).getMaNganSach();
                Intent intent = new Intent(context.getApplicationContext(), DetailBudgetsActivity.class);
                intent.putExtra("idBudgets", idBudgets);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
                HomeBudgetActivity.activity.onBackPressed();
            }
        });
    }

}
