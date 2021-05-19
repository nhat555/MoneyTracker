package com.nhat.moneytracker.controllers.mains;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.transactions.AddTransactionActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.alarms.AlarmFirstModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements IMappingView {
    private BottomNavigationView navView;
    private DBHelper dbHelper;
    private String[] NAME_REVENUE_ARRAY;
    private String[] NAME_EXPENSE_ARRAY;
    private String[] ICON_REVENUE_ARRAY;
    private String[] ICON_EXPENSE_ARRAY;
    private ViCaNhan viCaNhanSH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        saveDataSystem();
        displayBottomNavigation();
        eventClickAddTransaction();
    }

    private void saveDataSystem() {
        saveWallet();
        saveCategory();
    }

    private void saveWallet() {
        if(dbHelper.getAll_ViCaNhan().isEmpty()) {
            viCaNhanSH = new ViCaNhan();
            viCaNhanSH.setMaVi(RandomIDModule.getWalletID(dbHelper));
            viCaNhanSH.setTenVi(getResources().getString(R.string.wallet_system_sh));
            viCaNhanSH.setSoTien(0);
            dbHelper.insert_ViCaNhan(viCaNhanSH);

            ViCaNhan viCaNhanOther = new ViCaNhan();
            viCaNhanOther.setMaVi(RandomIDModule.getWalletID(dbHelper));
            viCaNhanOther.setTenVi(getResources().getString(R.string.wallet_system_other));
            viCaNhanOther.setSoTien(0);
            dbHelper.insert_ViCaNhan(viCaNhanOther);
        }
    }

    private void saveCategory() {
        if(dbHelper.getAll_DanhMuc().isEmpty()) {
            saveExpenseCate();
            saveRevenueCate();
            alarmFirst(HomeActivity.this);
        }
    }

    private void saveExpenseCate() {
        for (int i = 0; i < NAME_EXPENSE_ARRAY.length; i++) {
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setMaDanhMuc(RandomIDModule.getCategoryID(dbHelper));
            danhMuc.setTenDanhMuc(NAME_EXPENSE_ARRAY[i]);
            danhMuc.setBieuTuong(ICON_EXPENSE_ARRAY[i]);
            danhMuc.setLoaiDanhMuc("khoanchi");
            danhMuc.setMaVi(viCaNhanSH.getMaVi());
            dbHelper.insert_DanhMuc(danhMuc);
        }
    }

    private void saveRevenueCate() {
        for (int i = 0; i < NAME_REVENUE_ARRAY.length; i++) {
            DanhMuc danhMuc = new DanhMuc();
            danhMuc.setMaDanhMuc(RandomIDModule.getCategoryID(dbHelper));
            danhMuc.setTenDanhMuc(NAME_REVENUE_ARRAY[i]);
            danhMuc.setBieuTuong(ICON_REVENUE_ARRAY[i]);
            danhMuc.setLoaiDanhMuc("doanhthu");
            danhMuc.setMaVi(viCaNhanSH.getMaVi());
            dbHelper.insert_DanhMuc(danhMuc);
        }
    }

    private void alarmFirst(Activity activity) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        dateFormat.format(calendar.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String date = dayOfMonth + "/" + month + "/" + year;
        Log.d("DATE", date);
        AlarmFirstModule.handlingAlarmFirst_Date(date, activity);
    }

    private void displayBottomNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.trans_book, R.id.speed, R.id.planning, R.id.personal)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void eventClickAddTransaction() {
        FloatingActionButton button = findViewById(R.id.buttonTransactionAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void init() {
        navView = findViewById(R.id.nav_view);
        dbHelper = new DBHelper(this);
        NAME_EXPENSE_ARRAY = getResources().getStringArray(R.array.name_cate_expense_system);
        NAME_REVENUE_ARRAY = getResources().getStringArray(R.array.name_cate_revenue_system);
        ICON_EXPENSE_ARRAY = getResources().getStringArray(R.array.icon_cate_expense_system);
        ICON_REVENUE_ARRAY = getResources().getStringArray(R.array.icon_cate_revenue_system);
        getSupportActionBar().hide();
    }
}
