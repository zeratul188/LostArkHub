package com.lostark.lostarkapplication.ui.commander;

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
import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        chracterAdapter = new ChracterAdapter(getActivity(), characters, getActivity());
        listView.setAdapter(chracterAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.chracter_add_dialog, null);

                EditText edtName = view.findViewById(R.id.edtName);
                EditText edtLevel = view.findViewById(R.id.edtLevel);
                Spinner sprJob = view.findViewById(R.id.sprJob);
                Button btnAdd = view.findViewById(R.id.btnAdd);

                List<String> jobs = Arrays.asList(getActivity().getResources().getStringArray(R.array.job));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.job_item, jobs);
                sprJob.setAdapter(adapter);

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
                        int level = Integer.parseInt(edtLevel.getText().toString());
                        chracterListDBAdapter.insertData(new Chracter(name, job, level, true));
                        characters.add(new Chracter(name, job, level, true));
                        chracterListDBAdapter.close();
                        Collections.sort(characters, new ChracterComparator());
                        chracterAdapter.notifyDataSetChanged();
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
            boolean isAlarm = Boolean.parseBoolean(cursor.getString(4));
            characters.add(new Chracter(name, job, level, isAlarm));
            cursor.moveToNext();
        }
        Collections.sort(characters, new ChracterComparator());
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
