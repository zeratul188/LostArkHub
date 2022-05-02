package com.lostark.lostarkapplication.ui.guild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddGuildActivity extends AppCompatActivity {
    private EditText edtName, edtBoss, edtLevel, edtMinLevel, edtIF, edtContent, edtRequest, edtSolution;
    private Switch swtRequest;
    private LinearLayout layoutRequest;
    private Button btnCreate;
    private Spinner sprServer;

    private CustomToast toast;
    private SharedPreferences pref;
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    private int max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guild);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("길드 홍보 글 작성");

        edtName = findViewById(R.id.edtName);
        edtBoss = findViewById(R.id.edtBoss);
        edtLevel = findViewById(R.id.edtLevel);
        edtMinLevel = findViewById(R.id.edtMinLevel);
        edtIF = findViewById(R.id.edtIF);
        edtContent = findViewById(R.id.edtContent);
        edtRequest = findViewById(R.id.edtRequest);
        swtRequest = findViewById(R.id.swtRequest);
        layoutRequest = findViewById(R.id.layoutRequest);
        btnCreate = findViewById(R.id.btnCreate);
        edtSolution = findViewById(R.id.edtSolution);
        sprServer = findViewById(R.id.sprServer);

        Intent intent = getIntent();
        max = intent.getIntExtra("max", 0);
        toast = new CustomToast(getApplicationContext());
        pref = getSharedPreferences("setting_file", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("guilds");

        List<String> servers = Arrays.asList(getResources().getStringArray(R.array.servers));
        ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.job_item, servers);
        sprServer.setAdapter(serverAdapter);

        swtRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) layoutRequest.setVisibility(View.VISIBLE);
                else layoutRequest.setVisibility(View.GONE);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().equals("")) {
                    toast.createToast("길드명을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (edtBoss.getText().equals("")) {
                    toast.createToast("길드장 닉네임을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (edtLevel.getText().equals("")) {
                    toast.createToast("길드 레벨을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (edtSolution.getText().equals("")) {
                    toast.createToast("길드 가입 방법을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (pref.getString("app_id", "null").equals("null")) {
                    toast.createToast("앱 아이디가 생성되지 않았습니다. 아이디를 생성 후 재시도 해주시기 바랍니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (swtRequest.isChecked() && edtRequest.getText().toString().equals("")) {
                    toast.createToast("문의 링크를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int number = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("max")) continue;
                            if (number < Integer.parseInt(data.child("number").getValue().toString())) {
                                number = Integer.parseInt(data.child("number").getValue().toString());
                            }
                        }
                        number++;
                        String id = pref.getString("app_id", "null");
                        String name = edtName.getText().toString();
                        String boss = edtBoss.getText().toString();
                        String condition = edtIF.getText().toString();
                        String solution = edtSolution.getText().toString();
                        String content = edtContent.getText().toString();
                        Calendar now = Calendar.getInstance();
                        String date = now.get(Calendar.YEAR)+"년 "+(now.get(Calendar.MONTH)+1)+"월 "+now.get(Calendar.DAY_OF_MONTH)+"일";
                        String link = edtRequest.getText().toString();
                        int level = Integer.parseInt(edtLevel.getText().toString());
                        int min;
                        if (edtMinLevel.getText().toString().equals("")) {
                            min = 0;
                        } else {
                            min = Integer.parseInt(edtMinLevel.getText().toString());
                        }
                        int index = 0;
                        boolean isLink = swtRequest.isChecked();
                        String server = sprServer.getSelectedItem().toString();
                        if (isLink) reference.child("guild"+number).setValue(new Guild(number, id, name, boss, condition, solution, content, date, link, level, min, index, 1, server));
                        else reference.child("guild"+number).setValue(new Guild(number, id, name, boss, condition, solution, content, date, level, min, index, 0, server));
                        toast.createToast("길드 홍보글을 작성했습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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