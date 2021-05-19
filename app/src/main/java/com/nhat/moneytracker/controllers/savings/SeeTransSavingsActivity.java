package com.nhat.moneytracker.controllers.savings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.TransactionDisplayModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;
import com.nhat.moneytracker.modules.savings.MoneySavingsModule;

import java.util.ArrayList;

public class SeeTransSavingsActivity extends AppCompatActivity implements IMappingView {
    private ImageButton buttonReturn;
    private TextView textViewMoney;
    private ListView listView;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_see_trans);
        init();
        loadData();
        eventReturn();
        eventRefresh();
    }

    private void eventRefresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };
        SwipeRefreshModule.eventRefresh(swipeRefresh, runnable);
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        String idSavings = getIntent().getExtras().getString("idSavings");
        ArrayList<SoGiaoDich> list = dbHelper.getBySavings_SoGiaoDich(idSavings);
        TietKiem tietKiem = dbHelper.getByID_TietKiem(idSavings);
        double total = MoneySavingsModule.getMoneySavings(dbHelper, tietKiem);
        textViewMoney.setText(FormatMoneyModule.formatAmount(total) + " VND");
        TransactionDisplayModule.showListViewHome_Transaction(list, getApplicationContext(), listView, dbHelper);
    }

    private void eventReturn() {
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public void init() {
        buttonReturn = findViewById(R.id.buttonReturn_savings_see_trans);
        textViewMoney = findViewById(R.id.textViewMoney_savings_see_trans);
        listView = findViewById(R.id.listView_savings_see_trans);
        swipeRefresh = findViewById(R.id.swipeRefresh_savings_see_trans);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
