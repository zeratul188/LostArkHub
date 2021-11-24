package com.lostark.lostarkapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private AlertDialog alertDialog = null;

    private AlarmManager alarmManager;
    private GregorianCalendar gregorianCalendar;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ChracterListDBAdapter chracterListDBAdapter;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_commander, R.id.nav_stamp, R.id.nav_chracter, R.id.nav_skill)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String version = null;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("version")) {
                        version = data.getValue().toString();
                    }
                }

                if (version != null) {
                    if (!getVersion().equals(version)) {
                        View view = getLayoutInflater().inflate(R.layout.onedialog, null);

                        TextView txtContent = view.findViewById(R.id.txtContent);
                        Button btnOK = view.findViewById(R.id.btnOK);

                        txtContent.setText("현재 버전 : "+getVersion()+"\n최신 버전 : "+version+"\n\n최신 버전이 있습니다.");

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(view);

                        alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (pref.getBoolean("alarm", false)) {
            int year = pref.getInt("year", -1);
            int month = pref.getInt("month", -1);
            int day = pref.getInt("day", -1);
            int hour = pref.getInt("hour", -1);
            if (year != -1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, pref.getInt("setting_hour", 22));

                startAlarm(calendar);
            } else {
                editor.putInt("year", Calendar.YEAR);
                editor.putInt("month", Calendar.MONTH+1);
                editor.putInt("day", Calendar.DAY_OF_MONTH);
                editor.putInt("hour", Calendar.HOUR_OF_DAY);
                editor.commit();
            }
        } else {
            cancelAlarm();
        }*/

        int year = pref.getInt("year", -1);
        int month = pref.getInt("month", -1);
        int day = pref.getInt("day", -1);

        Calendar setting_calendar = Calendar.getInstance();
        setting_calendar.set(Calendar.YEAR, year);
        setting_calendar.set(Calendar.MONTH, month-1);
        setting_calendar.set(Calendar.DAY_OF_MONTH, day);
        setting_calendar.set(Calendar.HOUR_OF_DAY, 6);
        setting_calendar.set(Calendar.MINUTE, 0);
        setting_calendar.set(Calendar.SECOND, 0);

        /*Toast.makeText(getApplicationContext(), "Year : "+setting_calendar.get(Calendar.YEAR)
                +"Month : "+(setting_calendar.get(Calendar.MONTH)+1)
                +"Day : "+setting_calendar.get(Calendar.DAY_OF_MONTH)
                +"Hour : "+setting_calendar.get(Calendar.HOUR_OF_DAY), Toast.LENGTH_SHORT).show();*/

        Calendar now = Calendar.getInstance();

        chracterListDBAdapter.open();
        if (year != -1) {
            if (setting_calendar.compareTo(now) == -1) {
                Cursor cursor = chracterListDBAdapter.fetchAllData();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int rowID = cursor.getInt(0);
                    ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(this, "CHRACTER"+rowID);
                    chracterDBAdapter.open();
                    chracterDBAdapter.resetData("일일");
                    if (setting_calendar.get(Calendar.DAY_OF_WEEK) == 4) chracterDBAdapter.resetWeek("주간");
                    chracterDBAdapter.close();
                    cursor.moveToNext();
                }
                setting_calendar.add(Calendar.DATE, 1);
                Toast.makeText(getApplicationContext(), "오전 6시가 지나서 숙제가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
            }
            editor.putInt("year", setting_calendar.get(Calendar.YEAR));
            editor.putInt("month", setting_calendar.get(Calendar.MONTH)+1);
            editor.putInt("day", setting_calendar.get(Calendar.DAY_OF_MONTH));
            editor.commit();
        } else {
            editor.putInt("year", now.get(Calendar.YEAR));
            editor.putInt("month", now.get(Calendar.MONTH)+1);
            editor.putInt("day", now.get(Calendar.DAY_OF_MONTH));
            editor.commit();
        }
        chracterListDBAdapter.close();

        boolean isAlarm = pref.getBoolean("alarm", false);
        int set_time = pref.getInt("setting_hour", 22);

        cancelAlarm();
        if (isAlarm) {
            Calendar now_cal = Calendar.getInstance();

            now_cal.set(Calendar.HOUR_OF_DAY, set_time);
            now_cal.set(Calendar.MINUTE, 0);
            now_cal.set(Calendar.SECOND, 0);

            /*Test
            *Calendar nwo = Calendar.getInstance();
            *nwo.add(Calendar.SECOND, 10);
            *startAlarm(nwo);*/

            startAlarm(now_cal);
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        String content = "";
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
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
                if (max > now && type.equals("일일") && isChracterAlarm) content += name;
                chracterCursor.moveToNext();
            }
            chracterDBAdapter.close();
            cursor.moveToNext();
        }
        chracterListDBAdapter.close();

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        if (!content.equals("")) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        else alarmManager.cancel(pendingIntent);
    }

    private String getVersion() {
        String version = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        return version;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}
