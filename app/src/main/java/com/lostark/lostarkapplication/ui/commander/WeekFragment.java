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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterDBAdapter;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WeekFragment extends Fragment implements ChecklistPositionAdapter.OnStartDragListener {
    private ListView listView;
    private LinearLayout layoutAdd, layoutPosition;

    private ChracterDBAdapter chracterDBAdapter;
    private ChracterListDBAdapter chracterListDBAdapter;
    private ArrayList<Checklist> checklists;
    private HomeworkAdapter homeworkAdapter;
    private AlertDialog alertDialog;
    private CustomToast customToast;
    private ChecklistPositionAdapter positionAdapter;
    private ItemTouchHelper helper;
    private ArrayList<Checklist> lists;
    String name;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public WeekFragment() {

    }

    public WeekFragment(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_week, container, false);

        listView = root.findViewById(R.id.listView);
        layoutAdd = root.findViewById(R.id.layoutAdd);
        layoutPosition = root.findViewById(R.id.layoutPosition);

        customToast = new CustomToast(getActivity());

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());
        chracterListDBAdapter.open();
        chracterDBAdapter = new ChracterDBAdapter(getActivity(), "CHRACTER"+chracterListDBAdapter.getRowID(name));
        chracterListDBAdapter.close();

        checklists = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(checklists, getActivity(), chracterDBAdapter, getActivity(), false, name);
        listView.setAdapter(homeworkAdapter);



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
                positionAdapter = new ChecklistPositionAdapter(lists, getActivity(), false, WeekFragment.this);

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
                EditText edtHomework  = view.findViewById(R.id.edtHomework);
                EditText edtCount = view.findViewById(R.id.edtCount);
                Button btnAdd = view.findViewById(R.id.btnAdd);

                List<String> days = Arrays.asList(getActivity().getResources().getStringArray(R.array.week_homework));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, days);
                sprList.setAdapter(adapter);

                chracterDBAdapter.open();
                btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString(), "주간"));
                chracterDBAdapter.close();

                sprList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        chracterDBAdapter.open();
                        btnAdd.setEnabled(!chracterDBAdapter.isSame(sprList.getSelectedItem().toString(), "주간"));
                        chracterDBAdapter.close();
                        if (sprList.getItemAtPosition(position).toString().equals("기타")) layoutAndsoon.setVisibility(View.VISIBLE);
                        else {
                            edtHomework.setText("");
                            layoutAndsoon.setVisibility(View.GONE);
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
                            //Toast.makeText(getActivity(), "값이 비어있습니다.", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(getActivity(), "이미 동일한 이름의 숙제가 존재합니다.", Toast.LENGTH_SHORT).show();
                                customToast.createToast("이미 동일한 이름의 숙제가 존재합니다.", Toast.LENGTH_SHORT);
                                customToast.show();
                                return;
                            }
                            cursor.moveToNext();
                        }
                        int max = Integer.parseInt(edtCount.getText().toString());
                        Checklist checklist = new Checklist(name, "주간", "", 0, max, true, 0);
                        checklist.setPosition(chracterDBAdapter.getLastRowID());
                        checklist.setIcon(0);
                        checklists.add(checklist);
                        chracterDBAdapter.insertData(checklist);
                        chracterDBAdapter.close();
                        //Toast.makeText(getActivity(), name+" 숙제를 추가하였습니다.", Toast.LENGTH_SHORT).show();
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
        homeworkAdapter = new HomeworkAdapter(checklists, getActivity(), chracterDBAdapter, getActivity(), false, name);
        listView.setAdapter(homeworkAdapter);

        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = getActivity().getSharedPreferences("setting_file", MODE_PRIVATE);
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
                chracterDBAdapter.resetData("주간", 1);
                chracterDBAdapter.close();
            }
        } else {
            editor.putInt("year", Calendar.YEAR);
            editor.putInt("month", Calendar.MONTH+1);
            editor.putInt("day", Calendar.DAY_OF_MONTH);
            editor.putInt("hour", Calendar.HOUR_OF_DAY);
            editor.commit();
        }

        checklists.clear();
        chracterDBAdapter.open();
        Cursor cursor = chracterDBAdapter.fetchAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(2).equals("주간")) {
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String content = cursor.getString(6);
                int now = cursor.getInt(3);
                int max = cursor.getInt(4);
                int history = cursor.getInt(7);
                boolean isAlarm = Boolean.parseBoolean(cursor.getString(5));
                int position = cursor.getInt(9);
                int icon = cursor.getInt(10);
                Checklist checklist = new Checklist(name, type, content, now, max, isAlarm, history);
                checklist.setPosition(position);
                checklist.setIcon(icon);
                checklists.add(checklist);
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
