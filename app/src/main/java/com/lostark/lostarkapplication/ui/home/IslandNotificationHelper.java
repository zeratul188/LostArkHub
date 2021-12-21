
package com.lostark.lostarkapplication.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.MainActivity;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

public class IslandNotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID_island";
    public static final String channelNm = "channelNm_island";

    private NotificationManager notiManager;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public IslandNotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannels();
        pref = base.getSharedPreferences("island_file", MODE_PRIVATE);
        editor = pref.edit();
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
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setContentTitle("모험섬 알림");
        builder.setContentText("모험섬이 곧 출현합니다.");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        editor.putBoolean("island_alarm", false);
        editor.commit();
        try {
            HomeFragment.offSwitchAlarm();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }
}
