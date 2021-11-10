package com.lostark.lostarkapplication.ui.commander;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DayFragment extends Fragment {
    private ListView listView;
    private TextView txtDungeonRest, txtBossRest;
    private ProgressBar progressDungeon, progressBoss;
    private Button btnRest, btnAdd;
    private LinearLayout layoutDungeon, layoutBoss;

    private String name;
    private ChracterDBAdapter chracterDBAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ArrayList<Checklist> checklists;
    private HomeworkAdapter homeworkAdapter;
    private AlertDialog alertDialog;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public DayFragment(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        listView = root.findViewById(R.id.listView);
        txtDungeonRest = root.findViewById(R.id.txtDungeonRest);
        txtBossRest = root.findViewById(R.id.txtBossRest);
        progressDungeon = root.findViewById(R.id.progressDungeon);
        progressBoss = root.findViewById(R.id.progressBoss);
        btnRest = root.findViewById(R.id.btnRest);
        btnAdd = root.findViewById(R.id.btnAdd);
        layoutDungeon = root.findViewById(R.id.layoutDungeon);
        layoutBoss = root.findViewById(R.id.layoutBoss);

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());
        chracterListDBAdapter.open();
        chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
        chracterListDBAdapter.close();

        checklists = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(checklists, getActivity(), chracterDBAdapter, getActivity(), true);
        listView.setAdapter(homeworkAdapter);

        homeworkAdapter.setDungeonPackage(layoutDungeon, txtDungeonRest, progressDungeon);
        homeworkAdapter.setBossPackage(layoutBoss, txtBossRest, progressBoss);

        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        layoutDungeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.rest_edit_dialog, null);

                SeekBar seekRest = view.findViewById(R.id.seekRest);
                TextView txtRest = view.findViewById(R.id.txtRest);
                Button btnEdit = view.findViewById(R.id.btnEdit);

                seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        txtRest.setText(Integer.toString(progress*10));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeNow("카던 휴식", seekRest.getProgress());
                        chracterDBAdapter.close();
                        onResume();
                        Toast.makeText(getActivity(), "카오스 던전 휴식 게이지를 수정하였습니다.", Toast.LENGTH_SHORT).show();
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

        layoutBoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.rest_edit_dialog, null);

                SeekBar seekRest = view.findViewById(R.id.seekRest);
                TextView txtRest = view.findViewById(R.id.txtRest);
                Button btnEdit = view.findViewById(R.id.btnEdit);

                seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        txtRest.setText(Integer.toString(progress*10));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chracterDBAdapter.open();
                        chracterDBAdapter.changeNow("가디언 휴식", seekRest.getProgress());
                        chracterDBAdapter.close();
                        onResume();
                        Toast.makeText(getActivity(), "카오스 던전 휴식 게이지를 수정하였습니다.", Toast.LENGTH_SHORT).show();
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.add_homework_dialog, null);

                Spinner sprList = view.findViewById(R.id.sprList);
                LinearLayout layoutAndsoon = view.findViewById(R.id.layoutAndsoon);
                LinearLayout layoutRest = view.findViewById(R.id.layoutRest);
                EditText edtHomework  = view.findViewById(R.id.edtHomework);
                EditText edtCount = view.findViewById(R.id.edtCount);
                Button btnAdd = view.findViewById(R.id.btnAdd);
                SeekBar seekRest = view.findViewById(R.id.seekRest);
                TextView txtRest = view.findViewById(R.id.txtRest);

                List<String> days = Arrays.asList(getActivity().getResources().getStringArray(R.array.day_homework));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, days);
                sprList.setAdapter(adapter);

                seekRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        txtRest.setText(Integer.toString(progress*10));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                chracterDBAdapter.open();
                btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString()));
                chracterDBAdapter.close();

                sprList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        chracterDBAdapter.open();
                        btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString()));
                        chracterDBAdapter.close();
                        if (sprList.getItemAtPosition(position).toString().equals("기타")) layoutAndsoon.setVisibility(View.VISIBLE);
                        else {
                            edtHomework.setText("");
                            layoutAndsoon.setVisibility(View.GONE);
                            if (sprList.getItemAtPosition(position).toString().equals("카오스 던전") || sprList.getItemAtPosition(position).toString().equals("가디언 토벌")) layoutRest.setVisibility(View.VISIBLE);
                            else {
                                txtRest.setText("0");
                                seekRest.setProgress(0);
                                layoutRest.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((sprList.getSelectedItem().toString().equals("기타") && edtHomework.getText().toString().equals("")) || edtCount.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name;
                        if (sprList.getSelectedItem().toString().equals("기타")) name = edtHomework.getText().toString();
                        else name = sprList.getSelectedItem().toString();
                        chracterDBAdapter.open();
                        Cursor cursor = chracterDBAdapter.fetchAllData();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if (cursor.getString(1).equals(name)) {
                                Toast.makeText(getActivity(), "이미 동일한 이름의 숙제가 존재합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            cursor.moveToNext();
                        }
                        int max = Integer.parseInt(edtCount.getText().toString());
                        Checklist checklist = new Checklist(name, "일일", 0, max, !pref.getBoolean("homework_alarm", false));
                        checklists.add(0, checklist);
                        chracterDBAdapter.insertData(checklist);
                        if (name.equals("카오스 던전")) {
                            layoutDungeon.setVisibility(View.VISIBLE);
                            int progress = seekRest.getProgress();
                            progressDungeon.setProgress(progress);
                            txtDungeonRest.setText(Integer.toString(progress*10));
                            Checklist restList = new Checklist("카던 휴식", "휴식게이지", progress, 10, false);
                            checklists.add(restList);
                            chracterDBAdapter.insertData(restList);
                        } else if (name.equals("가디언 토벌")) {
                            layoutBoss.setVisibility(View.VISIBLE);
                            int progress = seekRest.getProgress();
                            progressBoss.setProgress(progress);
                            txtBossRest.setText(Integer.toString(progress*10));
                            Checklist restList = new Checklist("가디언 휴식", "휴식게이지", progress, 10, false);
                            checklists.add(restList);
                            chracterDBAdapter.insertData(restList);
                        }
                        chracterDBAdapter.close();
                        Toast.makeText(getActivity(), name+" 숙제를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        homeworkAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        /*pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        int year = pref.getInt("year", -1);
        int month = pref.getInt("month", -1);
        int day = pref.getInt("day", -1);
        int hour = pref.getInt("hour", -1);

        Calendar calendar = Calendar.getInstance();
        if (year != -1) {
            if (year <= Calendar.YEAR && month <= Calendar.MONTH+1 && day <= Calendar.DAY_OF_MONTH && hour < 6) {
                editor.putInt("year", Calendar.YEAR);
                editor.putInt("month", Calendar.MONTH+1);
                editor.putInt("day", Calendar.DAY_OF_MONTH);
                editor.putInt("hour", Calendar.HOUR_OF_DAY);
                editor.commit();

                chracterDBAdapter.open();
                chracterDBAdapter.resetData("일일");
                chracterDBAdapter.close();
            }
        } else {
            editor.putInt("year", Calendar.YEAR);
            editor.putInt("month", Calendar.MONTH+1);
            editor.putInt("day", Calendar.DAY_OF_MONTH);
            editor.putInt("hour", Calendar.HOUR_OF_DAY);
            editor.commit();
        }*/

        checklists.clear();
        layoutDungeon.setVisibility(View.GONE);
        layoutBoss.setVisibility(View.GONE);
        chracterDBAdapter.open();
        Cursor cursor = chracterDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(2).equals("일일") || cursor.getString(2).equals("휴식게이지")) {
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                int now = cursor.getInt(3);
                int max = cursor.getInt(4);
                boolean isAlarm = Boolean.parseBoolean(cursor.getString(5));
                if (type.equals("일일")) checklists.add(0, new Checklist(name, type, now, max, isAlarm));
                else checklists.add(new Checklist(name, type, now, max, isAlarm));
                if (name.equals("카던 휴식")) {
                    layoutDungeon.setVisibility(View.VISIBLE);
                    progressDungeon.setProgress(now);
                    txtDungeonRest.setText(Integer.toString(now*10));
                }
                if (name.equals("가디언 휴식")) {
                    layoutBoss.setVisibility(View.VISIBLE);
                    progressBoss.setProgress(now);
                    txtBossRest.setText(Integer.toString(now*10));
                }
            }
            cursor.moveToNext();
        }
        chracterDBAdapter.close();
        homeworkAdapter.notifyDataSetChanged();
    }
}
