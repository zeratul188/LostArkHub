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
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lostark.lostarkapplication.database.HistoryCountDBAdapter;
import com.lostark.lostarkapplication.database.HistoryDBAdapter;
import com.lostark.lostarkapplication.objects.History;
import com.lostark.lostarkapplication.ui.commander.ChecklistActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private AlertDialog alertDialog = null;

    private NavigationView navigationView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ChracterListDBAdapter chracterListDBAdapter;
    private HistoryCountDBAdapter historyCountDBAdapter;
    private HistoryDBAdapter historyDBAdapter;

    private long backKeyPressedTime = 0;

    private ImageView imgJob;
    private TextView txtName, txtLevel, txtJob;
    private LinearLayout layoutInformation;
    private FrameLayout layoutMain;

    private CustomToast customToast;

    private Bundle bundle = null;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (!pref.getBoolean("auto_level", true)) {
                return;
            }

            if (bundle.getBoolean("gone")) {
                return;
            }

            String level_str = bundle.getString("equip_level");
            if (level_str != null) {
                level_str = level_str.substring(3);
                level_str = level_str.replace(",", "");
                int level = 0;
                if (level_str.indexOf(".") != -1) {
                    double dv = Double.parseDouble(level_str);
                    level = (int)dv;
                } else level = Integer.parseInt(level_str);

                txtLevel.setText(Integer.toString(level));

                chracterListDBAdapter.open();
                Cursor cursor = chracterListDBAdapter.findFavoriteChracter();
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    String name = cursor.getString(1);
                    chracterListDBAdapter.changeLevel(name, level);
                }
                chracterListDBAdapter.close();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_commander, R.id.nav_stamp, R.id.nav_chracter, R.id.nav_skill)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int height = size.y;
        navigationView.getLayoutParams().width = height/7*2;
        navigationView.getLayoutParams().height = height;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        historyCountDBAdapter = new HistoryCountDBAdapter(getApplicationContext());
        customToast = new CustomToast(getApplicationContext());
        historyDBAdapter = new HistoryDBAdapter(getApplicationContext());

        historyCountDBAdapter.open();
        int count = historyCountDBAdapter.getQueryCount("접속");
        count++;
        historyCountDBAdapter.changeValue("접속", count);
        historyCountDBAdapter.close();

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();
        if (!pref.getBoolean("update_alarm", false)) {
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
                            View view = getLayoutInflater().inflate(R.layout.updatedialog, null);

                            TextView txtContent = view.findViewById(R.id.txtContent);
                            Button btnOK = view.findViewById(R.id.btnOK);
                            Button btnGooglePlay = view.findViewById(R.id.btnGooglePlay);

                            txtContent.setText("현재 버전 : "+getVersion()+"\n최신 버전 : "+version+"\n\n최신 버전이 있습니다.");

                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });

                            btnGooglePlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
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
        }

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        View view = navigationView.getHeaderView(0);

        imgJob = view.findViewById(R.id.imgJob);
        layoutInformation = view.findViewById(R.id.layoutInfomation);
        txtName = view.findViewById(R.id.txtName);
        txtLevel = view.findViewById(R.id.txtLevel);
        txtJob = view.findViewById(R.id.txtJob);
        layoutMain = view.findViewById(R.id.layoutMain);

        bundle = new Bundle();
    }

    public void uploadFavoriteChracter() {
        chracterListDBAdapter.open();
        Cursor cursort = chracterListDBAdapter.findFavoriteChracter();
        cursort.moveToFirst();
        if (cursort.getCount() > 0) {
            layoutInformation.setVisibility(View.VISIBLE);

            String name = cursort.getString(1);
            String job = cursort.getString(2);
            int level = cursort.getInt(3);

            new Thread() {
                @Override
                public void run() {
                    try {
                        bundleCroring(name, ".level-info2__expedition span", "equip_level", 2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }.start();

            txtName.setText(name);
            txtLevel.setText(Integer.toString(level));
            txtJob.setText(job);

            List<String> jobs = Arrays.asList(getResources().getStringArray(R.array.job));
            imgJob.setImageResource(getResources().getIdentifier("jb"+(jobs.indexOf(job)+1), "drawable", getPackageName()));
            navigationView.setBackgroundResource(getResources().getIdentifier("jbb"+(jobs.indexOf(job)+1), "drawable", getPackageName()));

            layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                    intent.putExtra("chracter_name", name);
                    intent.putExtra("chracter_level", level);
                    startActivity(intent);
                }
            });
        } else {
            layoutInformation.setVisibility(View.GONE);
            imgJob.setImageResource(0);
            txtName.setText("대표 캐릭터를 선택하세요.");
            layoutMain.setOnClickListener(null);
            navigationView.setBackgroundResource(R.drawable.jbb_none);
        }
        chracterListDBAdapter.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        uploadFavoriteChracter();

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

        Calendar now = Calendar.getInstance();

        chracterListDBAdapter.open();
        if (year != -1) {
            if (setting_calendar.compareTo(now) == -1) {
                int time = (int)(now.getTime().getTime() - setting_calendar.getTime().getTime())/1000;
                time /= (24*60*60);
                time++;
                boolean isResetWeek = false;
                Cursor cursor = chracterListDBAdapter.fetchAllData();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int rowID = cursor.getInt(0);
                    ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(this, "CHRACTER"+rowID);
                    chracterDBAdapter.open();
                    chracterDBAdapter.resetData("일일", time);
                    if (setting_calendar.get(Calendar.DAY_OF_WEEK) == 4) {
                        chracterDBAdapter.resetWeek("주간");
                        if (!isResetWeek) isResetWeek = true;
                    }
                    chracterDBAdapter.resetRestCount();
                    chracterDBAdapter.close();
                    cursor.moveToNext();
                }
                Calendar now_calendar = Calendar.getInstance();
                now_calendar.set(Calendar.HOUR_OF_DAY, 6);
                now_calendar.set(Calendar.MINUTE, 0);
                now_calendar.set(Calendar.SECOND, 0);
                now_calendar.add(Calendar.DATE, 1);
                setting_calendar = now_calendar;
                //Toast.makeText(getApplicationContext(), "오전 6시가 지나서 숙제가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                if (isResetWeek) customToast.createToast("오전 6시가 지나서 숙제가 초기화되었습니다. 오늘 수요일이라서 주간 숙제도 초기화되었습니다.", Toast.LENGTH_SHORT);
                else customToast.createToast("오전 6시가 지나서 숙제가 초기화되었습니다.", Toast.LENGTH_SHORT);
                customToast.show();
                editor.putInt("report_count", 0);
                editor.commit();
                historyDBAdapter.open();
                DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                Calendar calendar = Calendar.getInstance();
                String date = df.format(calendar.getTime());
                String name = "모든 캐릭터";
                String content;
                if (isResetWeek) content = "주간, 일일 숙제 모두 초기화 (수요일 오전 6시 초기화)";
                else content = "일일 숙제 모두 초기화 (오전 6시 초기화)";
                historyDBAdapter.insertData(new History(name, date, content));
                historyDBAdapter.close();
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

            startAlarm(now_cal);
        }
    }

    private void bundleCroring(String chracter, String id, String name, int index) throws IOException {
        Document doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+chracter).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
        Elements temele = doc.select(id+":nth-child("+index+")");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
        boolean isEmpty = temele.isEmpty(); //빼온 값 null체크
        Log.d("Tag", id+" : isNull? : " + isEmpty); //로그캣 출력
        if(isEmpty == false) { //null값이 아니면 크롤링 실행
            bundle.putBoolean("gone", false);
            bundle.putString(name, temele.get(0).text()); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
        } else {
            bundle.putBoolean("gone", true);
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
                break;
            case R.id.action_stats:
                startActivity(new Intent(MainActivity.this, StatActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            //Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            customToast.createToast("\'뒤로\' 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT);
            customToast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}
