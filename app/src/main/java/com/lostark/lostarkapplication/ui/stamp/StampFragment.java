package com.lostark.lostarkapplication.ui.stamp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.StampDBAdapter;
import com.lostark.lostarkapplication.ui.stamp.objects.Filter;
import com.lostark.lostarkapplication.ui.stamp.objects.StampChecklist;
import com.lostark.lostarkapplication.ui.stamp.objects.StampList;

import java.util.ArrayList;

public class StampFragment extends Fragment {
    private static final int CLASS_LENGTH = 21;

    private ImageButton imgbtnFilter, imgbtnSearch;
    private ClearEditText edtSearch;
    private ListView listView;
    private RecyclerView listFilter;

    private StampDBAdapter stampDBAdapter;
    private StampListAdapter adapter;
    private ArrayList<StampList> stampLists, searchList;
    private AlertDialog alertDialog;
    private StampChecklist stampChecklist;
    private ArrayList<Filter> filters;
    private FilterAdapter filterAdapter;

    private StampViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(StampViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stamp, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        imgbtnFilter = root.findViewById(R.id.imgbtnFilter);
        imgbtnSearch = root.findViewById(R.id.imgbtnSearch);
        edtSearch = root.findViewById(R.id.edtSearch);
        listView = root.findViewById(R.id.listView);
        listFilter = root.findViewById(R.id.listFilter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listFilter.setLayoutManager(layoutManager);
        filters = new ArrayList<>();
        filterAdapter = new FilterAdapter(filters, getActivity(), stampChecklist, StampFragment.this);
        listFilter.setAdapter(filterAdapter);
        FilterDecoration decoration = new FilterDecoration();
        listFilter.addItemDecoration(decoration);

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        searchProcess();
                        adapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });

        imgbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProcess();
                adapter.notifyDataSetChanged();
            }
        });

        stampChecklist = new StampChecklist();
        imgbtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.filterdialog, null);

                CheckBox chkIncrease = view.findViewById(R.id.chkIncrease);
                CheckBox chkDecrease = view.findViewById(R.id.chkDecrease);
                Button btnReset = view.findViewById(R.id.btnReset);
                Button btnApply = view.findViewById(R.id.btnApply);
                CheckBox[] chkClasses = new CheckBox[CLASS_LENGTH];
                for (int i = 0; i < CLASS_LENGTH; i++) {
                    chkClasses[i] = view.findViewById(getActivity().getResources().getIdentifier("chkClass"+(i+1), "id", getActivity().getPackageName()));
                    boolean[] isChecks = stampChecklist.getIsClasses();
                    chkClasses[i].setChecked(isChecks[i]);
                }

                chkIncrease.setChecked(stampChecklist.isIncrease());
                chkDecrease.setChecked(stampChecklist.isDecrease());

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chkIncrease.setChecked(false);
                        chkDecrease.setChecked(false);
                        for (CheckBox chkBox : chkClasses) chkBox.setChecked(false);
                    }
                });

                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stampLists.clear();
                        ArrayList<String> filter_list = new ArrayList<>();
                        if (chkIncrease.isChecked()) filter_list.add(chkIncrease.getText().toString());
                        if (chkDecrease.isChecked()) filter_list.add(chkDecrease.getText().toString());
                        for (CheckBox chkBox : chkClasses) if (chkBox.isChecked()) filter_list.add(chkBox.getText().toString());

                        for (int i = 0; i < stampDBAdapter.getSize(); i++) {
                            String[] content = stampDBAdapter.readData(i);
                            if (filter_list.contains(content[2])) {
                                String[] levels = new String[3];
                                for (int j = 0; j < levels.length; j++) levels[j] = content[3+j];
                                stampLists.add(new StampList(content[0], content[1], content[2], levels));
                            }
                        }
                        if (stampLists.isEmpty()) {
                            for (int i = 0; i < stampDBAdapter.getSize(); i++) {
                                String[] content = stampDBAdapter.readData(i);
                                String[] levels = new String[3];
                                for (int j = 0; j < levels.length; j++) levels[j] = content[3+j];
                                stampLists.add(new StampList(content[0], content[1], content[2], levels));
                            }
                        }

                        searchProcess();

                        adapter.notifyDataSetChanged();
                        stampChecklist.setIncrease(chkIncrease.isChecked());
                        stampChecklist.setDecrease(chkDecrease.isChecked());
                        boolean[] isCheckClass = new boolean[CLASS_LENGTH];
                        for (int i = 0; i < CLASS_LENGTH; i++) isCheckClass[i] = chkClasses[i].isChecked();
                        stampChecklist.setIsClasses(isCheckClass);

                        filterAdapter.setChecklist(stampChecklist);

                        filters.clear();
                        if (chkIncrease.isChecked()) filters.add(new Filter(chkIncrease.getText().toString(), chkIncrease, -1));
                        if (chkDecrease.isChecked()) filters.add(new Filter(chkDecrease.getText().toString(), chkDecrease, -1));
                        for (int i = 0; i < chkClasses.length; i++) {
                            if (chkClasses[i].isChecked()) {
                                filters.add(new Filter(chkClasses[i].getText().toString(), chkClasses[i], i));
                            }
                        }
                        if (!filters.isEmpty()) {
                            listFilter.setVisibility(View.VISIBLE);
                            filterAdapter.notifyDataSetChanged();
                        } else {
                            listFilter.setVisibility(View.GONE);
                        }

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

        stampDBAdapter = new StampDBAdapter(getActivity());
        stampLists = new ArrayList<>();
        for (int i = 0; i < stampDBAdapter.getSize(); i++) {
            String[] content = stampDBAdapter.readData(i);
            String[] levels = new String[3];
            for (int j = 0; j < levels.length; j++) levels[j] = content[3+j];
            stampLists.add(new StampList(content[0], content[1], content[2], levels));
        }
        adapter = new StampListAdapter(getActivity(), stampLists);
        listView.setAdapter(adapter);

        return root;
    }

    private void searchProcess() {
        if (edtSearch.getText().toString().equals("")) {
            adapter.setStampLists(stampLists);
            adapter.notifyDataSetChanged();
            return;
        }

        searchList = (ArrayList<StampList>) stampLists.clone();
        String[] strs = edtSearch.getText().toString().split(" ");
        for (int i = 0; i < searchList.size(); i++) {
            for (String str : strs) {
                if (searchList.get(i).getName().indexOf(str) == -1) {
                    searchList.remove(i);
                    i--;
                    break;
                }
            }
        }
        adapter.setStampLists(searchList);
    }

    public void refreshData(StampChecklist checklist) {
        stampLists.clear();
        ArrayList<String> filter_list = new ArrayList<>();
        if (checklist.isIncrease()) filter_list.add("공통");
        if (checklist.isDecrease()) filter_list.add("감소");
        for (Filter filter : filterAdapter.getFilters()) if (filter.getChkBox().isChecked()) filter_list.add(filter.getChkBox().getText().toString());

        for (int i = 0; i < stampDBAdapter.getSize(); i++) {
            String[] content = stampDBAdapter.readData(i);
            if (filter_list.contains(content[2])) {
                String[] levels = new String[3];
                for (int j = 0; j < levels.length; j++) levels[j] = content[3+j];
                stampLists.add(new StampList(content[0], content[1], content[2], levels));
            }
        }
        if (stampLists.isEmpty()) {
            for (int i = 0; i < stampDBAdapter.getSize(); i++) {
                String[] content = stampDBAdapter.readData(i);
                String[] levels = new String[3];
                for (int j = 0; j < levels.length; j++) levels[j] = content[3+j];
                stampLists.add(new StampList(content[0], content[1], content[2], levels));
            }
        }
        adapter.notifyDataSetChanged();
        if (filterAdapter.getFilters().isEmpty()) listFilter.setVisibility(View.GONE);
        else listFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
