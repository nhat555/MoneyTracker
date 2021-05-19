package com.nhat.moneytracker.controllers.chooses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.SavingsDisplayModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;
import com.nhat.moneytracker.modules.savings.SavingsApplyModule;

import java.util.ArrayList;

public class ChooseSavingsActivity extends AppCompatActivity implements IMappingView {
    private ListView listView;
    private ImageButton buttonReturn;
    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_home_choose);
        activity = this;
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

    private void eventReturn() {
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
        ArrayList<TietKiem> list = SavingsApplyModule.getSavingsApply(dbHelper);
        SavingsDisplayModule.showListViewChoose_Savings(list, getApplicationContext(), listView, dbHelper);
    }

    @Override
    public void init() {
        listView = findViewById(R.id.listView_savings_home_choose);
        buttonReturn = findViewById(R.id.buttonReturn_savings_home_choose);
        swipeRefresh = findViewById(R.id.swipeRefresh_savings_home_choose);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
