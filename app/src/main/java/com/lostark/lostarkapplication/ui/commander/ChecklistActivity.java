package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity {
    private final int CLICK_LEFT = -1;
    private final int CLICK_RIGHT = 1;

    private BottomNavigationView bottomNavigationView;
    private ImageView imgJob;
    private TextView txtName, txtLevel, txtJob;
    private ViewPager layoutFrame;
    private ImageButton btnLeft, btnRight;

    private String name;
    private int level;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ChecklistPageViewAdapter pageViewAdapter;
    private ArrayList<ChracterPosition> chracterPositions;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("chracter_name");
        level = getIntent().getIntExtra("chracter_level", 0);
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        chracterPositions = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        dayFragment = new DayFragment(name);
        weekFragment = new WeekFragment(name);
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.layoutFrame, dayFragment).commitAllowingStateLoss();

        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        dayFragment.setArguments(bundle);
        weekFragment.setArguments(bundle);

        imgJob = findViewById(R.id.imgJob);
        txtName = findViewById(R.id.txtName);
        txtLevel = findViewById(R.id.txtLevel);
        txtJob = findViewById(R.id.txtJob);
        layoutFrame = findViewById(R.id.layoutFrame);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);

        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        chracterListDBAdapter.open();
        int size = chracterListDBAdapter.getSize();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            int level = cursor.getInt(3);
            chracterPositions.add(new ChracterPosition(name, level));
            cursor.moveToNext();
        }
        Collections.sort(chracterPositions);
        Cursor positionCursor = chracterListDBAdapter.fetchAllData();
        positionCursor.moveToFirst();
        int index = 0, position = 0;
        while (!positionCursor.isAfterLast()) {
            if (positionCursor.getString(1).equals(name)) {
                position = index;
                break;
            } else {
                index++;
                positionCursor.moveToNext();
            }
        }
        chracterListDBAdapter.close();

        fragments = new ArrayList<>();
        fragments.add(dayFragment);
        fragments.add(weekFragment);
        pageViewAdapter = new ChecklistPageViewAdapter(fragmentManager, fragments);
        layoutFrame.setAdapter(pageViewAdapter);
        layoutFrame.setOffscreenPageLimit(2);

        chracterListDBAdapter.open();
        cursor = chracterListDBAdapter.fetchData(name);
        cursor.moveToFirst();
        txtName.setText(cursor.getString(1));
        txtJob.setText(cursor.getString(2));
        txtLevel.setText(Integer.toString(cursor.getInt(3)));
        List<String> jobs = Arrays.asList(getResources().getStringArray(R.array.job));
        imgJob.setImageResource(getResources().getIdentifier("jb"+(jobs.indexOf(cursor.getString(2))+1), "drawable", getPackageName()));
        chracterListDBAdapter.close();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_day:
                        //setFrag(0);
                        layoutFrame.setCurrentItem(0);
                        break;
                    case R.id.action_week:
                        //setFrag(1);
                        layoutFrame.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        layoutFrame.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChracter(CLICK_LEFT);
                btnDirectVisible();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChracter(CLICK_RIGHT);
                btnDirectVisible();
            }
        });

        btnDirectVisible();
    }

    private void changeChracter(int direct) {
        ArrayList<ChracterPosition> positions = new ArrayList<>();
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            int level = cursor.getInt(3);
            positions.add(new ChracterPosition(name, level));
            cursor.moveToNext();
        }
        Collections.sort(positions);
        int next_position = -1;
        for (int index = 0; index < positions.size(); index++) {
            if (positions.get(index).getName().equals(name)) {
                next_position = index;
                break;
            }
        }
        if (next_position != -1) next_position += direct;
        if (next_position != -1 && positions.size() > next_position) {
            name = positions.get(next_position).getName();
            level = positions.get(next_position).getLevel();
        }
        cursor = chracterListDBAdapter.fetchData(name);
        cursor.moveToFirst();
        txtName.setText(cursor.getString(1));
        txtJob.setText(cursor.getString(2));
        txtLevel.setText(Integer.toString(cursor.getInt(3)));
        List<String> jobs = Arrays.asList(getResources().getStringArray(R.array.job));
        imgJob.setImageResource(getResources().getIdentifier("jb"+(jobs.indexOf(cursor.getString(2))+1), "drawable", getPackageName()));

        ((DayFragment)pageViewAdapter.getItem(0)).refresh(name);
        ((WeekFragment)pageViewAdapter.getItem(1)).refresh(name);
        pageViewAdapter.notifyDataSetChanged();

        chracterListDBAdapter.close();
    }

    private void btnDirectVisible() {
        ArrayList<ChracterPosition> positions = new ArrayList<>();
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            int level = cursor.getInt(3);
            positions.add(new ChracterPosition(name, level));
            cursor.moveToNext();
        }
        Collections.sort(positions);
        int next_position = -1;
        for (int index = 0; index < positions.size(); index++) {
            if (positions.get(index).getName().equals(name)) {
                next_position = index;
                break;
            }
        }

        if (next_position == 0) btnLeft.setVisibility(View.GONE);
        else btnLeft.setVisibility(View.VISIBLE);
        if (next_position == positions.size()-1) btnRight.setVisibility(View.GONE);
        else btnRight.setVisibility(View.VISIBLE);
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
