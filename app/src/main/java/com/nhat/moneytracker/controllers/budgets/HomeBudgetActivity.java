package com.nhat.moneytracker.controllers.budgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.interfaces.IMappingView;

public class HomeBudgetActivity extends TabActivity implements IMappingView {
    private ImageButton imageButtonAdd, buttonReturn;
    private static final String APDUNG = "Đang áp dụng";
    private static final String KETTHUC = "Đã kết thúc";
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_home);
        activity = this;
        init();
        tabHost();
        eventReturn();
        eventAddBudget();
    }

    private void eventAddBudget() {
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBudgetActivity.this, AddBudgetsActivity.class);
                startActivity(intent);
                onBackPressed();
            }
        });
    }

    private void eventReturn() {
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void tabHost() {
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tab1 = tabHost.newTabSpec(APDUNG);
        tab1.setIndicator(APDUNG, getResources().getDrawable(R.color.colorLightBlue));
        Intent photosIntent = new Intent(HomeBudgetActivity.this, TabApplyBudgetsActivity.class);
        tab1.setContent(photosIntent);

        TabHost.TabSpec tab2 = tabHost.newTabSpec(KETTHUC);
        tab2.setIndicator(KETTHUC, getResources().getDrawable(R.color.colorLightBlue));
        Intent songsIntent = new Intent(HomeBudgetActivity.this, TabFinishBudgetsActivity.class);
        tab2.setContent(songsIntent);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }

    @Override
    public void init() {
        buttonReturn = findViewById(R.id.buttonReturn_budget_home);
        imageButtonAdd = findViewById(R.id.imageButtonAdd_budget_home);
    }
}
