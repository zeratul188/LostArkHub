package com.example.lostarkapplication.ui.gallery;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.lostarkapplication.R;

import java.util.ArrayList;

public class StampAdapter extends BaseAdapter {
    private ArrayList<Stamp> stamps;
    private DataNetwork dn;
    private Context context;
    private AlertDialog alertDialog;

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public StampAdapter(ArrayList<Stamp> stamps, Context context, DataNetwork dn) {
        this.stamps = stamps;
        this.context = context;
        this.dn = dn;
    }

    @Override
    public int getCount() {
        return stamps.size();
    }

    @Override
    public Object getItem(int position) {
        return stamps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.stamplistitem, null);

        ImageView imgStamp = convertView.findViewById(R.id.imgStamp);
        TextView txtName = convertView.findViewById(R.id.txtName);
        Button btnSelect = convertView.findViewById(R.id.btnSelect);

        imgStamp.setImageResource(context.getResources().getIdentifier(stamps.get(position).getImage(), "drawable", context.getPackageName()));
        txtName.setText(stamps.get(position).getName());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dn.setContent(stamps.get(position).getName());
                alertDialog.dismiss();
            }
        });

        return convertView;
    }
}
