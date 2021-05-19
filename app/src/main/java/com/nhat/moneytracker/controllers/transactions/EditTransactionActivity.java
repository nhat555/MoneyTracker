package com.nhat.moneytracker.controllers.transactions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.chooses.ChooseEventActivity;
import com.nhat.moneytracker.controllers.chooses.ChooseSavingsActivity;
import com.nhat.moneytracker.controllers.chooses.TabHostCateActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.checks.CheckEmptyModule;
import com.nhat.moneytracker.modules.dates.ChooseDateModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.icons.IconsDrawableModule;
import com.nhat.moneytracker.modules.others.DrawableRightModule;
import com.nhat.moneytracker.sessions.Session;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventRemindTemplate;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventRepeatTemplate;
import com.nhat.moneytracker.templates.transactions.edit_transaction.EventUpdateTemplate;

import java.text.SimpleDateFormat;

public class EditTransactionActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private CheckBox checkBoxRepeat, checkBoxFast;
    private ImageButton buttonCate, buttonCancel, buttonHelpRepeat, buttonHelpFast;
    private TextView textViewRepeat;
    private EditText editTextMoney, editTextCategory, editTextNote, editTextDate, editTextWallet, editTextEvent, editTextSavings,
            editTextRemind;
    private DBHelper dbHelper;
    private SoGiaoDich soGiaoDich;
    private Session session;
    private DanhMuc danhMuc;
    private SuKien suKien;
    private String[] REPEAT_ARRAY;
    private static final String DOANHTHU = "doanhthu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_edit);
        init();
        loadData();
        eventReturn();
        eventChooseCate();
        eventChooseDate();
        eventChooseEvent();
        eventChooseSavings();
        eventClearEvent();
        eventRepeat();
        eventRemind();
        eventFast();
        eventSave();
    }

    private void eventRemind() {
        editTextRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventRemindTemplate.displayRemind(EditTransactionActivity.this, editTextRemind);
            }
        });
    }

    private void eventRepeat() {
        checkBoxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventRepeatTemplate.eventRepeat(EditTransactionActivity.this, REPEAT_ARRAY, checkBoxRepeat, textViewRepeat);
                checkBoxFast.setChecked(false);
            }
        });
    }

    private void eventFast() {
        checkBoxFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxRepeat.setChecked(false);
                textViewRepeat.setText("");
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void eventClearEvent() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                session.clearEvent();
                suKien = null;
                editTextEvent.setText("");
                editTextEvent.setHint(getResources().getString(R.string.event));
                editTextEvent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
            }
        };
        DrawableRightModule.eventDrawableRight(editTextEvent, runnable);
    }

    private void eventSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViCaNhan viCaNhan = EventUpdateTemplate.handlingUpdateWallet(soGiaoDich, dbHelper, DOANHTHU);
                String money = editTextMoney.getText().toString().replace(",", "");
                String note = editTextNote.getText().toString();
                String remind = editTextRemind.getText().toString();
                String repeat = textViewRepeat.getText().toString();
                String dateStr = editTextDate.getText().toString();
                java.sql.Date sqlDate = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
                if(CheckEmptyModule.isEmpty(money, dateStr, dateStr)) {
                    if(money.length() > 3 && Double.parseDouble(money) > 0 && money.substring(money.length() - 3).equals("000")) {
                        if(!danhMuc.getLoaiDanhMuc().equals(DOANHTHU) && Double.parseDouble(money) > viCaNhan.getSoTien()) {
                            Toast.makeText(getApplicationContext(), R.string.fast_money_error, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            EventUpdateTemplate.handlingSave(Double.parseDouble(money), sqlDate, note, soGiaoDich, danhMuc, suKien,
                                    EditTransactionActivity.this, dbHelper, DOANHTHU, remind, repeat, checkBoxFast);
                        }
                    } else Toast.makeText(getApplicationContext(), R.string.invalid_money, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), R.string.empty_money, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventChooseEvent() {
        editTextEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTransactionActivity.this, ChooseEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChooseSavings() {
        editTextSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTransactionActivity.this, ChooseSavingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChooseCate() {
        editTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTransactionActivity.this, TabHostCateActivity.class);
                startActivity(intent);
            }
        });
        buttonCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTransactionActivity.this, TabHostCateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(EditTransactionActivity.this, editTextDate, false);
            }
        });
    }

    private void loadData() {
        String idTrans = getIntent().getExtras().getString("idTrans");
        soGiaoDich = dbHelper.getByID_SoGiaoDich(idTrans);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        editTextMoney.setText(FormatMoneyModule.formatAmount(soGiaoDich.getSoTien()));
        editTextDate.setText(DateDisplayModule.displayDate(formatter.format(soGiaoDich.getNgayGiaoDich())));
        loadSessionCate(soGiaoDich.getMaDanhMuc());
        loadSessionEvent(soGiaoDich.getMaSuKien());
        loadSavings(soGiaoDich.getMaTietKiem());
        loadWallet(soGiaoDich.getMaVi());
        loadNote(soGiaoDich.getGhiChu());
        loadRepeat(soGiaoDich.getLapLai());
        loadStatus(soGiaoDich.getStatus());
        loadRemind();
    }

    private void loadStatus(int status) {
        if(status == 1) {
            checkBoxFast.setChecked(true);
        }
        else {
            checkBoxFast.setChecked(false);
        }
    }

    private void loadRemind() {
        editTextRemind.setText("");
        editTextRemind.setHint(getResources().getString(R.string.remind));
    }

    private void loadRepeat(String lapLai) {
        if(lapLai.equals("") || lapLai.isEmpty()) {
            checkBoxRepeat.setChecked(false);
            textViewRepeat.setText("");
        }
        else {
            checkBoxRepeat.setChecked(true);
            textViewRepeat.setText(lapLai);
        }
    }

    private void loadWallet(String idWallet) {
        if(idWallet.isEmpty() || idWallet.equals("null")) {
            editTextWallet.setText("");
            editTextWallet.setHint(getResources().getString(R.string.wallet));
        }
        else {
            ViCaNhan viCaNhan = dbHelper.getByID_ViCaNhan(idWallet);
            editTextWallet.setText(viCaNhan.getTenVi());
        }
    }

    private void loadSavings(String idSavings) {
        if(idSavings.isEmpty() || idSavings.equals("null")){
            editTextSavings.setText("");
            editTextSavings.setHint(getResources().getString(R.string.saving));
        }
        else {
            TietKiem tietKiem = dbHelper.getByID_TietKiem(idSavings);
            editTextSavings.setText(tietKiem.getTenTietKiem());
        }
    }

    private void loadNote(String note) {
        if(note.equals("") || note.isEmpty()) {
            editTextNote.setText("");
            editTextNote.setHint(getResources().getString(R.string.note));
        }
        else {
            editTextNote.setText(note);
        }
    }

    private void loadSessionCate(String id) {
        String idCate = session.getIDCate();
        if(idCate != null && !idCate.isEmpty()) {
            try {
                danhMuc = dbHelper.getByID_DanhMuc(idCate);
                int resID = IconsDrawableModule.getResourcesDrawble(EditTransactionActivity.this, danhMuc.getBieuTuong());
                buttonCate.setImageResource(resID);
                editTextCategory.setText(danhMuc.getTenDanhMuc());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            danhMuc = dbHelper.getByID_DanhMuc(id);
            buttonCate.setImageResource(IconsDrawableModule.getResourcesDrawble(getApplicationContext(), danhMuc.getBieuTuong()));
            editTextCategory.setText(danhMuc.getTenDanhMuc());
        }
    }

    private void loadSessionEvent(String id) {
        String idEvent = session.getIDEvent();
        if(idEvent != null && !idEvent.isEmpty()) {
            try {
                suKien = dbHelper.getByID_SuKien(idEvent);
                editTextEvent.setText(suKien.getTenSuKien());
                editTextEvent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_backspace_black_24dp, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!id.equals("null")){
            suKien = dbHelper.getByID_SuKien(id);
            editTextEvent.setText(suKien.getTenSuKien());
            editTextEvent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_backspace_black_24dp, 0);
        }
        else {
            editTextEvent.setText("");
            editTextEvent.setHint(getResources().getString(R.string.event));
            editTextEvent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
        }
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
    protected void onRestart() {
        super.onRestart();
        loadSessionCate(soGiaoDich.getMaDanhMuc());
        loadSessionEvent(soGiaoDich.getMaSuKien());
    }

    @Override
    public void init() {
        buttonCancel = findViewById(R.id.buttonCancel_transaction_edit);
        buttonSave = findViewById(R.id.buttonSave_transaction_edit);
        buttonCate = findViewById(R.id.buttonCate_transaction_edit);
        editTextMoney = findViewById(R.id.editTextMoney_transaction_edit);
        editTextCategory = findViewById(R.id.editTextCategory_transaction_edit);
        editTextNote = findViewById(R.id.editTextNote_transaction_edit);
        editTextDate= findViewById(R.id.editTextDate_transaction_edit);
        editTextWallet = findViewById(R.id.editTextWallet_transaction_edit);
        editTextEvent = findViewById(R.id.editTextEvent_transaction_edit);
        editTextSavings = findViewById(R.id.editTextSaving_transaction_edit);
        editTextRemind = findViewById(R.id.editTextRemind_transaction_edit);
        checkBoxRepeat = findViewById(R.id.checkBoxRepeat_transaction_edit);
        checkBoxFast = findViewById(R.id.checkBoxFast_transaction_edit);
        //buttonHelpRepeat = findViewById(R.id.buttonHelpRepeat_transaction_edit);
        //buttonHelpFast = findViewById(R.id.buttonHelpFast_transaction_edit);
        textViewRepeat = findViewById(R.id.textViewRepeat_transaction_edit);
        dbHelper = new DBHelper(this);
        session = new Session(this);
        session.clear();
        REPEAT_ARRAY = getResources().getStringArray(R.array.repeat_array);
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        getSupportActionBar().hide();
    }
}
