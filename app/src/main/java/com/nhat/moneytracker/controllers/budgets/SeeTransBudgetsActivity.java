package com.nhat.moneytracker.controllers.budgets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.budgets.MoneyBudgetModule;
import com.nhat.moneytracker.modules.budgets.Transs_budgets_Module;
import com.nhat.moneytracker.modules.displays.TransactionDisplayModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.refreshs.SwipeRefreshModule;

import java.util.ArrayList;

public class SeeTransBudgetsActivity extends AppCompatActivity implements IMappingView {
    private ImageButton buttonReturn;
    private TextView textViewMoney;
    private ListView listView;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets_see_trans);
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
        String idBudgets = getIntent().getExtras().getString("idBudgets");
        if (!idBudgets.isEmpty() && idBudgets != null) {
            NganSach nganSach = dbHelper.getByID_NganSach(idBudgets);
            double total = MoneyBudgetModule.getMoneyUseBudget(dbHelper, nganSach);
            textViewMoney.setText(FormatMoneyModule.formatAmount(total) + " VND");
            ArrayList<SoGiaoDich> list = Transs_budgets_Module.gettranssBudget(dbHelper, nganSach);
            TransactionDisplayModule.showListViewHome_Transaction(list, getApplicationContext(), listView, dbHelper);
        }
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
        buttonReturn = findViewById(R.id.buttonReturn_budgets_see_trans);
        textViewMoney = findViewById(R.id.textViewMoney_budgets_see_trans);
        swipeRefresh = findViewById(R.id.swipeRefresh_budgets_see_trans);
        listView = findViewById(R.id.listView_budgets_see_trans);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
