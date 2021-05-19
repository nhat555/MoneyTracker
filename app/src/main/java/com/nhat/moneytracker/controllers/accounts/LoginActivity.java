package com.nhat.moneytracker.controllers.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.mains.HomeActivity;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.others.ShowHidePasswordModule;

public class LoginActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonForgotPass, buttonSignUp;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_login);
        init();
        eventSignUp();
        eventLogin();
        eventForgotPassword();
        eventShowPassword();
    }

    private void eventShowPassword() {
        ShowHidePasswordModule.eventShowHidePassword(editTextPassword);
    }

    private void eventForgotPassword() {
        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventLogin() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                try {
                    if(CheckEmptyModule.isEmpty(email, password, password)) {
                        TaiKhoan taikhoan = dbHelper.getByID_TaiKhoan(email);
                        if(taikhoan.getMatKhau().equals(password)) {
                            taikhoan.setStatus(1);
                            dbHelper.update_TaiKhoan(taikhoan);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else Toast.makeText(getApplicationContext(), R.string.password_incorrect, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.email_incorrect, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void eventSignUp() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void init() {
        editTextEmail = findViewById(R.id.editTextEmail_login);
        editTextPassword = findViewById(R.id.editTextPassword_login);
        buttonLogin = findViewById(R.id.buttonLogin_login);
        buttonForgotPass = findViewById(R.id.buttonForgotPass_login);
        buttonSignUp = findViewById(R.id.buttonSignUp_login);
        dbHelper = new DBHelper(this);
        editTextEmail.requestFocus();
        getSupportActionBar().hide();
    }
}
