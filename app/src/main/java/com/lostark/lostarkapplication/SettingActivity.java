package com.lostark.lostarkapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.database.Earring1DBAdapter;
import com.lostark.lostarkapplication.database.Earring2DBAdapter;
import com.lostark.lostarkapplication.database.EquipmentDBAdapter;
import com.lostark.lostarkapplication.database.EquipmentStoneDBAdapter;
import com.lostark.lostarkapplication.database.NeckDBAdapter;
import com.lostark.lostarkapplication.database.Ring1DBAdapter;
import com.lostark.lostarkapplication.database.Ring2DBAdapter;
import com.lostark.lostarkapplication.database.StatDBAdapter;
import com.lostark.lostarkapplication.database.StoneDBAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {
    private Button btnDeleteStone, btnDeletePreset, btnCheckUpdate, btnResetDate;
    private CheckBox chkStoneHistory, chkStampListOpen, chkAlarm;
    private Spinner sprAlarm;
    private TextView txtResetDate;

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

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

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
        btnCheckUpdate = findViewById(R.id.btnCheckUpdate);
        chkAlarm = findViewById(R.id.chkAlarm);
        btnResetDate = findViewById(R.id.btnResetDate);
        sprAlarm = findViewById(R.id.sprAlarm);
        txtResetDate = findViewById(R.id.txtResetDate);
        
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

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        txtResetDate.setText("다음 초기화 날짜 : "+pref.getInt("year", -1)+"년 "+pref.getInt("month", -1)+"월 "+pref.getInt("day", -1)+"일 오전 6시");

        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) times.add(Integer.toString(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.time_item, times);
        sprAlarm.setAdapter(adapter);
        sprAlarm.setSelection(pref.getInt("setting_hour", 22));

        chkAlarm.setChecked(pref.getBoolean("alarm", false));
        chkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("alarm", isChecked);
                editor.commit();
            }
        });

        btnResetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("정말로 다음 초기화 날짜를 오늘로 초기화하시겠습니까?\n초기화하게되면 숙제가 모두 횟수가 0으로 초기화가 될것입니다.");
                btnOK.setText("초기화");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        editor.putInt("year", now.get(Calendar.YEAR));
                        editor.putInt("month", now.get(Calendar.MONTH)+1);
                        editor.putInt("day", now.get(Calendar.DAY_OF_MONTH));
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "현재 날짜로 설정되었습니다. 숙제 초기화 주기가 바뀌었습니다.", Toast.LENGTH_SHORT).show();
                        txtResetDate.setText("다음 초기화 날짜 : "+pref.getInt("year", -1)+"년 "+pref.getInt("month", -1)+"월 "+pref.getInt("day", -1)+"일 오전 6시");
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

        sprAlarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int time = Integer.parseInt(sprAlarm.getSelectedItem().toString());
                editor.putInt("setting_hour", time);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String version = null;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("version")) {
                                version = data.getValue().toString();
                            }
                        }
                        View view = getLayoutInflater().inflate(R.layout.onedialog, null);

                        TextView txtContent = view.findViewById(R.id.txtContent);
                        Button btnOK = view.findViewById(R.id.btnOK);

                        if (version != null) {
                            if (version.equals(getVersion())) txtContent.setText("이미 최신 버전입니다.");
                            else txtContent.setText("신규 버전 : "+version+"\n\n신규 버전이 있습니다.");
                        } else txtContent.setText("데이터를 불어올 수 없습니다. 인터넷을 확인해주세요.");

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

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

    private String getVersion() {
        String version = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        return version;
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
