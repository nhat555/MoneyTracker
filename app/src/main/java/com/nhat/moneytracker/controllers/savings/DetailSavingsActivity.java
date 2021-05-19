package com.nhat.moneytracker.controllers.savings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.alerts.AlertConfirmModule;
import com.nhat.moneytracker.modules.checks.CheckMoneyFinishModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.dates.DateBetweenModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailSavingsActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSeeTrans;
    private ImageButton buttonCancel, buttonEdit, buttonDelete;
    private TextView editTextName, editTextMoney, editTextDateStart, editTextDay, editTextDateFinish;
    private DBHelper dbHelper;
    private String idSavings;
    private TietKiem tietKiem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_detail);
        init();
        loadData();
        eventReturn();
        eventEdit();
        eventDelete();
        eventSeeTrans();
    }

    private void eventSeeTrans() {
        buttonSeeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailSavingsActivity.this, SeeTransSavingsActivity.class);
                intent.putExtra("idSavings", idSavings);
                startActivity(intent);
            }
        });
    }

    private void eventDelete() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateTransaction();
                        dbHelper.delete_TietKiem(tietKiem);
                        Toast.makeText(getApplicationContext(), R.string.delete_savings_success, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                };
                AlertConfirmModule.alertDialogConfirm(DetailSavingsActivity.this, R.string.mes_delete_savings, runnable);
            }
        });
    }

    private void updateTransaction() {
        ArrayList<SoGiaoDich> list = dbHelper.getBySavings_SoGiaoDich(idSavings);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setMaSuKien("null");
            dbHelper.update_SoGiaoDich(list.get(i));
        }
    }

    private void eventEdit() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailSavingsActivity.this, EditSavingsActivity.class);
                intent.putExtra("idSavings", idSavings);
                startActivity(intent);
            }
        });
    }

    private void eventReturn() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        idSavings = getIntent().getExtras().getString("idSavings");
        tietKiem = dbHelper.getByID_TietKiem(idSavings);
        int date = DateBetweenModule.daysBetween(tietKiem.getNgayKetThuc(), new Date(Calendar.getInstance().getTime().getTime()));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        editTextName.setText(tietKiem.getTenTietKiem());
        editTextMoney.setText(FormatMoneyModule.formatAmount(tietKiem.getSoTien()) + " VND");
        editTextMoney.setTextColor(getResources().getColor(R.color.colorBlack));
        editTextMoney.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dollarsymbol, 0, 0, 0);
        editTextDateStart.setText(formatter.format(tietKiem.getNgayBatDau()) + " (Ngày bắt đầu)");
        editTextDateFinish.setText(formatter.format(tietKiem.getNgayKetThuc()) + " (Ngày kết thúc)");
        if(CheckMoneyFinishModule.isFinish(dbHelper, tietKiem)) {
            editTextDay.setText("Đã đạt mục tiêu");
        }
        else {
            editTextDay.setText("Còn lại " + date + " ngày");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailSavingsActivity.this, HomeSavingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public void init() {
        buttonCancel = findViewById(R.id.buttonCancel_savings_detail);
        buttonEdit = findViewById(R.id.buttonEdit_savings_detail);
        buttonDelete = findViewById(R.id.buttonDelete_savings_detail);
        buttonSeeTrans = findViewById(R.id.buttonSeeTrans_savings_detail);
        editTextName = findViewById(R.id.editTextName_savings_detail);
        editTextMoney = findViewById(R.id.editTextMoney_savings_detail);
        editTextDateStart = findViewById(R.id.editTextDateStart_savings_detail);
        editTextDateFinish = findViewById(R.id.editTextDateFinish_savings_detail);
        editTextDay = findViewById(R.id.editTextDay_savings_detail);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
