package com.nhat.moneytracker.controllers.budgets;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.budgets.BudgetsFinishModule;
import com.nhat.moneytracker.modules.displays.BudgetDisplayModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;

import java.util.ArrayList;

public class TabFinishBudgetsActivity extends AppCompatActivity implements IMappingView {
    private ListView listView;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets_tab_finish);
        init();
        loadData();
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

    private void loadData() {
        ArrayList<NganSach> list = BudgetsFinishModule.getBudgetsFinish(dbHelper);
        BudgetDisplayModule.showListViewHome_Budget(list, getApplicationContext(), listView, dbHelper);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void init() {
        listView = findViewById(R.id.listView_budgets_tab_finish);
        swipeRefresh = findViewById(R.id.swipeRefresh_budgets_tab_finish);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
