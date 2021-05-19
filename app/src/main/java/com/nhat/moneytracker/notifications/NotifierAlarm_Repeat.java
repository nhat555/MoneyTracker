package com.nhat.moneytracker.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.controllers.transactions.DetailTransactionActivity;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.entities.SuKien;
import com.nhat.moneytracker.entities.TietKiem;
import com.nhat.moneytracker.entities.ViCaNhan;
import com.nhat.moneytracker.helper.DBHelper;
import com.nhat.moneytracker.modules.checks.CheckPropertyRepeatModule;
import com.nhat.moneytracker.modules.randoms.RandomIDModule;
import com.nhat.moneytracker.templates.transactions.add_transaction.EventSaveTemplate;

import java.sql.Date;

public class NotifierAlarm_Repeat extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String dateNew = intent.getStringExtra("dateNew");

        SoGiaoDich soGiaoDich = dbHelper.getByID_SoGiaoDich(id);

        SoGiaoDich soGiaoDichNew = new SoGiaoDich();
        String idNew = RandomIDModule.getTransID(dbHelper);

        Intent intent1 = new Intent(context, DetailTransactionActivity.class);
        intent1.putExtra("idTrans", idNew);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(DetailTransactionActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01","notification", NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle(title)
                .setSmallIcon(R.drawable.logo2)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo2))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setChannelId("my_channel_01")
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);
    }

    private void saveTransNewRepeat(DBHelper dbHelper, SoGiaoDich soGiaoDich, Date dateNew, Context activity, String idNew, SoGiaoDich soGiaoDichNew) {
        DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDich.getMaDanhMuc());
        ViCaNhan viCaNhan = CheckPropertyRepeatModule.checkWallet(soGiaoDich.getMaVi(), dbHelper);
        TietKiem tietKiem = CheckPropertyRepeatModule.checkSavings(soGiaoDich.getMaTietKiem(), dbHelper);
        SuKien suKien = CheckPropertyRepeatModule.checkEvent(soGiaoDich.getMaSuKien(), dbHelper);
        EventSaveTemplate.handlingSaveTransRepeat(dbHelper, String.valueOf(soGiaoDich.getSoTien()), soGiaoDich.getGhiChu(),
                dateNew, danhMuc, viCaNhan, tietKiem, suKien, "", soGiaoDich.getLapLai(), activity, idNew, soGiaoDichNew);
        soGiaoDich.setLapLai("");
        dbHelper.update_SoGiaoDich(soGiaoDich);
    }
}
