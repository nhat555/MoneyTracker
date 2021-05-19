package com.nhat.moneytracker.controllers.accounts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.SinhVien;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.checks.CheckRegexModule;
import com.nhat.moneytracker.modules.others.ShowHidePasswordModule;

public class SignUpActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextEmail, editTextPassword, editTextName;
    private ImageButton imageButtonCapture, imageButtonChoose, buttonReturn;
    private ImageView imageViewPicture;
    private Button buttonSignUp;
    private static final int CHOOSEN = 1;
    private static final int CAPTURE = 2;
    private  DBHelper dbHelper;
    private Uri avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_signup);
        init();
        eventReturn();
        eventSignUp();
        eventShowPassword();
    }

    private void eventShowPassword() {
        ShowHidePasswordModule.eventShowHidePassword(editTextPassword);
    }

    private void eventSignUp() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaiKhoan taiKhoan = new TaiKhoan();
                SinhVien sinhVien = new SinhVien();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                try {
                    if(CheckEmptyModule.isEmpty(email, password, name)) {
                        if(CheckRegexModule.isEmail(email)) {
                            if(CheckRegexModule.isPassword(password)) {
                                if(CheckRegexModule.isName(name)) {
                                    taiKhoan.setEmail(email);
                                    taiKhoan.setMatKhau(password);
                                    taiKhoan.setStatus(0);
                                    if(dbHelper.insert_TaiKhoan(taiKhoan)) {
                                        sinhVien.setMasv(email);
                                        sinhVien.setTen(name);
                                        sinhVien.setEmail(email);
                                        if(dbHelper.insert_SinhVien(sinhVien)) {
                                            onBackPressed();
                                            Toast.makeText(getApplicationContext(), R.string.success_signup, Toast.LENGTH_SHORT).show();
                                        } else Toast.makeText(getApplicationContext(), R.string.unsuccess_signup, Toast.LENGTH_SHORT).show();
                                    } else Toast.makeText(getApplicationContext(), R.string.email_exist, Toast.LENGTH_SHORT).show();
                                } else Toast.makeText(getApplicationContext(), R.string.regex_name, Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(getApplicationContext(), R.string.regex_password, Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getApplicationContext(), R.string.regex_email, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        if(contentUri != null) {
            String [] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return "none";
    }

    private void eventOpenCapture() {
        imageButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, CAPTURE);
            }
        });
    }

    private void eventOpenGallery() {
        imageButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, CHOOSEN);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSEN && resultCode == RESULT_OK && data != null) {
            try {
                avatar = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), avatar);
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,600, 600, true);
                imageViewPicture.setImageBitmap(bitmap2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(resultCode == CAPTURE && requestCode == RESULT_OK && data != null) {
            try {
                Uri selectedImg = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImg);
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,600, 600, true);
                imageViewPicture.setImageBitmap(bitmap2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init() {
        editTextEmail = findViewById(R.id.editTextEmail_signup);
        editTextPassword = findViewById(R.id.editTextPassword_signup);
        editTextName = findViewById(R.id.editTextName_signup);
        buttonSignUp = findViewById(R.id.buttonSignUp_signup);
        buttonReturn = findViewById(R.id.buttonReturn_signup);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
