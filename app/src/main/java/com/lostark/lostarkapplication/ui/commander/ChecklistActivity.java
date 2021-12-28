package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.Arrays;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView imgJob;
    private TextView txtName, txtLevel, txtJob;

    private String name;
    private int level;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ChracterListDBAdapter chracterListDBAdapter;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;

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

        fragmentManager = getSupportFragmentManager();
        dayFragment = new DayFragment(name);
        weekFragment = new WeekFragment(name);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFrame, dayFragment).commitAllowingStateLoss();

        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        dayFragment.setArguments(bundle);
        weekFragment.setArguments(bundle);

        imgJob = findViewById(R.id.imgJob);
        txtName = findViewById(R.id.txtName);
        txtLevel = findViewById(R.id.txtLevel);
        txtJob = findViewById(R.id.txtJob);

        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchData(name);
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
                        setFrag(0);
                        break;
                    case R.id.action_week:
                        setFrag(1);
                        break;
                }
                return true;
            }
        });

        setFrag(0);
    }

    private void setFrag(int n)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (n)
        {
            case 0:
                fragmentTransaction.replace(R.id.layoutFrame, dayFragment).commitAllowingStateLoss();
                break;
            case 1:
                fragmentTransaction.replace(R.id.layoutFrame, weekFragment).commitAllowingStateLoss();
                break;
        }
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
