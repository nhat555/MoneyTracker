package com.nhat.moneytracker.controllers.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.checks.CheckRegexModule;
import com.nhat.moneytracker.modules.others.ShowHidePasswordModule;

public class ChangePassActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextPasswordOld, editTextPasswordNew;
    private Button buttonSave, buttonForgotPass;
    private ImageButton buttonReturn;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_pass_change);
        init();
        eventReturn();
        eventForgotPassword();
        eventSaveInfo();
        eventShowPassword();
    }

    private void eventShowPassword() {
        ShowHidePasswordModule.eventShowHidePassword(editTextPasswordOld);
        ShowHidePasswordModule.eventShowHidePassword(editTextPasswordNew);
    }

    private void eventSaveInfo() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass_old = editTextPasswordOld.getText().toString();
                final String pass_new = editTextPasswordNew.getText().toString();
                final TaiKhoan taiKhoan = dbHelper.getByCode_TaiKhoan(1);
                if(CheckEmptyModule.isEmpty(pass_old, pass_old, pass_old)) {
                    if(taiKhoan.getMatKhau().equals(pass_old)) {
                        if(CheckEmptyModule.isEmpty(pass_new, pass_new, pass_new)) {
                            if(CheckRegexModule.isPassword(pass_new)) {
                                taiKhoan.setMatKhau(pass_new);
                                dbHelper.update_TaiKhoan(taiKhoan);
                                Toast.makeText(getApplicationContext(), R.string.change_pass_success, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else Toast.makeText(getApplicationContext(), R.string.regex_password, Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getApplicationContext(), R.string.change_pass_empty_new, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), R.string.password_incorrect, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.change_pass_empty_old, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventForgotPassword() {
        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassActivity.this, ForgotPassActivity.class);
                startActivity(intent);
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

    @Override
    public void init() {
        editTextPasswordOld = findViewById(R.id.editTextPasswordOld_pass_change);
        editTextPasswordNew = findViewById(R.id.editTextPasswordNew_pass_change);
        buttonSave = findViewById(R.id.buttonSave_pass_change);
        buttonForgotPass = findViewById(R.id.buttonForgotPass_pass_change);
        buttonReturn = findViewById(R.id.buttonReturn_pass_change);
        dbHelper = new DBHelper(this);
        editTextPasswordOld.requestFocus();
        getSupportActionBar().hide();
    }
}
