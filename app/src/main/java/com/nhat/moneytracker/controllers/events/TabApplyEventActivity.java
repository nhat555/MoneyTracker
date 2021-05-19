package com.nhat.moneytracker.controllers.events;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.EventDisplayModule;
import com.nhat.moneytracker.modules.events.EventApplyModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;

import java.util.ArrayList;

public class TabApplyEventActivity extends AppCompatActivity implements IMappingView {
    private ListView listViewEvent;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_tab_apply);
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
        ArrayList<SuKien> list = EventApplyModule.getEventApply(dbHelper);
        EventDisplayModule.showListViewHome_Event(list, getApplicationContext(), listViewEvent, dbHelper);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void init() {
        listViewEvent = findViewById(R.id.listView_event_tab_apply);
        swipeRefresh = findViewById(R.id.swipeRefresh_event_tab_apply);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
