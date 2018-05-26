package com.example.toshiba.easypeasybus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {
        APBAuth vg = APBAuth.getInstance();
        Intent intent = new Intent(context, Main.class);
        PendingIntent pi = PendingIntent.getActivity(context, vg.getAlarmIndex(), intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_airport_shuttle_black_24dp)
                .setContentTitle("Auto Peasy Bus le recuerda:")
                .setContentText("Su bus sale dentro de 5 minutos");
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(vg.getAlarmIndex(), mBuilder.build());
    }
}