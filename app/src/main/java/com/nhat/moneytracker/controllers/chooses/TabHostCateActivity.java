package com.nhat.moneytracker.controllers.chooses;

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

public class TabHostCateActivity extends TabActivity implements IMappingView {
    private ImageButton buttonReturn;
    private static String DOANHTHU;
    private static String KHOANCHI;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_home_choose);
        activity = this;
        init();
        tabHost();
        eventReturn();
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

        TabHost.TabSpec expenses = tabHost.newTabSpec(KHOANCHI);
        expenses.setIndicator(KHOANCHI, getResources().getDrawable(R.color.colorLightBlue));
        Intent songsIntent = new Intent(TabHostCateActivity.this, TabRevenueCateActivity.class);
        expenses.setContent(songsIntent);

        TabHost.TabSpec revenue = tabHost.newTabSpec(DOANHTHU);
        revenue.setIndicator(DOANHTHU, getResources().getDrawable(R.color.colorLightBlue));
        Intent photosIntent = new Intent(TabHostCateActivity.this, TabExpensesCateActivity.class);
        revenue.setContent(photosIntent);

        tabHost.addTab(expenses);
        tabHost.addTab(revenue);
    }

    @Override
    public void init() {
        buttonReturn = findViewById(R.id.buttonReturn_catetory_choose);
        DOANHTHU = getResources().getString(R.string.recharge_money);
        KHOANCHI = getResources().getString(R.string.spend_money);
    }
}
