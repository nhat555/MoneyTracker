package com.nhat.moneytracker.controllers.wallets;

import android.annotation.SuppressLint;
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
import com.nhat.moneytracker.controllers.chooses.ChooseWalletActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.dates.ChooseDateModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.icons.IconsDrawableModule;
import com.nhat.moneytracker.sessions.Session;
import com.nhat.moneytracker.templates.wallets.EventTransferMoneyTemplate;
import com.google.gson.Gson;

public class TransferWalletActivity extends AppCompatActivity implements IMappingView {
    private EditText editTextMoney, editTextWallet, editTextCategory, editTextNote, editTextWalletReceive, editTextDate;
    private Button buttonSave;
    private ImageButton buttonIconCate, buttonCancel;
    private DBHelper dbHelper;
    private Session session;
    private DanhMuc danhMuc;
    private ViCaNhan viCaNhan;
    private ViCaNhan viCaNhanReceive;
    private static final String WALLET_FROM_TRANSFER = "from_transfers";
    private static final String TRANSFERS_CATEGORY = "Chuyển tiền";
    private SoGiaoDich soGiaoDich;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_transfers);
        init();
        loadIntent();
        loadData();
        eventReturn();
        eventChooseWallet();
        eventChooseDate();
        eventSave();
    }

    private void eventSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString();
                String money = editTextMoney.getText().toString().replace(",", "");
                String dateStr = editTextDate.getText().toString();
                java.sql.Date sqlDate = DateFormatModule.getDateSQL(dateStr);
                if(viCaNhan != null && viCaNhanReceive != null) {
                    if(danhMuc != null) {
                        if(viCaNhan.getSoTien() >= Double.parseDouble(money)) {
                            EventTransferMoneyTemplate.handlingSaveTrans(money, note, sqlDate, dbHelper, danhMuc,
                                    viCaNhan, viCaNhanReceive, TransferWalletActivity.this, session, soGiaoDich);
                        } else Toast.makeText(getApplicationContext(), R.string.invalid_money_transfers, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), R.string.chooseCate, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.chooseWallet, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(TransferWalletActivity.this, editTextDate, false);
            }
        });
    }

    private void eventChooseWallet() {
        editTextWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseWalletActivity.class);
                intent.putExtra("name", WALLET_FROM_TRANSFER);
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
    private void loadIntent() {
        Gson gson = new Gson();
        soGiaoDich = gson.fromJson(getIntent().getStringExtra("myJson"), SoGiaoDich.class);
        viCaNhanReceive = dbHelper.getByID_ViCaNhan(soGiaoDich.getMaVi());
        editTextWalletReceive.setText(viCaNhanReceive.getTenVi());
        editTextMoney.setText((int) (soGiaoDich.getSoTien() - viCaNhanReceive.getSoTien()) + "");
        editTextNote.setText(getResources().getString(R.string.note_wallet_transfer) + " " + viCaNhanReceive.getTenVi());
        session.setIDWalletReceive(viCaNhanReceive.getMaVi());
        loadCate();
    }

    private void loadCate() {
        danhMuc = dbHelper.getByName_DanhMuc(TRANSFERS_CATEGORY);
        int resID = IconsDrawableModule.getResourcesDrawble(getApplicationContext(), danhMuc.getBieuTuong());
        buttonIconCate.setImageResource(resID);
        editTextCategory.setText(danhMuc.getTenDanhMuc());
    }

    private void loadData() {
        String idWallet = session.getIDWallet();
        if(idWallet != null && !idWallet.isEmpty()) {
            try {
                viCaNhan = dbHelper.getByID_ViCaNhan(idWallet);
                editTextWallet.setText(viCaNhan.getTenVi());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            editTextWallet.setText("");
            editTextWallet.setHint(getResources().getString(R.string.wallet));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public void init() {
        editTextWallet = findViewById(R.id.editTextWallet_wallet_transfers);
        editTextMoney = findViewById(R.id.editTextMoney_wallet_transfers);
        editTextCategory = findViewById(R.id.editTextCategory_wallet_transfers);
        editTextNote = findViewById(R.id.editTextNote_wallet_transfers);
        editTextWalletReceive = findViewById(R.id.editTextWalletReceive_wallet_transfers);
        editTextDate = findViewById(R.id.editTextDate_wallet_transfers);
        buttonCancel = findViewById(R.id.buttonCancel_wallet_transfers);
        buttonSave = findViewById(R.id.buttonSave_wallet_transfers);
        buttonIconCate = findViewById(R.id.buttonCate_wallet_transfers);
        dbHelper = new DBHelper(this);
        session = new Session(this);
        session.clear();
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        getSupportActionBar().hide();
    }
}
