package com.nhat.moneytracker.controllers.savings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.dates.ChooseDateModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;

import java.util.Calendar;
import java.util.Date;

public class AddSavingsActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private ImageButton buttonCancel;
    private EditText editTextName, editTextDate, editTextMoney;
    private DBHelper dbHelper;
    private static final String DATE = "01/01/2100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_add);
        init();
        eventReturn();
        eventChooseDate();
        eventSave();
    }

    private void eventSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String dateStr = editTextDate.getText().toString();
                String money = editTextMoney.getText().toString().replace(",", "");
                if(CheckEmptyModule.isEmpty(name, dateStr, money)) {
                    if(money.length() > 3 && Double.parseDouble(money) > 0 && money.substring(money.length() - 3).equals("000")) {
                        java.sql.Date sqlDate = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
                        try {
                            dbHelper.getByName_TietKiem(name);
                            Toast.makeText(getApplicationContext(), R.string.name_exist_savings, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            handlingSave(name, sqlDate, money);
                        }
                    } else Toast.makeText(getApplicationContext(), R.string.invalid_money, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlingSave(final String name, final java.sql.Date date, final String money) {
        TietKiem tietKiem = new TietKiem();
        tietKiem.setMaTietKiem(RandomIDModule.getSavingsID(dbHelper));
        tietKiem.setTenTietKiem(name);
        tietKiem.setNgayBatDau(new java.sql.Date(new Date(Calendar.getInstance().getTime().getTime()).getTime()));
        tietKiem.setSoTien(Double.parseDouble(money));
        tietKiem.setNgayKetThuc(date);
        dbHelper.insert_TietKiem(tietKiem);
        onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.success_savings_add, Toast.LENGTH_SHORT).show();
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(AddSavingsActivity.this, editTextDate, true);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddSavingsActivity.this, HomeSavingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void init() {
        buttonCancel = findViewById(R.id.buttonCancel_saving_add);
        buttonSave = findViewById(R.id.buttonSave_saving_add);
        editTextName = findViewById(R.id.editTextName_saving_add);
        editTextDate = findViewById(R.id.editTextDate_saving_add);
        editTextMoney = findViewById(R.id.editTextMoney_saving_add);
        dbHelper = new DBHelper(this);
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        editTextName.requestFocus();
        getSupportActionBar().hide();
    }
}
