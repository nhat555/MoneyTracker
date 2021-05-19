package com.nhat.moneytracker.controllers.accounts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.emails.JavaMailAPI;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;

public class ForgotPassActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextEmail;
    private Button buttonSave;
    private ImageButton buttonReturn;
    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_forgot_email);
        activity = this;
        init();
        eventReturn();
        eventSendEmail();
    }

    private void eventSendEmail() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                try {
                    if(CheckEmptyModule.isEmpty(email, email, email)) {
                        String codeRandom = String.valueOf(RandomIDModule.getRandomIDSendEmail());
                        TaiKhoan taikhoan = dbHelper.getByID_TaiKhoan(email);
                        taikhoan.setMatKhau(codeRandom);
                        dbHelper.update_TaiKhoan(taikhoan);
                        hideKeyBoard();
                        sendEmail(email, codeRandom);
                    } else Toast.makeText(getApplicationContext(), R.string.empty_email, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.email_incorrect, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String email, String codeRandom) {
        String subject = getResources().getString(R.string.subject);
        String message = getResources().getString(R.string.message) + " " + codeRandom;
        JavaMailAPI javaMailAPI = new JavaMailAPI(ForgotPassActivity.this, email, subject, message);
        javaMailAPI.execute();
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
        editTextEmail = findViewById(R.id.editTextEmail_forgot_email);
        buttonSave = findViewById(R.id.buttonSave_forgot_email);
        buttonReturn = findViewById(R.id.buttonReturn_forgot_email);
        dbHelper = new DBHelper(this);
        editTextEmail.requestFocus();
        getSupportActionBar().hide();
    }
}
