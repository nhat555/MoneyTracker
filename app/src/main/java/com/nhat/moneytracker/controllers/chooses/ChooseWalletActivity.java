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
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.displays.WalletDisplayModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;
import com.nhat.moneytracker.sessions.Session;

import java.util.ArrayList;

public class ChooseWalletActivity extends AppCompatActivity implements IMappingView {
    private ListView listViewMyWallet;
    private ImageButton buttonReturn;
    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    private static final String WALLET_FROM = "from";
    private static final String WALLET_FROM_TRANSFER = "from_transfers";
    private static final String WALLET_TO = "to";
    private static final String WALLET_TOTAL = "total";
    private Session session;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_home_choose);
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
        String name = getIntent().getExtras().getString("name");
        switch (name) {
            case WALLET_FROM: {
                ArrayList<ViCaNhan> list = dbHelper.getAll_ViCaNhan();
                WalletDisplayModule.showListViewChoose_Wallet(list, getApplicationContext(), listViewMyWallet, WALLET_FROM);
                break;
            }
            case WALLET_TO: {
                ViCaNhan viCaNhan = getViFrom();
                ArrayList<ViCaNhan> list = dbHelper.getAllAnother_ViCaNhan(viCaNhan.getMaVi());
                WalletDisplayModule.showListViewChoose_Wallet(list, getApplicationContext(), listViewMyWallet, WALLET_TO);
                break;
            }
            case WALLET_FROM_TRANSFER: {
                ViCaNhan viCaNhan = getViTo();
                ArrayList<ViCaNhan> list = dbHelper.getAllAnother_ViCaNhan(viCaNhan.getMaVi());
                WalletDisplayModule.showListViewChoose_Wallet(list, getApplicationContext(), listViewMyWallet, WALLET_FROM);
                break;
            }
            default: {
                ArrayList<ViCaNhan> list = dbHelper.getAll_ViCaNhan();
                WalletDisplayModule.showListViewChoose_Wallet(list, getApplicationContext(), listViewMyWallet, WALLET_TOTAL);
                break;
            }
        }
    }

    private ViCaNhan getViFrom() {
        ViCaNhan viCaNhan = null;
        String idWallet = session.getIDWallet();
        if(idWallet != null && !idWallet.isEmpty()) {
            try {
                viCaNhan = dbHelper.getByID_ViCaNhan(idWallet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return viCaNhan;
    }

    private ViCaNhan getViTo() {
        ViCaNhan viCaNhan = null;
        String idWallet = session.getIDWalletReceive();
        if(idWallet != null && !idWallet.isEmpty()) {
            try {
                viCaNhan = dbHelper.getByID_ViCaNhan(idWallet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return viCaNhan;
    }

    @Override
    public void init() {
        listViewMyWallet = findViewById(R.id.listView_mywallet_choose);
        buttonReturn = findViewById(R.id.buttonReturn_wallet_home_choose);
        swipeRefresh = findViewById(R.id.swipeRefresh_mywallet_choose);
        dbHelper = new DBHelper(this);
        session = new Session(this);
        getSupportActionBar().hide();
    }
}
