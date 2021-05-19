package com.nhat.moneytracker.controllers.budgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.TimeDisplayModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;

public class ChooseTimeActivity extends AppCompatActivity implements IMappingView {
    private ListView listView;
    private ImageButton buttonCancel;
    private EditText editTextDateStart, editTextDateEnd;
    private Button buttonChooseTime;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    private SwipeRefreshLayout swipeRefresh;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets_choose_time);
        activity = this;
        init();
        eventReturn();
        loadData();
        eventRefresh();
    }

    private void eventRefresh() {
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                loadData();
            }
        };
        SwipeRefreshModule.eventRefresh(swipeRefresh, runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        TimeDisplayModule.showListView_Budget_time(ChooseTimeActivity.this, listView);
    }

    private void eventReturn() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void init() {
        listView = findViewById(R.id.Listview_budget_choosetime);
        buttonCancel = findViewById(R.id.buttonCancel_budget_choosetime);
        swipeRefresh = findViewById(R.id.swipeRefresh_budget_choosetime);
        getSupportActionBar().hide();
    }
}
