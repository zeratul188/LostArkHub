package com.lostark.lostarkapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelNm = "channelNm";

    private ChracterListDBAdapter chracterListDBAdapter;

   /* private String noti_content = "";

    public void setNoti_content(String noti_content) {
        this.noti_content = noti_content;
    }*/

    private NotificationManager notiManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channelID, channelNm, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_on_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager() {
        if (notiManager == null) notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notiManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        chracterListDBAdapter = new ChracterListDBAdapter(this);
        String content = "";
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        boolean flag;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            flag = false;
            int rowID = cursor.getInt(0);
            String name = cursor.getString(1);
            boolean isAlarm = Boolean.parseBoolean(cursor.getString(4));
            ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(this, "CHRACTER"+rowID);
            chracterDBAdapter.open();
            Cursor chracterCursor = chracterDBAdapter.fetchAllData();
            chracterCursor.moveToFirst();
            while (!chracterCursor.isAfterLast() && isAlarm) {
                String type = chracterCursor.getString(2);
                int now = chracterCursor.getInt(3);
                int max = chracterCursor.getInt(4);
                boolean isChracterAlarm = Boolean.parseBoolean(chracterCursor.getString(5));
                if (max > now && type.equals("일일") && isChracterAlarm) {
                    content += name;
                    flag = true;
                    break;
                }
                chracterCursor.moveToNext();
            }
            chracterDBAdapter.close();
            cursor.moveToNext();
            if (flag) content += ", ";
        }
        chracterListDBAdapter.close();
        content = content.substring(0, content.length()-2);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setContentTitle("숙제를 안한 캐릭터가 있습니다.");
        //builder.setContentTitle("숙제 알림");
        //builder.setContentText(noti_content);
        //builder.setContentText("숙제를 안한 캐릭터가 있습니다.");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        return builder;
    }
}
