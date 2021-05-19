package com.nhat.moneytracker.controllers.chooses;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.CategoryDisplayModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;

import java.util.ArrayList;

public class TabExpensesCateActivity extends AppCompatActivity implements IMappingView {
    private ListView listViewCategory;
    private DBHelper dbHelper;
    private static final String DOANHTHU = "doanhthu";
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_tab_expenses);
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
        ArrayList<DanhMuc> list = dbHelper.getByCate_DanhMuc(DOANHTHU);
        CategoryDisplayModule.showListViewChoose_Category(list, getApplicationContext(), listViewCategory);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void init() {
        listViewCategory = findViewById(R.id.listView_catetory_tab_expenses);
        swipeRefresh = findViewById(R.id.swipeRefresh_catetory_tab_expenses);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
