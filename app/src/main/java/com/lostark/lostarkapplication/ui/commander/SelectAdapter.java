package com.lostark.lostarkapplication.ui.commander;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;

import java.util.ArrayList;

public class SelectAdapter extends BaseAdapter {
    private ArrayList<Select> selects;
    private Context context;

    public SelectAdapter(ArrayList<Select> selects, Context context) {
        this.selects = selects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return selects.size();
    }

    @Override
    public Object getItem(int position) {
        return selects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.select_list, null);

        TextView txtSelect = convertView.findViewById(R.id.txtSelect);
        CheckBox chkSelect = convertView.findViewById(R.id.chkSelect);

        txtSelect.setText(selects.get(position).getContent());
        chkSelect.setChecked(selects.get(position).isChecked());

        chkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selects.get(position).isChecked()) selects.get(position).setChecked(false);
                else selects.get(position).setChecked(true);
            }
        });

        return convertView;
    }
}
