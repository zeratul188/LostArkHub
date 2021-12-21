package com.lostark.lostarkapplication.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.lostark.lostarkapplication.NotificationHelper;

public class IslandAlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String content = intent.getStringExtra("content");
        IslandNotificationHelper notificationHelper = new IslandNotificationHelper(context);
        //notificationHelper.setNoti_content(content);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(2, nb.build());
    }
}
