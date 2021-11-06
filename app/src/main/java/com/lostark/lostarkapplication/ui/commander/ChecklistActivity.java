package com.lostark.lostarkapplication.ui.commander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lostark.lostarkapplication.R;

public class ChecklistActivity extends AppCompatActivity {
    private RadioGroup rgMenu;
    private RadioButton rdoDayMenu, rdoWeekMenu;

    private String name;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private DayFragment dayFragment;
    private WeekFragment weekFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("chracter_name");
        setTitle(name);

        fragmentManager = getSupportFragmentManager();
        dayFragment = new DayFragment(name);
        weekFragment = new WeekFragment(name);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFrame, dayFragment).commitAllowingStateLoss();

        rgMenu = findViewById(R.id.rgMenu);
        rdoDayMenu = findViewById(R.id.rdoDayMenu);
        rdoWeekMenu = findViewById(R.id.rdoWeekMenu);

        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rdoDayMenu:
                        fragmentTransaction.replace(R.id.layoutFrame, dayFragment).commitAllowingStateLoss();
                        break;
                    case R.id.rdoWeekMenu:
                        fragmentTransaction.replace(R.id.layoutFrame, weekFragment).commitAllowingStateLoss();
                        break;
                }
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
