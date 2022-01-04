package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class ChecklistActivity extends AppCompatActivity {
    private final int CLICK_LEFT = -1;
    private final int CLICK_RIGHT = 1;

    private BottomNavigationView bottomNavigationView;
    private ViewPager layoutFrame, pagerChracter;
    private ImageButton btnLeft, btnRight;

    private String name;
    private int level, now_position;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ChecklistPageViewAdapter pageViewAdapter;
    private ChracterPageViewAdapter chracterPageViewAdapter;
    private ArrayList<ChracterPosition> chracterPositions;
    private AlertDialog alertDialog;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;
    private ArrayList<Fragment> fragments;
    private ArrayList<Fragment> chracter_fragments;

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

        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        dayFragment.setArguments(bundle);
        weekFragment.setArguments(bundle);

        layoutFrame = findViewById(R.id.layoutFrame);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        pagerChracter = findViewById(R.id.pagerChracter);

        chracter_fragments = new ArrayList<>();
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            int level = cursor.getInt(3);
            chracterPositions.add(new ChracterPosition(name, level));
            cursor.moveToNext();
        }
        Collections.sort(chracterPositions);
        for (ChracterPosition obj : chracterPositions) {
            chracter_fragments.add(new ChracterFragment(obj.getName()));
        }
        for (int index = 0; index < chracterPositions.size(); index++) {
            if (chracterPositions.get(index).getName().equals(name)) {
                now_position = index;
                break;
            }
        }
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

        chracterPageViewAdapter = new ChracterPageViewAdapter(fragmentManager, chracter_fragments);
        pagerChracter.setAdapter(chracterPageViewAdapter);
        pagerChracter.setOffscreenPageLimit(chracter_fragments.size());
        pagerChracter.setCurrentItem(now_position);

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

        pagerChracter.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                name = ((ChracterFragment)chracter_fragments.get(position)).getName();
                changeChracter();
                btnDirectVisible();
                now_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChracter(CLICK_LEFT);
                now_position--;
                pagerChracter.setCurrentItem(now_position);
                btnDirectVisible();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChracter(CLICK_RIGHT);
                now_position++;
                pagerChracter.setCurrentItem(now_position);
                btnDirectVisible();
            }
        });

        btnDirectVisible();
    }

    private void changeChracter() {
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
        if (next_position != -1 && positions.size() > next_position) {
            name = positions.get(next_position).getName();
            level = positions.get(next_position).getLevel();
        }

        ((DayFragment)pageViewAdapter.getItem(0)).refresh(name);
        ((WeekFragment)pageViewAdapter.getItem(1)).refresh(name);
        pageViewAdapter.notifyDataSetChanged();

        chracterListDBAdapter.close();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checklist_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_list:
                View view = getLayoutInflater().inflate(R.layout.chracter_list_dialog, null);

                ListView listView = view.findViewById(R.id.listView);

                ArrayList<ChracterSelectList> lists = new ArrayList<>();

                chracterListDBAdapter.open();
                Cursor cursor = chracterListDBAdapter.fetchAllData();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(1);
                    String job = cursor.getString(2);
                    int level = cursor.getInt(3);
                    String server = cursor.getString(5);
                    if (!name.equals(this.name)) lists.add(new ChracterSelectList(name, job, server, level));
                    cursor.moveToNext();
                }
                chracterListDBAdapter.close();
                Collections.sort(lists);

                ChracterListAdapter adapter = new ChracterListAdapter(getApplicationContext(), lists);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        name = lists.get(position).getName();
                        now_position = position;
                        pagerChracter.setCurrentItem(position);
                        changeChracter();
                        btnDirectVisible();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ChecklistActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
