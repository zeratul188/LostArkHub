package com.lostark.lostarkapplication.ui.commander;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lostark.lostarkapplication.MainActivity;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.BossDBAdapter;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;
import com.lostark.lostarkapplication.database.DungeonDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CommanderFragment extends Fragment {

    private CommanderViewModel homeViewModel;

    private ListView listView;
    private FloatingActionButton fabAdd;

    private ArrayList<Chracter> characters;
    private ChracterAdapter chracterAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(CommanderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_commander, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        listView = root.findViewById(R.id.listView);
        fabAdd = root.findViewById(R.id.fabAdd);

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());
        characters = new ArrayList<>();
        chracterAdapter = new ChracterAdapter(getActivity(), characters, getActivity(), this);
        listView.setAdapter(chracterAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.chracter_add_dialog, null);

                EditText edtName = view.findViewById(R.id.edtName);
                EditText edtLevel = view.findViewById(R.id.edtLevel);
                Spinner sprJob = view.findViewById(R.id.sprJob);
                Spinner sprServer = view.findViewById(R.id.sprServer);
                Button btnAdd = view.findViewById(R.id.btnAdd);

                List<String> jobs = Arrays.asList(getActivity().getResources().getStringArray(R.array.job));
                ArrayAdapter<String> jobAdapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, jobs);
                sprJob.setAdapter(jobAdapter);

                List<String> servers = Arrays.asList(getActivity().getResources().getStringArray(R.array.servers));
                ArrayAdapter<String> serverAdapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, servers);
                sprServer.setAdapter(serverAdapter);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtName.getText().toString().equals("") || edtLevel.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (isSameName(edtName.getText().toString())) {
                            Toast.makeText(getActivity(), "이미 동일한 캐릭터가 존재합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        chracterListDBAdapter.open();
                        String name = edtName.getText().toString();
                        String job = sprJob.getSelectedItem().toString();
                        String server = sprServer.getSelectedItem().toString();
                        int level = Integer.parseInt(edtLevel.getText().toString());
                        chracterListDBAdapter.insertData(new Chracter(name, job, server, level, 0, true));
                        characters.add(new Chracter(name, job, server, level, 0, true));
                        chracterListDBAdapter.close();
                        Collections.sort(characters);
                        chracterAdapter.notifyDataSetChanged();

                        SharedPreferences pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
                        if (pref.getBoolean("auto_create_homework", true)) {
                            boolean isAlarm = !pref.getBoolean("homework_alarm", false);
                            chracterListDBAdapter.open();
                            ChracterDBAdapter chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
                            chracterListDBAdapter.close();
                            chracterDBAdapter.open();
                            chracterDBAdapter.insertData(new Checklist("에포나 주간 의뢰", "주간", "", 0, 3, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("에포나 일일 의뢰", "일일", "", 0, 3, isAlarm));
                            chracterDBAdapter.insertData(new Checklist("에포나 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            if (level >= 325 && level < 1415) {
                                int now;
                                if (level >= 915 && level < 1325) now = 3;
                                else now = 2;
                                chracterDBAdapter.insertData(new Checklist("어비스 던전", "주간", "", 0, now, isAlarm));
                            }
                            if (level >= 915) {
                                chracterDBAdapter.insertData(new Checklist("도전 어비스 던전", "주간", "", 0, 2, isAlarm));
                            }
                            if (level >= 415) {
                                chracterDBAdapter.insertData(new Checklist("도전 가디언 토벌", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1370 && level < 1475) {
                                chracterDBAdapter.insertData(new Checklist("어비스 레이드 - 아르고스", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1415) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 발탄", "주간", "", 0, 2, isAlarm));
                            }
                            if (level >= 1430) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 비아키스", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1475) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 쿠크세이튼", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1490) {
                                chracterDBAdapter.insertData(new Checklist("군단장 레이드 - 아브렐슈드", "주간", "", 0, 6, isAlarm));
                            }
                            if (level >= 1385) {
                                chracterDBAdapter.insertData(new Checklist("한밤중의 서커스 리허설", "주간", "", 0, 3, isAlarm));
                            }
                            if (level >= 1430) {
                                chracterDBAdapter.insertData(new Checklist("몽환의 아스탤지어 데자뷰", "주간", "", 0, 4, isAlarm));
                            }
                            DungeonDBAdapter dungeonDBAdapter = new DungeonDBAdapter(getActivity());
                            if (level >= 1560) {
                                String[] args = dungeonDBAdapter.readData(dungeonDBAdapter.getSize()-1);
                                String content = args[0]+" : "+args[1];
                                chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                                chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            } else {
                                for (int i = 0; i < dungeonDBAdapter.getSize(); i++) {
                                    String[] args = dungeonDBAdapter.readData(i);
                                    int min_level = Integer.parseInt(args[2]);
                                    if (level < min_level) {
                                        if (i == 0) break;
                                        String[] results = dungeonDBAdapter.readData(i-1);
                                        String content = results[0]+" : "+results[1];
                                        chracterDBAdapter.insertData(new Checklist("카오스 던전", "일일", content, 0, 2, isAlarm));
                                        chracterDBAdapter.insertData(new Checklist("카던 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                        break;
                                    }
                                }
                            }
                            BossDBAdapter bossDBAdapter = new BossDBAdapter(getActivity());
                            if (level >= 1540) {
                                String[] args = bossDBAdapter.readData(bossDBAdapter.getSize()-1);
                                String content = args[1]+" : "+args[0];
                                chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                                chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                            } else {
                                for (int i = 0; i < bossDBAdapter.getSize(); i++) {
                                    String[] args = bossDBAdapter.readData(i);
                                    int min_level = Integer.parseInt(args[2]);
                                    if (level < min_level) {
                                        if (i == 0) break;
                                        String[] results = bossDBAdapter.readData(i-1);
                                        String content = results[1]+" : "+results[0];
                                        chracterDBAdapter.insertData(new Checklist("가디언 토벌", "일일", content, 0, 2, isAlarm));
                                        chracterDBAdapter.insertData(new Checklist("가디언 휴식", "휴식게이지", "", 0, 10, isAlarm));
                                        break;
                                    }
                                }
                            }

                            chracterDBAdapter.close();
                        }

                        Toast.makeText(getActivity(), name+"을 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        return root;
    }

    public void reSort() {
        Collections.sort(characters);
        chracterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        characters.clear();
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            String job = cursor.getString(2);
            int level = cursor.getInt(3);
            String server = cursor.getString(5);
            int favorite = cursor.getInt(6);
            boolean isAlarm = Boolean.parseBoolean(cursor.getString(4));
            characters.add(new Chracter(name, job, server, level, favorite, isAlarm));
            cursor.moveToNext();
        }
        Collections.sort(characters);
        chracterListDBAdapter.close();
        chracterAdapter.notifyDataSetChanged();
    }

    private boolean isSameName(String str) {
        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            if (str.equals(name)) {
                chracterListDBAdapter.close();
                return true;
            }
            cursor.moveToNext();
        }
        chracterListDBAdapter.close();
        return false;
    }
}
