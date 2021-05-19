package com.nhat.moneytracker.controllers.budgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.ChiTietNganSach;
import com.nhat.moneytracker.entities.NganSach;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.alerts.AlertConfirmModule;
import com.nhat.moneytracker.modules.budgets.MoneyBudgetModule;
import com.nhat.moneytracker.modules.dates.DateBetweenModule;
import com.nhat.moneytracker.modules.formats.FormatMoneyModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailBudgetsActivity extends AppCompatActivity implements IMappingView{
    private Button buttonSeeTrans;
    private ImageButton buttonCancel, buttonEdit, buttonDelete;
    private TextView editTextDate, editTextMoney, money_rest,moneyEveryDate, expectedmoneyspent, actualmoneyspending, editTextDayRest,excessiveamount;
    private DBHelper dbHelper;
    private NganSach nganSach;
    private String idBudgets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_detail);
        init();
        loadData();
        eventReturn();
        eventEdit();
        eventDelete();
        eventdetailtrans();
    }

    private void eventdetailtrans() {
        buttonSeeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBudgetsActivity.this, SeeTransBudgetsActivity.class);
                intent.putExtra("idBudgets", idBudgets);
                startActivity(intent);
            }
        });
    }

    private void eventDelete() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        deleteDetail();
                        dbHelper.delete_NganSach(nganSach);
                        Toast.makeText(getApplicationContext(), R.string.delete_budgets_success, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                };
                AlertConfirmModule.alertDialogConfirm(DetailBudgetsActivity.this, R.string.mes_delete_budgets, runnable);
            }
        });
    }

    private void deleteDetail() {
        ArrayList<ChiTietNganSach> list = dbHelper.getByIDBudget_ChiTietNganSach(nganSach.getMaNganSach());
        for (int i = 0; i < list.size(); i++) {
            dbHelper.delete_ChiTietNganSach(list.get(i));
        }
    }

    private void eventEdit() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBudgetsActivity.this, EditBudgetsActivity.class);
                intent.putExtra("idBudgets", idBudgets);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        idBudgets = getIntent().getExtras().getString("idBudgets");
        if(!idBudgets.isEmpty() && idBudgets != null) {
            nganSach = dbHelper.getByID_NganSach(idBudgets);
            editTextMoney.setText(FormatMoneyModule.formatAmount(nganSach.getSoTien()) + " VND");
            editTextMoney.setTextColor(getResources().getColor(R.color.colorBlue));
            editTextMoney.setCompoundDrawablesWithIntrinsicBounds(R.drawable.money_goal, 0, 0, 0);
            editTextDate.setText(formatter.format(nganSach.getNgayBatDau()) + " - " + formatter.format(nganSach.getNgayKetThuc()));

            int totalDate= DateBetweenModule.daysBetween(nganSach.getNgayKetThuc(), nganSach.getNgayBatDau());
            int daterest = DateBetweenModule.daysBetween(nganSach.getNgayKetThuc(), new Date(Calendar.getInstance().getTime().getTime()));
            int datespending = totalDate - daterest;
            String moneyrest = FormatMoneyModule.formatAmount(MoneyBudgetModule.getMoneyRestBudget(dbHelper, nganSach));
            int totalMoneyExpenses = (int) MoneyBudgetModule.getTotalMoneyExpenses(dbHelper, idBudgets);

            //so tien nen chi moi ngay = tổng số tiền còn lại chia số ngày còn lai
            int money = (int) (MoneyBudgetModule.getMoneyRestBudget(dbHelper, nganSach) / daterest);
            //so tien thuc te chi = số tiền da chi chia cho so ngay da chi
            int actualmoneyspent = (totalMoneyExpenses / datespending);
            //so tien du kien chi tieu = so tien thuc te chi * tong so ngay
            int expectMoneySpent = actualmoneyspent * totalDate;

            int tongtien=0;
            ArrayList<ChiTietNganSach> listbudget = dbHelper.getByIDBudget_ChiTietNganSach(nganSach.getMaNganSach());
            for (int i1 = 0; i1 < listbudget.size(); i1++) {
                SoGiaoDich giaoDich = dbHelper.getByID_SoGiaoDich(listbudget.get(i1).getMaGiaoDich());
                int tien = (int) giaoDich.getSoTien();
                tongtien += tien;
            }
            if(nganSach.getSoTien()<tongtien){
                excessiveamount.setText(moneyrest+"VND");
                money_rest.setText("0");
                moneyEveryDate.setText("0");
                editTextDayRest.setText("Còn lại " + daterest + " ngày");
                actualmoneyspending.setText(FormatMoneyModule.formatAmount(actualmoneyspent) + " VND");
                expectedmoneyspent.setText(FormatMoneyModule.formatAmount(expectMoneySpent) + " VND");
            }
            else{
                excessiveamount.setText("0");
                money_rest.setText(moneyrest+"VND");
                editTextDayRest.setText("Còn lại " + daterest + " ngày");
                moneyEveryDate.setText(FormatMoneyModule.formatAmount(money) + " VND");
                actualmoneyspending.setText(FormatMoneyModule.formatAmount(actualmoneyspent) + " VND");
                expectedmoneyspent.setText(FormatMoneyModule.formatAmount(expectMoneySpent) + " VND");
            }
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
        Intent intent = new Intent(DetailBudgetsActivity.this, HomeBudgetActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public void init() {
        buttonCancel = findViewById(R.id.buttonCancel_Budgets_detail);
        buttonEdit = findViewById(R.id.buttonEdit_Budgets_detail);
        buttonDelete = findViewById(R.id.buttonDelete_Budgets_detail);
        buttonSeeTrans = findViewById(R.id.buttonSeeTrans_budgets_detail);
        editTextMoney = findViewById(R.id.editTextMoney_Budgets_detail);
        editTextDate = findViewById(R.id.editTextDate_Budgets_detail);
        editTextDayRest = findViewById(R.id.editTextDayRest_Budgets_detail);
        moneyEveryDate = findViewById(R.id.moneyEveryDate);
        money_rest=findViewById(R.id.money_rest);
        excessiveamount=findViewById(R.id.excessiveamount);
        expectedmoneyspent = findViewById(R.id.Expectedmoneyspent);
        actualmoneyspending = findViewById(R.id.actualspending);
        dbHelper = new DBHelper(this);
        getSupportActionBar().hide();
    }
}
