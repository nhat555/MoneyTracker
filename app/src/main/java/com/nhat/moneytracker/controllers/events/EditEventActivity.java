package com.nhat.moneytracker.controllers.events;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;

public class EditEventActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private EditText editTextName, editTextDate;
    private ImageButton buttonCancel;
    private DBHelper dbHelper;
    private SuKien suKien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        init();
        loadData();
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
                    if(suKien.getTenSuKien().equals(name)) {
                        handlingSave(name, sqlDate);
                    }
                    else {
                        try {
                            dbHelper.getByName_SuKien(name);
                            Toast.makeText(getApplicationContext(), R.string.name_exist_event, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            handlingSave(name, sqlDate);
                        }
                    }
                } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlingSave(final String name, final java.sql.Date date) {
        suKien.setTenSuKien(name);
        suKien.setNgayKetThuc(date);
        dbHelper.update_SuKien(suKien);
        onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.success_event_edit, Toast.LENGTH_SHORT).show();
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(EditEventActivity.this, editTextDate, true);
            }
        });
    }

    private void loadData() {
        String idEvent = getIntent().getExtras().getString("idEvent");
        suKien = dbHelper.getByID_SuKien(idEvent);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        editTextName.setText(suKien.getTenSuKien());
        editTextDate.setText(DateDisplayModule.displayDate(formatter.format(suKien.getNgayKetThuc())));
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
        buttonCancel = findViewById(R.id.buttonCancel_event_edit);
        buttonSave = findViewById(R.id.buttonSave_event_edit);
        editTextName = findViewById(R.id.editTextName_event_edit);
        editTextDate = findViewById(R.id.editTextDate_event_edit);
        dbHelper = new DBHelper(this);
        editTextName.requestFocus();
        getSupportActionBar().hide();
    }
}
