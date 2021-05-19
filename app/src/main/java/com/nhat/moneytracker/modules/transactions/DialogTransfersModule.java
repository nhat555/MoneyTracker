package com.nhat.moneytracker.modules.transactions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.wallets.TransferWalletActivity;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.google.gson.Gson;

public class DialogTransfersModule {

    public static void displayDialogTransfers(final Activity context, final Runnable runnable, SoGiaoDich soGiaoDich) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.wallet_transfers_dialog);
        Button buttonTransfers = dialog.findViewById(R.id.buttonTransfers_wallet_transfers_dialog);
        Button buttonChangeWallet = dialog.findViewById(R.id.buttonChangeWallet_wallet_transfers_dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        eventTransfers(buttonTransfers, dialog, soGiaoDich, context);
        eventChangeWallet(buttonChangeWallet, runnable, dialog);
    }

    private static void eventChangeWallet(final Button button, final Runnable runnable, final Dialog dialog) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
                dialog.dismiss();
            }
        });
    }

    private static void eventTransfers(final Button button, final Dialog dialog, final SoGiaoDich soGiaoDich, final Activity context) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransferWalletActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(soGiaoDich);
                intent.putExtra("myJson", myJson);
                context.onBackPressed();
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
