package com.lostark.lostarkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;
import com.lostark.lostarkapplication.database.HistoryCountDBAdapter;
import com.lostark.lostarkapplication.database.HistoryDBAdapter;
import com.lostark.lostarkapplication.objects.ChracterHistory;
import com.lostark.lostarkapplication.objects.History;

import java.util.ArrayList;
import java.util.Collections;

public class StatActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView txtLogin, txtDungeon, txtBoss, txtQuest;
    private ListView listChracter, listHistory;

    private HistoryDBAdapter historyDBAdapter;
    private HistoryCountDBAdapter historyCountDBAdapter;
    private ArrayList<History> histories;
    private HistoryAdapter historyAdapter;
    private ArrayList<ChracterHistory> chracterHistories;
    private ChracterHistoryAdapter chracterHistoryAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;

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

        historyDBAdapter = new HistoryDBAdapter(getApplicationContext());
        historyCountDBAdapter = new HistoryCountDBAdapter(getApplicationContext());
        customToast = new CustomToast(getApplicationContext());
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());

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