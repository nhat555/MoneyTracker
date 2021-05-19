package com.nhat.moneytracker.templates.transactions.add_transaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nhat.moneytracker.R;

public class EventRepeatTemplate {

    public static void eventRepeat(Context context, final String[] REPEAT_ARRAY, CheckBox checkBox, final TextView textView) {
        if(checkBox.isChecked()) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.transaction_repeat_dialog);
            ListView listViewFilter = dialog.findViewById(R.id.listViewRepeat_repeat_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, REPEAT_ARRAY);
            listViewFilter.setAdapter(adapter);
            listViewFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    textView.setText("(" + REPEAT_ARRAY[position] + ")");
                    dialog.dismiss();
                }
            });
        }
        else {
            textView.setText("");
        }
    }
}
