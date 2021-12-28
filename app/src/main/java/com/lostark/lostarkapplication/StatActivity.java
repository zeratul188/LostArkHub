package com.lostark.lostarkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;
import com.lostark.lostarkapplication.database.HistoryCountDBAdapter;
import com.lostark.lostarkapplication.database.HistoryDBAdapter;
import com.lostark.lostarkapplication.database.HomeworkStatueDBAdapter;
import com.lostark.lostarkapplication.objects.ChracterHistory;
import com.lostark.lostarkapplication.objects.History;
import com.lostark.lostarkapplication.objects.StatueChart;

import java.util.ArrayList;
import java.util.Collections;

public class StatActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView txtLogin, txtDungeon, txtBoss, txtQuest, txtHistoryCount;
    private ListView listChracter, listHistory;
    private BarChart barChart;

    private HistoryDBAdapter historyDBAdapter;
    private HistoryCountDBAdapter historyCountDBAdapter;
    private ArrayList<History> histories;
    private HistoryAdapter historyAdapter;
    private ArrayList<ChracterHistory> chracterHistories;
    private ChracterHistoryAdapter chracterHistoryAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private HomeworkStatueDBAdapter homeworkStatueDBAdapter;

    private Cursor cursor = null;
    private CustomToast customToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("숙제 통계");

        scrollView = findViewById(R.id.scrollView);
        txtLogin = findViewById(R.id.txtLogin);
        txtDungeon = findViewById(R.id.txtDungeon);
        txtBoss = findViewById(R.id.txtBoss);
        txtQuest = findViewById(R.id.txtQuest);
        listChracter = findViewById(R.id.listChracter);
        listHistory = findViewById(R.id.listHistory);
        txtHistoryCount = findViewById(R.id.txtHistoryCount);
        barChart = findViewById(R.id.barChart);

        historyDBAdapter = new HistoryDBAdapter(getApplicationContext());
        historyCountDBAdapter = new HistoryCountDBAdapter(getApplicationContext());
        customToast = new CustomToast(getApplicationContext());
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        homeworkStatueDBAdapter = new HomeworkStatueDBAdapter(getApplicationContext());

        ArrayList<BarEntry> charts = new ArrayList<>();
        homeworkStatueDBAdapter.open();
        int max = 0, now = 0;
        chracterListDBAdapter.open();
        Cursor chracter_cursor = chracterListDBAdapter.fetchAllData();
        chracter_cursor.moveToFirst();
        while (!chracter_cursor.isAfterLast()) {
            String name = chracter_cursor.getString(1);
            boolean isAlarm = Boolean.parseBoolean(chracter_cursor.getString(4));
            if (isAlarm) {
                ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getApplicationContext(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                chracterDBAdapter.open();
                Cursor homework_cursor = chracterDBAdapter.fetchAllData();
                homework_cursor.moveToFirst();
                while (!homework_cursor.isAfterLast()) {
                    String type = homework_cursor.getString(2);
                    boolean isHomeworkAlarm = Boolean.parseBoolean(homework_cursor.getString(5));
                    if (type.equals("일일") && isHomeworkAlarm) {
                        now += homework_cursor.getInt(3);
                        max += homework_cursor.getInt(4);
                    }
                    homework_cursor.moveToNext();
                }
                chracterDBAdapter.close();
            }
            chracter_cursor.moveToNext();
        }
        chracterListDBAdapter.close();
        int percent = (int)((double)now / (double)max * 100.0);
        homeworkStatueDBAdapter.changeLastValue(percent);
        Cursor cursor = homeworkStatueDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int title = cursor.getInt(1);
            int value = cursor.getInt(2);
            charts.add(new BarEntry(title, value));
            cursor.moveToNext();
        }
        homeworkStatueDBAdapter.close();

        BarDataSet barDataSet = new BarDataSet(charts, "기록");
        barDataSet.setColor(Color.parseColor("#7B9A3D"));
        barDataSet.setValueTextColor(Color.parseColor("#40FFFFFF"));
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#AAAAAA"));
        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setTextColor(Color.parseColor("#AAAAAA"));
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setTextColor(Color.parseColor("#AAAAAA"));
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.parseColor("#AAAAAA"));

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("");
        barChart.animateY(1000);

        try {
            historyCountDBAdapter.open();
            cursor = historyCountDBAdapter.fetchAllData();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                int count = cursor.getInt(2);
                switch (name) {
                    case "카오스 던전":
                        txtDungeon.setText(Integer.toString(count));
                        break;
                    case "가디언 토벌":
                        txtBoss.setText(Integer.toString(count));
                        break;
                    case "에포나 일일 의뢰":
                        txtQuest.setText(Integer.toString(count));
                        break;
                    case "접속":
                        txtLogin.setText(Integer.toString(count));
                        break;
                }
                cursor.moveToNext();
            }
            historyCountDBAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
            customToast.createToast("정보를 불러오는데 오류가 발생하였습니다.", Toast.LENGTH_SHORT);
            customToast.show();
        }

        histories = new ArrayList<>();
        try {
            historyDBAdapter.open();
            cursor = historyDBAdapter.fetchAllData();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String content = cursor.getString(3);
                histories.add(new History(name, date, content));
                cursor.moveToNext();
            }
            txtHistoryCount.setText("총 "+cursor.getCount()+"개의 기록");
            historyDBAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
            customToast.createToast("정보를 불러오는데 오류가 발생하였습니다.", Toast.LENGTH_SHORT);
            customToast.show();
        }
        Collections.sort(histories);
        historyAdapter = new HistoryAdapter(getApplicationContext(), histories);
        listHistory.setAdapter(historyAdapter);

        chracterHistories = new ArrayList<>();
        try {
            chracterListDBAdapter.open();
            cursor = chracterListDBAdapter.fetchAllData();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                String job = cursor.getString(2);
                int level = cursor.getInt(3);
                String server = cursor.getString(5);
                int dungeon = 0, boss = 0, quest = 0;
                ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getApplicationContext(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                chracterDBAdapter.open();
                Cursor cursor2 = chracterDBAdapter.fetchAllData();
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    String homework_name = cursor2.getString(1);
                    switch (homework_name) {
                        case "에포나 일일 의뢰":
                            quest = cursor2.getInt(7);
                            break;
                        case "카오스 던전":
                            dungeon = cursor2.getInt(7);
                            break;
                        case "가디언 토벌":
                            boss = cursor2.getInt(7);
                            break;
                    }
                    cursor2.moveToNext();
                }
                chracterDBAdapter.close();
                chracterHistories.add(new ChracterHistory(name, server, job, level, dungeon, boss, quest));
                cursor.moveToNext();
            }
            chracterListDBAdapter.close();
        } catch (Exception e) {
            e.printStackTrace();
            customToast.createToast("정보를 불러오는데 오류가 발생하였습니다.", Toast.LENGTH_SHORT);
            customToast.show();
        }
        Collections.sort(chracterHistories);
        chracterHistoryAdapter = new ChracterHistoryAdapter(getApplicationContext(), chracterHistories);
        listChracter.setAdapter(chracterHistoryAdapter);

        listHistory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listChracter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}