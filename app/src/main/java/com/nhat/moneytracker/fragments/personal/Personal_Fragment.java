package com.nhat.moneytracker.fragments.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.accounts.ChangePassActivity;
import com.nhat.moneytracker.controllers.accounts.EditInfoActivity;
import com.nhat.moneytracker.controllers.accounts.LoginActivity;
import com.nhat.moneytracker.controllers.categories.HomeCateActivity;
import com.nhat.moneytracker.controllers.wallets.HomeWalletActivity;
import com.nhat.moneytracker.entities.SinhVien;
import com.nhat.moneytracker.entities.TaiKhoan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.interfaces.IMappingView;
import com.nhat.moneytracker.modules.alerts.AlertConfirmModule;

public class Personal_Fragment extends Fragment implements IMappingView {
    private ImageButton buttonLogout;
    private Button buttonMyWallet, buttonMyCate, buttonChangePass, buttonEditInfo;
    private ImageView imgPicture;
    private TextView textViewName, textViewEmail;
    private DBHelper dbHelper;
    private View root;
    private TaiKhoan taiKhoan;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.account_personal, container, false);
        init();
        if(getUserVisibleHint()) loadInfo();
        eventLogout();
        eventChangePassword();
        eventEditInfo();
        eventMyWallet();
        eventMyCate();
        return root;
    }

    private void eventMyCate() {
        buttonMyCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeCateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventMyWallet() {
        buttonMyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventEditInfo() {
        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), EditInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChangePassword() {
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), ChangePassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventLogout() {
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        taiKhoan.setStatus(0);
                        dbHelper.update_TaiKhoan(taiKhoan);
                        Intent intent = new Intent(root.getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                };
                AlertConfirmModule.alertDialogConfirm(getActivity(), R.string.mes_logout, runnable);
            }
        });
    }

    private void loadInfo() {
        try {
            taiKhoan = dbHelper.getByCode_TaiKhoan(1);
            SinhVien sinhVien = dbHelper.getByID_SinhVien(taiKhoan.getEmail());
            textViewName.setText(sinhVien.getTen());
            textViewEmail.setText(sinhVien.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()) {
            loadInfo();
        }
    }

    @Override
    public void init() {
        buttonLogout = root.findViewById(R.id.buttonLogout_personal);
        buttonMyWallet = root.findViewById(R.id.buttonMyWallet_personal);
        buttonMyCate = root.findViewById(R.id.buttonMyCate_personal);
        buttonChangePass = root.findViewById(R.id.buttonChangePass_personal);
        buttonEditInfo = root.findViewById(R.id.buttonEditInfo_personal);
        imgPicture = root.findViewById(R.id.imgPicture_personal);
        textViewName = root.findViewById(R.id.textViewName_personal);
        textViewEmail = root.findViewById(R.id.textViewEmail_personal);
        dbHelper = new DBHelper(root.getContext());
    }
}
