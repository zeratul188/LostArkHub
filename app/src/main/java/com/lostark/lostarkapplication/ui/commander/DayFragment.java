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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.CustomToast;
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

public class DayFragment extends Fragment implements ChecklistPositionAdapter.OnStartDragListener{
    private ListView listView;
    private LinearLayout layoutAdd, layoutPosition;

    private String name;
    private ChracterDBAdapter chracterDBAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ArrayList<Checklist> checklists;
    private HomeworkAdapter homeworkAdapter;
    private AlertDialog alertDialog;
    private CustomToast customToast;
    private ChecklistPositionAdapter positionAdapter;
    private ItemTouchHelper helper;
    private ArrayList<Checklist> lists;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public DayFragment() {

    }

    public DayFragment(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        listView = root.findViewById(R.id.listView);
        layoutAdd = root.findViewById(R.id.layoutAdd);
        layoutPosition = root.findViewById(R.id.layoutPosition);

        customToast = new CustomToast(getActivity());

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());
        chracterListDBAdapter.open();
        chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
        chracterListDBAdapter.close();

        checklists = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(checklists, getActivity(), chracterDBAdapter, getActivity(), true, name);
        listView.setAdapter(homeworkAdapter);

        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
        editor = pref.edit();

        layoutPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.edit_position_homework_dialog, null);

                RecyclerView listView = view.findViewById(R.id.listView);
                Button btnEdit = view.findViewById(R.id.btnEdit);

                lists = new ArrayList<>();
                lists = (ArrayList<Checklist>) checklists.clone();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getType().equals("휴식게이지")) {
                        lists.remove(i);
                        i--;
                    }
                }

                listView.setLayoutManager(new LinearLayoutManager(getActivity()));
                positionAdapter = new ChecklistPositionAdapter(lists, getActivity(), true, DayFragment.this);

                helper = new ItemTouchHelper(new ChecklistItemTouchHelperCallback(positionAdapter));
                helper.attachToRecyclerView(listView);

                listView.setAdapter(positionAdapter);
                ChecklistDecoration decoration = new ChecklistDecoration();
                listView.addItemDecoration(decoration);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < lists.size(); i++) {
                            for (Checklist checklist : checklists) {
                                if (checklist.getName().equals(lists.get(i).getName())) {
                                    checklist.setPosition(i);
                                    chracterDBAdapter.open();
                                    chracterDBAdapter.changePosition(checklist.getName(), i);
                                    chracterDBAdapter.close();
                                    break;
                                }
                            }
                        }
                        customToast.createToast("순서를 변경하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        Collections.sort(checklists);
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

        layoutAdd.setOnClickListener(new View.OnClickListener() {
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
                LinearLayout layoutSelect = view.findViewById(R.id.layoutSelect);
                ListView listSelect = view.findViewById(R.id.listSelect);

                ArrayList<Select> selects = new ArrayList<>();
                SelectAdapter selectAdapter = new SelectAdapter(selects, getActivity());
                listSelect.setAdapter(selectAdapter);

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
                btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString(), "일일"));
                chracterDBAdapter.close();

                sprList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        chracterDBAdapter.open();
                        btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString(), "일일"));
                        chracterDBAdapter.close();
                        if (sprList.getItemAtPosition(position).toString().equals("기타")) {
                            layoutAndsoon.setVisibility(View.VISIBLE);
                            layoutRest.setVisibility(View.GONE);
                            layoutSelect.setVisibility(View.GONE);
                        } else {
                            edtHomework.setText("");
                            layoutAndsoon.setVisibility(View.GONE);
                            if (sprList.getItemAtPosition(position).toString().equals("카오스 던전") || sprList.getItemAtPosition(position).toString().equals("가디언 토벌")) {
                                layoutRest.setVisibility(View.VISIBLE);
                                layoutSelect.setVisibility(View.VISIBLE);
                                selects.clear();
                                if (sprList.getItemAtPosition(position).toString().equals("카오스 던전")) {
                                    DungeonDBAdapter dungeonDBAdapter = new DungeonDBAdapter(getActivity());
                                    for (int i = 0; i < dungeonDBAdapter.getSize(); i++) {
                                        String[] args = dungeonDBAdapter.readData(i);
                                        String content = args[0]+" : "+args[1];
                                        selects.add(new Select(content));
                                    }
                                } else if (sprList.getItemAtPosition(position).toString().equals("가디언 토벌")) {
                                    BossDBAdapter bossDBAdapter = new BossDBAdapter(getActivity());
                                    for (int i = 0; i < bossDBAdapter.getSize(); i++) {
                                        String[] args = bossDBAdapter.readData(i);
                                        String content = args[1]+" : "+args[0];
                                        selects.add(new Select(content));
                                    }
                                }
                                selectAdapter.notifyDataSetChanged();
                            } else if (sprList.getItemAtPosition(position).toString().equals("에포나 일일 의뢰")) {
                                layoutSelect.setVisibility(View.GONE);
                                layoutRest.setVisibility(View.VISIBLE);
                            } else {
                                txtRest.setText("0");
                                seekRest.setProgress(0);
                                layoutSelect.setVisibility(View.GONE);
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
                            //.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                            customToast.createToast("값이 비어있습니다.", Toast.LENGTH_SHORT);
                            customToast.show();
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
                                customToast.createToast("이미 동일한 이름의 숙제가 존재합니다.", Toast.LENGTH_SHORT);
                                customToast.show();
                                return;
                            }
                            cursor.moveToNext();
                        }
                        int max = Integer.parseInt(edtCount.getText().toString());
                        Checklist checklist;
                        if (name.equals("카오스 던전") || name.equals("가디언 토벌")) {
                            String content = "";
                            for (Select select : selects) {
                                if (select.isChecked()) {
                                    if (!content.equals("")) content += "\n";
                                    content += select.getContent();
                                }
                            }
                            checklist = new Checklist(name, "일일", content, 0, max, !pref.getBoolean("homework_alarm", false), 0);
                        } else checklist = new Checklist(name, "일일", "", 0, max, !pref.getBoolean("homework_alarm", false), 0);
                        checklist.setPosition(chracterDBAdapter.getLastRowID());
                        checklist.setIcon(0);
                        checklists.add(0, checklist);
                        chracterDBAdapter.insertData(checklist);
                        if (name.equals("카오스 던전")) {
                            int progress = seekRest.getProgress();
                            Checklist restList = new Checklist("카던 휴식", "휴식게이지", "", progress, 10, false, 0);
                            checklist.setPosition(9998);
                            checklists.add(restList);
                            chracterDBAdapter.insertData(restList);
                        } else if (name.equals("가디언 토벌")) {
                            int progress = seekRest.getProgress();
                            Checklist restList = new Checklist("가디언 휴식", "휴식게이지", "", progress, 10, false, 0);
                            checklist.setPosition(9998);
                            checklists.add(restList);
                            chracterDBAdapter.insertData(restList);
                        } else if (name.equals("에포나 일일 의뢰")) {
                            int progress = seekRest.getProgress();
                            Checklist restList = new Checklist("에포나 휴식", "휴식게이지", "", progress, 10, false, 0);
                            checklist.setPosition(9998);
                            checklists.add(restList);
                            chracterDBAdapter.insertData(restList);
                        }
                        chracterDBAdapter.close();
                        customToast.createToast(name+" 숙제를 추가하였습니다.", Toast.LENGTH_SHORT);
                        customToast.show();
                        Collections.sort(checklists);
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

    public void refresh(String name) {
        this.name = name;
        chracterListDBAdapter.open();
        chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
        chracterListDBAdapter.close();

        checklists = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(checklists, getActivity(), chracterDBAdapter, getActivity(), true, name);
        listView.setAdapter(homeworkAdapter);

        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        checklists.clear();
        chracterDBAdapter.open();
        Cursor cursor = chracterDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(2).equals("일일") || cursor.getString(2).equals("휴식게이지")) {
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String content = cursor.getString(6);
                int now = cursor.getInt(3);
                int max = cursor.getInt(4);
                boolean isAlarm = Boolean.parseBoolean(cursor.getString(5));
                int history = cursor.getInt(7);
                int position = cursor.getInt(9);
                int icon = cursor.getInt(10);
                Checklist checklist = new Checklist(name, type, content, now, max, isAlarm, history);
                checklist.setPosition(position);
                checklist.setIcon(icon);
                checklists.add(checklist);
                /*
                if (type.equals("일일")) checklists.add(0, new Checklist(name, type, content, now, max, isAlarm, history));
                else checklists.add(new Checklist(name, type, content, now, max, isAlarm, history));
                */
            }
            cursor.moveToNext();
        }
        chracterDBAdapter.close();
        Collections.sort(checklists);
        homeworkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(ChecklistPositionAdapter.ViewHolder viewHolder) {
        helper.startDrag(viewHolder);
    }
}
