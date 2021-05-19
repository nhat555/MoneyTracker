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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.chooses.ChooseEventActivity;
import com.nhat.moneytracker.controllers.chooses.ChooseSavingsActivity;
import com.nhat.moneytracker.controllers.chooses.ChooseWalletActivity;
import com.nhat.moneytracker.controllers.chooses.TabHostCateActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.dates.ChooseDateModule;
import com.nhat.moneytracker.modules.dates.DateDisplayModule;
import com.nhat.moneytracker.modules.formats.DateFormatModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;
import com.nhat.moneytracker.modules.icons.IconsDrawableModule;
import com.nhat.moneytracker.modules.others.DrawableRightModule;
import com.nhat.moneytracker.sessions.Session;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventRemindTemplate;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventRepeatTemplate;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventSaveTemplate;

import java.text.SimpleDateFormat;

public class AddTransactionActivity extends AppCompatActivity implements IMappingView {
    private Button buttonSave;
    private EditText editTextMoney, editTextCategory, editTextNote, editTextDate, editTextWallet,
            editTextEvent, editTextSaving, editTextRemind;
    private CheckBox checkBoxRepeat, checkBoxFast;
    private ImageButton buttonIconCate, buttonHelpRepeat, buttonHelpFast, buttonCancel;
    private TextView textViewRepeat;
    private DBHelper dbHelper;
    private Session session;
    private DanhMuc danhMuc;
    private ViCaNhan viCaNhan;
    private SuKien suKien;
    private TietKiem tietKiem;
    private String[] REPEAT_ARRAY;
    private static final String WALLET_FROM = "from";
    private static final String DATE = "01/01/2100";
    private static final String KHOANCHI = "khoanchi";
    private static final String SAVINGS_CATEGORY = "Tiết kiệm";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_add);
        init();
        loadIntent();
        loadData();
        eventReturn();
        eventChooseWallet();
        eventChooseCate();
        eventChooseDate();
        eventChooseEvent();
        eventChooseSavings();
        eventClearWallet();
        eventClearSavings();
        eventClearEvent();
        eventClearRemind();
        eventRepeat();
        eventRemind();
        eventFast();
        eventSave();
    }

    private void eventClearRemind() {
        editTextRemind.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                editTextRemind.setText("");
                editTextRemind.setHint(getResources().getString(R.string.remind));
                editTextRemind.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
            }
        };
        DrawableRightModule.eventDrawableRight(editTextRemind, runnable);
    }

    private void eventRemind() {
        editTextRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventRemindTemplate.displayRemind(AddTransactionActivity.this, editTextRemind);
            }
        });
    }

    private void eventRepeat() {
        checkBoxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventRepeatTemplate.eventRepeat(AddTransactionActivity.this, REPEAT_ARRAY, checkBoxRepeat, textViewRepeat);
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
    private void eventClearWallet() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                session.clearWallet();
                viCaNhan = null;
                editTextWallet.setText("");
                editTextWallet.setHint(getResources().getString(R.string.wallet));
                editTextWallet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
            }
        };
        DrawableRightModule.eventDrawableRight(editTextWallet, runnable);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void eventClearSavings() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                session.clearSavings();
                tietKiem = null;
                editTextSaving.setText("");
                editTextSaving.setHint(getResources().getString(R.string.saving));
                editTextSaving.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);

                session.clearCate();
                danhMuc = null;
                buttonIconCate.setImageResource(R.drawable.ic_help_black_24dp);
                editTextCategory.setText("");
                editTextCategory.setHint(getResources().getString(R.string.category));
            }
        };
        DrawableRightModule.eventDrawableRight(editTextSaving, runnable);
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
                String note = editTextNote.getText().toString();
                String money = editTextMoney.getText().toString().replace(",", "");
                String dateStr = editTextDate.getText().toString();
                String remind = editTextRemind.getText().toString();
                String repeat = textViewRepeat.getText().toString();
                java.sql.Date sqlDate = DateFormatModule.getDateSQL(DateDisplayModule.getDateByDisplay(dateStr));
                if(tietKiem != null) {
                    if(danhMuc != null) {
                        EventSaveTemplate.handlingInput(dbHelper, money, note, sqlDate, danhMuc, AddTransactionActivity.this,
                                viCaNhan, tietKiem, suKien, checkBoxFast, session, remind, repeat);
                    } else Toast.makeText(AddTransactionActivity.this, R.string.empty_cate, Toast.LENGTH_SHORT).show();
                }
                else {
                    if(viCaNhan != null && danhMuc != null) {
                        EventSaveTemplate.handlingInput(dbHelper, money, note, sqlDate, danhMuc, AddTransactionActivity.this,
                                viCaNhan, tietKiem, suKien, checkBoxFast, session, remind, repeat);
                    } else Toast.makeText(AddTransactionActivity.this, R.string.empty_cate_wallets, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void eventChooseSavings() {
        editTextSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, ChooseSavingsActivity.class);
                startActivity(intent);
                session.clearWallet();
                session.clearCate();
            }
        });
    }

    private void eventChooseEvent() {
        editTextEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, ChooseEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChooseDate() {
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateModule.chooseDate(AddTransactionActivity.this, editTextDate, false);
            }
        });
    }

    private void eventChooseCate() {
        editTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, TabHostCateActivity.class);
                startActivity(intent);
                session.clearSavings();
            }
        });
        buttonIconCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, TabHostCateActivity.class);
                startActivity(intent);
                session.clearSavings();
            }
        });
    }

    private void eventChooseWallet() {
        editTextWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, ChooseWalletActivity.class);
                intent.putExtra("name", WALLET_FROM);
                startActivity(intent);
                session.clearSavings();
            }
        });
    }

    private void loadIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String idTrans = bundle.getString("idTrans");
            if(!idTrans.isEmpty() && idTrans != null && !idTrans.equals("")) {
                SoGiaoDich soGiaoDich = dbHelper.getByID_SoGiaoDich(idTrans);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                editTextMoney.setText(FormatMoneyModule.formatAmount(soGiaoDich.getSoTien()));
                session.setIDCate(soGiaoDich.getMaDanhMuc());
                session.setIDWallet(soGiaoDich.getMaVi());
                session.setIDEvent(soGiaoDich.getMaSuKien());
                session.setIDSavings(soGiaoDich.getMaTietKiem());
                loadNote(soGiaoDich.getGhiChu());
            }
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

    private void loadData() {
        loadSessionCate();
        loadSessionWallet();
        loadSessionSavings();
        loadSessionEvent();
    }

    private void loadSessionCate() {
        String idCate = session.getIDCate();
        if(idCate != null && !idCate.isEmpty()) {
            try {
                danhMuc = dbHelper.getByID_DanhMuc(idCate);
                int resID = IconsDrawableModule.getResourcesDrawble(AddTransactionActivity.this, danhMuc.getBieuTuong());
                buttonIconCate.setImageResource(resID);
                editTextCategory.setText(danhMuc.getTenDanhMuc());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            danhMuc = null;
            buttonIconCate.setImageResource(R.drawable.ic_help_black_24dp);
            editTextCategory.setText("");
            editTextCategory.setHint(getResources().getString(R.string.category));
        }
    }

    private void loadSessionWallet() {
        String idWallet = session.getIDWallet();
        if(idWallet != null && !idWallet.isEmpty()) {
            try {
                viCaNhan = dbHelper.getByID_ViCaNhan(idWallet);
                editTextWallet.setText(viCaNhan.getTenVi());
                editTextWallet.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_backspace_black_24dp, 0);

                if(danhMuc.getTenDanhMuc().equals(SAVINGS_CATEGORY)) {
                    session.clearCate();
                    loadSessionCate();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(danhMuc != null && !danhMuc.getTenDanhMuc().equals(SAVINGS_CATEGORY)) {
            viCaNhan = dbHelper.getByID_ViCaNhan(danhMuc.getMaVi());
            editTextWallet.setText(viCaNhan.getTenVi());
            editTextWallet.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_backspace_black_24dp, 0);
            session.clearSavings();
        }
        else {
            viCaNhan = null;
            editTextWallet.setText("");
            editTextWallet.setHint(getResources().getString(R.string.wallet));
            editTextWallet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
        }
    }

    private void loadSessionSavings() {
        String idSavings = session.getIDSavings();
        if(idSavings != null && !idSavings.isEmpty()) {
            try {
                DanhMuc danhMuc = dbHelper.getByName_DanhMuc(SAVINGS_CATEGORY);
                session.setIDCate(danhMuc.getMaDanhMuc());
                tietKiem = dbHelper.getByID_TietKiem(idSavings);
                editTextSaving.setText(tietKiem.getTenTietKiem());
                editTextSaving.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_backspace_black_24dp, 0);

                viCaNhan = null;
                editTextWallet.setText("");
                editTextWallet.setHint(getResources().getString(R.string.wallet));
                editTextWallet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
                loadSessionCate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            tietKiem = null;
            editTextSaving.setText("");
            editTextSaving.setHint(getResources().getString(R.string.saving));
            editTextSaving.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.color.colorWhite, 0);
        }
    }

    private void loadSessionEvent() {
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
        else {
            suKien = null;
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
    public void onBackPressed() {
        super.onBackPressed();
        session.clear();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public void init() {
        buttonSave = findViewById(R.id.buttonSave_transaction_add);
        buttonCancel = findViewById(R.id.buttonCancel_transaction_add);
        editTextMoney = findViewById(R.id.editTextMoney_transaction_add);
        editTextCategory = findViewById(R.id.editTextCategory_transaction_add);
        editTextNote = findViewById(R.id.editTextNote_transaction_add);
        editTextDate = findViewById(R.id.editTextDate_transaction_add);
        editTextWallet = findViewById(R.id.editTextWallet_transaction_add);
        editTextEvent = findViewById(R.id.editTextEvent_transaction_add);
        editTextSaving = findViewById(R.id.editTextSaving_transaction_add);
        buttonIconCate = findViewById(R.id.buttonCate_transaction_add);
        editTextRemind = findViewById(R.id.editTextRemind_transaction_add);
        checkBoxRepeat = findViewById(R.id.checkBoxRepeat_transaction_add);
        checkBoxFast = findViewById(R.id.checkBoxFast_transaction_add);
        //buttonHelpRepeat = findViewById(R.id.buttonHelpRepeat_transaction_add);
        //buttonHelpFast = findViewById(R.id.buttonHelpFast_transaction_add);
        textViewRepeat = findViewById(R.id.textViewRepeat_transaction_add);
        dbHelper = new DBHelper(getApplicationContext());
        session = new Session(getApplicationContext());
        session.clear();
        REPEAT_ARRAY = getResources().getStringArray(R.array.repeat_array);
        FormatMoneyModule.formatEditTextMoney(editTextMoney);
        getSupportActionBar().hide();
    }
}
