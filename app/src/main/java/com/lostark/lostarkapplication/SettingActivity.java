package com.lostark.lostarkapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;
import com.lostark.lostarkapplication.database.Earring1DBAdapter;
import com.lostark.lostarkapplication.database.Earring2DBAdapter;
import com.lostark.lostarkapplication.database.EquipmentDBAdapter;
import com.lostark.lostarkapplication.database.EquipmentStoneDBAdapter;
import com.lostark.lostarkapplication.database.HistoryCountDBAdapter;
import com.lostark.lostarkapplication.database.HistoryDBAdapter;
import com.lostark.lostarkapplication.database.NeckDBAdapter;
import com.lostark.lostarkapplication.database.Ring1DBAdapter;
import com.lostark.lostarkapplication.database.Ring2DBAdapter;
import com.lostark.lostarkapplication.database.SkillDBAdapter;
import com.lostark.lostarkapplication.database.SkillPresetDBAdapter;
import com.lostark.lostarkapplication.database.StatDBAdapter;
import com.lostark.lostarkapplication.database.StoneDBAdapter;
import com.lostark.lostarkapplication.objects.Report;
import com.lostark.lostarkapplication.ui.stamp.ClearEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SettingActivity extends AppCompatActivity {
    private final int REPORT_LIMIT = 3;

    private Button btnDeleteStone, btnDeletePreset, btnCheckUpdate, btnResetDate, btnDeleteSkillPreset, btnReportSubmit, btnDeleteStat, btnDeleteStack, btnReview, btnResetID;
    private Switch chkStoneHistory, chkStampListOpen, chkAlarm, chkHomeworkAlarm, chkUpdateAlarm, chkAutoCreateHomework, chkAutoLevelSetting, chkProgressHomework;
    private Spinner sprAlarm, sprLimitStack;
    private TextView txtResetDate, txtVersion, txtReportLimit, txtReportStatue, txtID;
    private ClearEditText edtReport;

    private NeckDBAdapter neckDBAdapter;
    private Earring1DBAdapter earring1DBAdapter;
    private Earring2DBAdapter earring2DBAdapter;
    private Ring1DBAdapter ring1DBAdapter;
    private Ring2DBAdapter ring2DBAdapter;
    private EquipmentStoneDBAdapter equipmentStoneDBAdapter;
    private EquipmentDBAdapter equipmentDBAdapter;
    private StatDBAdapter statDBAdapter;
    private StoneDBAdapter stoneDBAdapter;

    private HistoryDBAdapter historyDBAdapter;
    private HistoryCountDBAdapter historyCountDBAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;

    private SkillPresetDBAdapter skillPresetDBAdapter;
    
    private AlertDialog alertDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private CustomToast customToast;

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
        chkHomeworkAlarm = findViewById(R.id.chkHomeworkAlarm);
        txtVersion = findViewById(R.id.txtVersion);
        chkUpdateAlarm = findViewById(R.id.chkUpdateAlarm);
        btnDeleteSkillPreset = findViewById(R.id.btnDeleteSkillPreset);
        chkAutoCreateHomework = findViewById(R.id.chkAutoCreateHomework);
        chkAutoLevelSetting = findViewById(R.id.chkAutoLevelSetting);
        chkProgressHomework = findViewById(R.id.chkProgressHomework);
        edtReport = findViewById(R.id.edtReport);
        txtReportLimit = findViewById(R.id.txtReportLimit);
        btnReportSubmit = findViewById(R.id.btnReportSubmit);
        txtReportStatue = findViewById(R.id.txtReportStatue);
        btnDeleteStat = findViewById(R.id.btnDeleteStat);
        btnDeleteStack = findViewById(R.id.btnDeleteStack);
        sprLimitStack = findViewById(R.id.sprLimitStack);
        btnReview = findViewById(R.id.btnReview);
        txtID = findViewById(R.id.txtID);
        btnResetID = findViewById(R.id.btnResetID);

        customToast = new CustomToast(getApplicationContext());

        stoneDBAdapter = new StoneDBAdapter(getApplicationContext());
        neckDBAdapter = new NeckDBAdapter(getApplicationContext());
        earring1DBAdapter = new Earring1DBAdapter(getApplicationContext());
        earring2DBAdapter = new Earring2DBAdapter(getApplicationContext());
        ring1DBAdapter = new Ring1DBAdapter(getApplicationContext());
        ring2DBAdapter = new Ring2DBAdapter(getApplicationContext());
        equipmentStoneDBAdapter = new EquipmentStoneDBAdapter(getApplicationContext());
        equipmentDBAdapter = new EquipmentDBAdapter(getApplicationContext());
        statDBAdapter = new StatDBAdapter(getApplicationContext());

        historyDBAdapter = new HistoryDBAdapter(getApplicationContext());
        chracterListDBAdapter = new ChracterListDBAdapter(getApplicationContext());
        historyCountDBAdapter = new HistoryCountDBAdapter(getApplicationContext());

        skillPresetDBAdapter = new SkillPresetDBAdapter(getApplicationContext());

        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();
        chkStoneHistory.setChecked(pref.getBoolean("stone_history", false));
        chkStampListOpen.setChecked(pref.getBoolean("stamp_open", false));

        if (!pref.getString("app_id", "null").equals("null")) {
            String content = "앱 ID : "+pref.getString("app_id", "null");
            txtID.setText(content);
        }

        btnResetID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("앱 아이디를 변경하시겠습니까?\n아이디가 변경되던 이전 아이디의 건의사항 답변이나 기타 내용을 확인이 불가능해집니다.");
                btnOK.setText("변경");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = "";
                        for (int i = 0; i < 4; i++) {
                            String value = Integer.toString((int)(Math.random()*1234567)%10000);
                            if (value.length() != 4) {
                                int repeat = 4 - value.length();
                                String temp = "";
                                for (int j = 0; j < repeat; j++) {
                                    temp += "0";
                                }
                                temp += value;
                                value = temp;
                            }
                            id += value;
                            if (i != 3) id += "-";
                        }
                        editor.putString("app_id", id);
                        editor.commit();
                        customToast.createToast("아이디가 변경되었습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        String content = "앱 ID : "+pref.getString("app_id", "null");
                        txtID.setText(content);
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

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        txtResetDate.setText("다음 초기화 날짜 : "+pref.getInt("year", -1)+"년 "+pref.getInt("month", -1)+"월 "+pref.getInt("day", -1)+"일 오전 6시");
        txtVersion.setText("앱 버전 : "+getVersion());

        txtReportLimit.setText(Integer.toString(pref.getInt("report_count", 0)));
        if (pref.getInt("report_count", 0) == REPORT_LIMIT) {
            txtReportLimit.setTextColor(Color.parseColor("#FF8888"));
            edtReport.setEnabled(false);
        } else {
            txtReportLimit.setTextColor(Color.parseColor("#FFFFFF"));
            edtReport.setEnabled(true);
        }
        edtReport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtReportStatue.setText("("+edtReport.getText().toString().length()+"/400)");
                if (edtReport.getText().toString().length() < 10) btnReportSubmit.setEnabled(false);
                else {
                    if (pref.getInt("report_count", 0) == REPORT_LIMIT) btnReportSubmit.setEnabled(false);
                    else btnReportSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnReportSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reportRef = mDatabase.getReference("report");
                reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long number = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (number < (long) data.child("number").getValue()) {
                                number = (long) data.child("number").getValue();
                            }
                        }
                        number++;
                        DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
                        Calendar calendar = Calendar.getInstance();
                        Date now = calendar.getTime();
                        String date = df.format(now);
                        String version = getVersion();
                        String device = Build.MODEL;
                        String os = Build.VERSION.RELEASE.toString();
                        String content = edtReport.getText().toString();

                        try {
                            reportRef.child("report"+number).setValue(new Report(number, date, version, device, os, content, false, pref.getString("app_id", "null"), false));
                            customToast.createToast("메시지를 보내는데 성공하였습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                            editor.putInt("report_count", pref.getInt("report_count", 0)+1);
                            editor.commit();
                            txtReportLimit.setText(Integer.toString(pref.getInt("report_count", 0)));
                            if (pref.getInt("report_count", 0) == REPORT_LIMIT) {
                                txtReportLimit.setTextColor(Color.parseColor("#FF8888"));
                                edtReport.setEnabled(false);
                            } else {
                                txtReportLimit.setTextColor(Color.parseColor("#FFFFFF"));
                                edtReport.setEnabled(true);
                            }
                            edtReport.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            customToast.createToast("메시지를 보내는데 실패하였습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ArrayList<String> limits = new ArrayList<>();
        for (int i = 100; i <= 1000; i += 100) limits.add(Integer.toString(i));
        ArrayAdapter<String> limitAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.time_item, limits);
        sprLimitStack.setAdapter(limitAdapter);
        sprLimitStack.setSelection(limits.indexOf(Integer.toString(pref.getInt("limit_count", 300))));
        sprLimitStack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("limit_count", Integer.parseInt(limits.get(position)));
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) times.add(Integer.toString(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.time_item, times);
        sprAlarm.setAdapter(adapter);
        sprAlarm.setSelection(pref.getInt("setting_hour", 22));

        chkProgressHomework.setChecked(pref.getBoolean("progress_homework", false));
        chkProgressHomework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("progress_homework", isChecked);
                editor.commit();
            }
        });

        chkAutoLevelSetting.setChecked(pref.getBoolean("auto_level", true));
        chkAutoLevelSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("auto_level", isChecked);
                editor.commit();
            }
        });

        chkAlarm.setChecked(pref.getBoolean("alarm", false));
        chkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("alarm", isChecked);
                editor.commit();
            }
        });

        chkHomeworkAlarm.setChecked(pref.getBoolean("homework_alarm", false));
        chkHomeworkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("homework_alarm", isChecked);
                editor.commit();
            }
        });

        chkUpdateAlarm.setChecked(pref.getBoolean("update_alarm", false));
        chkUpdateAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("update_alarm", isChecked);
                editor.commit();
            }
        });

        chkAutoCreateHomework.setChecked(pref.getBoolean("auto_create_homework", true));
        chkAutoCreateHomework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("auto_create_homework", isChecked);
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
                        //Toast.makeText(getApplicationContext(), "현재 날짜로 설정되었습니다. 숙제 초기화 주기가 바뀌었습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("현재 날짜로 설정되었습니다. 숙제 초기화 주기가 바뀌었습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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

        btnDeleteSkillPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("스킬 시뮬레이션 프리셋을 모두 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skillPresetDBAdapter.open();
                        Cursor cursor = skillPresetDBAdapter.fetchAllData();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            int rowID = cursor.getInt(0);
                            /*skillDBAdapter = new SkillDBAdapter(getApplicationContext(), Integer.toString(rowID));
                            skillDBAdapter.open();
                            skillDBAdapter.dropTable();
                            skillDBAdapter.close();*/
                            String db_path = "/data/data/"+getPackageName();
                            String db_name = "LOSTARKHUB_SKILL"+rowID;
                            String db_fulll_path = db_path+"/databases/"+db_name;
                            File dbFile = new File(db_fulll_path);
                            if (!dbFile.delete()) {
                                //Toast.makeText(getApplicationContext(), "삭제에 실패했습니다. ("+rowID+")", Toast.LENGTH_SHORT).show();
                                customToast.createToast("삭제에 실패했습니다. ("+rowID+")", Toast.LENGTH_SHORT);
                                customToast.show();
                            }
                            cursor.moveToNext();
                        }
                        skillPresetDBAdapter.deleteAllData();
                        skillPresetDBAdapter.close();

                        //Toast.makeText(getApplicationContext(), "모든 스킬 프리셋을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("모든 스킬 프리셋을 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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
                        View view = getLayoutInflater().inflate(R.layout.updatedialog, null);

                        TextView txtContent = view.findViewById(R.id.txtContent);
                        Button btnOK = view.findViewById(R.id.btnOK);
                        Button btnGooglePlay = view.findViewById(R.id.btnGooglePlay);

                        if (version != null) {
                            if (version.equals(getVersion())) {
                                txtContent.setText("이미 최신 버전입니다.");
                                btnGooglePlay.setVisibility(View.GONE);
                            } else {
                                txtContent.setText("신규 버전 : "+version+"\n\n신규 버전이 있습니다.");
                                btnGooglePlay.setVisibility(View.VISIBLE);
                            }
                        } else {
                            txtContent.setText("데이터를 불어올 수 없습니다. 인터넷을 확인해주세요.");
                            btnGooglePlay.setVisibility(View.GONE);
                        }

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
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
                        //Toast.makeText(getApplicationContext(), "세공 내역을 모두 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        customToast.createToast("세공 내역을 모두 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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
                        customToast.createToast("프리셋을 모두 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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

        btnDeleteStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("숙제 기록을 전부 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyDBAdapter.open();
                        historyDBAdapter.deleteAllData();
                        historyDBAdapter.close();
                        customToast.createToast("모든 숙제 기록을 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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

        btnDeleteStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);

                TextView txtContent = view.findViewById(R.id.txtContent);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                Button btnOK = view.findViewById(R.id.btnOK);

                txtContent.setText("캐릭터별 현황 기록을 전부 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyCountDBAdapter.open();
                        historyCountDBAdapter.resetData();
                        historyCountDBAdapter.close();
                        chracterListDBAdapter.open();
                        Cursor cursor = chracterListDBAdapter.fetchAllData();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            String name = cursor.getString(1);
                            ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getApplicationContext(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                            chracterDBAdapter.open();
                            chracterDBAdapter.resetHistory();
                            chracterDBAdapter.close();
                            cursor.moveToNext();
                        }
                        chracterListDBAdapter.close();
                        customToast.createToast("모든 캐릭터별 현황 기록을 삭제하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
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
