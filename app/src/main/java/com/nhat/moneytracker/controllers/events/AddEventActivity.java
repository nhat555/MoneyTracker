package com.nhat.moneytracker.controllers.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.dates.ChooseDateModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;

import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private ImageButton buttonCancel;
    private EditText editTextName, editTextDate;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);
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
                if(CheckEmptyModule.isEmpty(name, dateStr, name)) {
                    java.sql.Date sqlDate = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
                    try {
                        dbHelper.getByName_SuKien(name);
                        Toast.makeText(getApplicationContext(), R.string.name_exist_event, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        handlingSave(name, sqlDate);
                    }
                } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlingSave(final String name, final java.sql.Date date) {
        SuKien suKien = new SuKien();
        suKien.setMaSuKien(RandomIDModule.getEventID(dbHelper));
        suKien.setTenSuKien(name);
        suKien.setNgayBatDau(new java.sql.Date(new Date(Calendar.getInstance().getTime().getTime()).getTime()));
        suKien.setNgayKetThuc(date);
        dbHelper.insert_SuKien(suKien);
        onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.success_event_add, Toast.LENGTH_SHORT).show();
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(AddEventActivity.this, editTextDate, true);
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
        Intent intent = new Intent(AddEventActivity.this, HomeEventActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void init() {
        buttonCancel = findViewById(R.id.buttonCancel_event_add);
        buttonSave = findViewById(R.id.buttonSave_event_add);
        editTextName = findViewById(R.id.editTextName_event_add);
        editTextDate = findViewById(R.id.editTextDate_event_add);
        dbHelper = new DBHelper(this);
        editTextName.requestFocus();
        getSupportActionBar().hide();
    }
}
