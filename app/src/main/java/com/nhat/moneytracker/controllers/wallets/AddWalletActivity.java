package com.nhat.moneytracker.controllers.wallets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;

import java.util.ArrayList;

public class AddWalletActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextName, editTextMoney;
    private Button buttonSave;
    private ImageButton buttonCancel;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_add);
        init();
        eventCancel();
        eventSave();
    }

    private void eventSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String money = editTextMoney.getText().toString().replace(",", "");
                if(CheckEmptyModule.isEmpty(name, money, money)) {
                    if(money.length() > 3 && Double.parseDouble(money) > 0 && money.substring(money.length() - 3).equals("000")) {
                        try {
                            dbHelper.getByName_ViCaNhan(name);
                            Toast.makeText(getApplicationContext(), R.string.name_exist_wallet_add, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            ArrayList<ViCaNhan> list = dbHelper.getAll_ViCaNhan();
                            if(list.size() < 5) {
                                handlingSave(name, money);
                            } else Toast.makeText(getApplicationContext(), R.string.full_wallet, Toast.LENGTH_SHORT).show();
                        }
                    } else Toast.makeText(getApplicationContext(), R.string.invalid_money, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlingSave(final String name, final String money) {
        ViCaNhan viCaNhan = new ViCaNhan();
        viCaNhan.setMaVi(RandomIDModule.getWalletID(dbHelper));
        viCaNhan.setTenVi(name);
        viCaNhan.setSoTien(Double.parseDouble(money));
        dbHelper.insert_ViCaNhan(viCaNhan);
        onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.success_wallet_add, Toast.LENGTH_SHORT).show();
    }

    private void eventCancel() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void init() {
        editTextName = findViewById(R.id.editTextName_wallet_add);
        editTextMoney = findViewById(R.id.editTextMoney_wallet_add);
        buttonCancel = findViewById(R.id.buttonCancel_wallet_add);
        buttonSave = findViewById(R.id.buttonSave_wallet_add);
        dbHelper = new DBHelper(this);
        editTextName.requestFocus();
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        getSupportActionBar().hide();
    }
}
