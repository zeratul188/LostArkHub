package com.lostark.lostarkapplication.ui.stamp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.stamp.objects.StampList;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class StampListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampList> stampLists;
    private SharedPreferences pref;

    public StampListAdapter(Context context, ArrayList<StampList> stampLists) {
        this.context = context;
        this.stampLists = stampLists;
        pref = context.getSharedPreferences("setting_file", MODE_PRIVATE);
    }

    public ArrayList<StampList> getStampLists() {
        return stampLists;
    }

    public void setStampLists(ArrayList<StampList> stampLists) {
        this.stampLists = stampLists;
    }

    @Override
    public int getCount() {
        return stampLists.size();
    }

    @Override
    public Object getItem(int position) {
        return stampLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.stamp_search_layout, null);

        ImageView imgStamp = convertView.findViewById(R.id.imgStamp);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtType = convertView.findViewById(R.id.txtType);
        ImageButton imgbtnEye = convertView.findViewById(R.id.imgbtnEye);
        LinearLayout layoutContent = convertView.findViewById(R.id.layoutContent);
        TextView[] txtLevels = new TextView[3];
        for (int i = 0; i < txtLevels.length; i++) {
            txtLevels[i] = convertView.findViewById(context.getResources().getIdentifier("txtLevel"+(i+1), "id", context.getPackageName()));
            txtLevels[i].setText(stampLists.get(position).getContents()[i]);
        }

        imgStamp.setImageResource(context.getResources().getIdentifier(stampLists.get(position).getImage(), "drawable", context.getPackageName()));
        txtName.setText(stampLists.get(position).getName());
        txtType.setText(stampLists.get(position).getType());

        if (txtType.getText().toString().equals("감소")) txtName.setTextColor(Color.parseColor("#e66363"));
        else txtName.setTextColor(Color.parseColor("#5ba5d4"));

        if (pref.getBoolean("stamp_open", false)) {
            imgbtnEye.setImageResource(context.getResources().getIdentifier("close_eye", "drawable", context.getPackageName()));
            stampLists.get(position).setOpen(true);
            layoutContent.setVisibility(View.VISIBLE);
        } else {
            imgbtnEye.setImageResource(context.getResources().getIdentifier("open_eye", "drawable", context.getPackageName()));
            stampLists.get(position).setOpen(false);
            layoutContent.setVisibility(View.GONE);
        }

        imgbtnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stampLists.get(position).isOpen()) {
                    imgbtnEye.setImageResource(context.getResources().getIdentifier("open_eye", "drawable", context.getPackageName()));
                    stampLists.get(position).setOpen(false);
                    layoutContent.setVisibility(View.GONE);
                } else {
                    imgbtnEye.setImageResource(context.getResources().getIdentifier("close_eye", "drawable", context.getPackageName()));
                    stampLists.get(position).setOpen(true);
                    layoutContent.setVisibility(View.VISIBLE);
                }
            }
        });

        return convertView;
    }
}
