package com.nhat.moneytracker.controllers.budgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.dates.DateGetStringModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.sessions.Session;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EditBudgetsActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private ImageButton buttonCancel;
    private EditText editTextMoney, editTextDate;
    private DBHelper dbHelper;
    private NganSach nganSach;
    private String timeStart, timeEnd;
    private Session session;
    String idBudgets;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_edit);
        init();
        loadData();
        loadDate();
        eventReturn();
        eventChooseDate();
        eventSave();
    }

    private void eventSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = editTextMoney.getText().toString().replace(",", "");
                String dateStr = editTextDate.getText().toString();

                if(CheckEmptyModule.isEmpty(money, dateStr, money)) {
                    java.sql.Date sqlStart = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(timeStart));
                    java.sql.Date sqlEnd = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(timeEnd));
                    if(money.length() > 3 && money.substring(money.length() - 3).equals("000")) {
                        if(Double.parseDouble(money) >= 300000) {
                            if(checkdate(sqlStart.toString(),sqlEnd.toString())){
                                handlingSave(sqlStart, sqlEnd, money);
                            } else Toast.makeText(getApplicationContext(), R.string.condition_timee, Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getApplicationContext(), R.string.condition_money, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), R.string.invalid_money, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlingSave(final  Date sqlStart, final  Date sqlEnd,final String money) {
        nganSach.setSoTien(Double.parseDouble(money));
        nganSach.setNgayBatDau(sqlStart);
        nganSach.setNgayKetThuc(sqlEnd);
        dbHelper.update_NganSach(nganSach);
        onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.success_budget_edit, Toast.LENGTH_SHORT).show();
    }

    private boolean checkdate(String sqlStart, String sqlEnd){
        ArrayList<NganSach> all = dbHelper.getAll_NganSach();
        for(int i = 0; i < all.size(); i++){
            if(nganSach.getNgayBatDau().toString().equals(sqlStart) && nganSach.getNgayKetThuc().toString().equals(sqlEnd)) {
                return true;
            }
            else if(all.get(i).getNgayBatDau().toString().equals(sqlStart) && all.get(i).getNgayKetThuc().toString().equals(sqlEnd)){
                return false;
            }
        }
        return true;
    }
    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBudgetsActivity.this, ChooseTimeActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        idBudgets = getIntent().getExtras().getString("idBudgets");
        if(!idBudgets.isEmpty() && idBudgets != null) {
            nganSach = dbHelper.getByID_NganSach(idBudgets);
            editTextMoney.setText(FormatMoneyModule.formatAmount(nganSach.getSoTien()));
            editTextDate.setText(formatter.format(nganSach.getNgayBatDau()) + " - " + formatter.format(nganSach.getNgayKetThuc()));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void loadDate() {
        String nameTime = session.getNameTime();
        timeStart = session.getTimeStart();
        timeEnd = session.getTimeEnd();
        if(timeStart != null && !timeStart.isEmpty() && timeEnd != null && !timeEnd.isEmpty()) {
            editTextDate.setText(timeStart + " - " + timeEnd);
        }
        else if(nameTime != null && !nameTime.isEmpty()) {
            editTextDate.setText(nameTime);
            timeStart = DateGetStringModule.getDayStartByNameTime(nameTime);
            timeEnd = DateGetStringModule.getDayEndByNameTime(nameTime);
        }
        else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            timeStart = formatter.format(nganSach.getNgayBatDau());
            timeEnd = formatter.format(nganSach.getNgayKetThuc());
            editTextDate.setHint(R.string.choosetime);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onRestart() {
        super.onRestart();
        loadDate();
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
        buttonCancel = findViewById(R.id.buttonCancel_budget_edit);
        buttonSave = findViewById(R.id.buttonSave_budget_edit);
        editTextMoney = findViewById(R.id.editTextMoney_budget_edit);
        editTextDate = findViewById(R.id.editTextDate_budget_edit);
        dbHelper = new DBHelper(this);
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        editTextMoney.requestFocus();
        session = new Session(this);
        session.clear();
        getSupportActionBar().hide();
    }
}