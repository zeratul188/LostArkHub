package com.lostark.lostarkapplication.ui.commander;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.ChracterListDBAdapter;

import java.util.Arrays;
import java.util.List;

public class ChracterFragment extends Fragment {
    private String name = null;

    private ImageView imgJob;
    private TextView txtName, txtLevel, txtJob;

    private ChracterListDBAdapter chracterListDBAdapter;

    public ChracterFragment() {
    }

    public ChracterFragment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_homework_chracter, container, false);

        imgJob = root.findViewById(R.id.imgJob);
        txtName = root.findViewById(R.id.txtName);
        txtLevel = root.findViewById(R.id.txtLevel);
        txtJob = root.findViewById(R.id.txtJob);

        chracterListDBAdapter = new ChracterListDBAdapter(getActivity());

        chracterListDBAdapter.open();
        Cursor cursor = chracterListDBAdapter.fetchData(name);
        cursor.moveToFirst();
        txtName.setText(cursor.getString(1));
        txtJob.setText(cursor.getString(2));
        txtLevel.setText(Integer.toString(cursor.getInt(3)));
        List<String> jobs = Arrays.asList(getResources().getStringArray(R.array.job));
        imgJob.setImageResource(getResources().getIdentifier("jb"+(jobs.indexOf(cursor.getString(2))+1), "drawable", getActivity().getPackageName()));
        chracterListDBAdapter.close();

        return root;
    }
}
