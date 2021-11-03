package com.lostark.lostarkapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lostark.lostarkapplication.database.Earring1DBAdapter;
import com.lostark.lostarkapplication.database.Earring2DBAdapter;
import com.lostark.lostarkapplication.database.EquipmentDBAdapter;
import com.lostark.lostarkapplication.database.EquipmentStoneDBAdapter;
import com.lostark.lostarkapplication.database.NeckDBAdapter;
import com.lostark.lostarkapplication.database.Ring1DBAdapter;
import com.lostark.lostarkapplication.database.Ring2DBAdapter;
import com.lostark.lostarkapplication.database.StatDBAdapter;
import com.lostark.lostarkapplication.database.StoneDBAdapter;

public class SettingActivity extends AppCompatActivity {
    private Button btnDeleteStone, btnDeletePreset;
    private CheckBox chkStoneHistory, chkStampListOpen;

    private NeckDBAdapter neckDBAdapter;
    private Earring1DBAdapter earring1DBAdapter;
    private Earring2DBAdapter earring2DBAdapter;
    private Ring1DBAdapter ring1DBAdapter;
    private Ring2DBAdapter ring2DBAdapter;
    private EquipmentStoneDBAdapter equipmentStoneDBAdapter;
    private EquipmentDBAdapter equipmentDBAdapter;
    private StatDBAdapter statDBAdapter;
    private StoneDBAdapter stoneDBAdapter;
    
    private AlertDialog alertDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("환경 설정");
        
        btnDeletePreset = findViewById(R.id.btnDeletePreset);
        btnDeleteStone = findViewById(R.id.btnDeleteStone);
        chkStoneHistory = findViewById(R.id.chkStoneHistory);
        chkStampListOpen = findViewById(R.id.chkStampListOpen);
        
        stoneDBAdapter = new StoneDBAdapter(getApplicationContext());
        neckDBAdapter = new NeckDBAdapter(getApplicationContext());
        earring1DBAdapter = new Earring1DBAdapter(getApplicationContext());
        earring2DBAdapter = new Earring2DBAdapter(getApplicationContext());
        ring1DBAdapter = new Ring1DBAdapter(getApplicationContext());
        ring2DBAdapter = new Ring2DBAdapter(getApplicationContext());
        equipmentStoneDBAdapter = new EquipmentStoneDBAdapter(getApplicationContext());
        equipmentDBAdapter = new EquipmentDBAdapter(getApplicationContext());
        statDBAdapter = new StatDBAdapter(getApplicationContext());

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();
        chkStoneHistory.setChecked(pref.getBoolean("stone_history", false));
        chkStampListOpen.setChecked(pref.getBoolean("stamp_open", false));

        chkStoneHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("stone_history", isChecked);
                editor.commit();
            }
        });

        chkStampListOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("stamp_open", isChecked);
                editor.commit();
            }
        });
        
        btnDeleteStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);
                
                txtContent.setText("어빌리티 스톤 세공 내역을 전부 삭제하시겠습니까?");
                btnOK.setText("전부 삭제");
                
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stoneDBAdapter.open();
                        stoneDBAdapter.deleteAllData();
                        stoneDBAdapter.close();
                        Toast.makeText(getApplicationContext(), "세공 내역을 모두 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
        
        btnDeletePreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("각인 계산서 프리셋을 전부 삭제하시겠습니까?");
                btnOK.setText("전부 삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        neckDBAdapter.open();
                        neckDBAdapter.deleteAllData();
                        neckDBAdapter.close();
                        earring1DBAdapter.open();
                        earring1DBAdapter.deleteAllData();
                        earring1DBAdapter.close();
                        earring2DBAdapter.open();
                        earring2DBAdapter.deleteAllData();
                        earring2DBAdapter.close();
                        ring1DBAdapter.open();
                        ring1DBAdapter.deleteAllData();
                        ring1DBAdapter.close();
                        ring2DBAdapter.open();
                        ring2DBAdapter.deleteAllData();
                        ring2DBAdapter.close();
                        equipmentStoneDBAdapter.open();
                        equipmentStoneDBAdapter.deleteAllData();
                        equipmentStoneDBAdapter.close();
                        equipmentDBAdapter.open();
                        equipmentDBAdapter.deleteAllData();
                        equipmentDBAdapter.close();
                        statDBAdapter.open();
                        statDBAdapter.deleteAllData();
                        statDBAdapter.close();
                        Toast.makeText(getApplicationContext(), "프리셋을 모두 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
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
