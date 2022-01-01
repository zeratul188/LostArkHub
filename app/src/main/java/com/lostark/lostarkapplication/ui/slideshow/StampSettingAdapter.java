package com.lostark.lostarkapplication.ui.slideshow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.stamp.objects.StampSetting;

import java.util.ArrayList;

public class StampSettingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampSetting> stamps;

    public StampSettingAdapter(Context context, ArrayList<StampSetting> stamps) {
        this.context = context;
        this.stamps = stamps;
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
        if (convertView == null) convertView = View.inflate(context, R.layout.stamp_setting_list, null);

        ImageView imgStamp = convertView.findViewById(R.id.imgStamp);
        TextView txtName = convertView.findViewById(R.id.txtName);
        CheckBox chkSelect = convertView.findViewById(R.id.chkSelect);

        imgStamp.setImageResource(context.getResources().getIdentifier(stamps.get(position).getImage(), "drawable", context.getPackageName()));
        txtName.setText(stamps.get(position).getName());
        chkSelect.setChecked(stamps.get(position).isActivate());

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkSelect.toggle();
                if (chkSelect.isChecked()) stamps.get(position).setActivate(true);
                else stamps.get(position).setActivate(false);
            }
        });

        chkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) stamps.get(position).setActivate(true);
                else stamps.get(position).setActivate(false);
            }
        });


        return convertView;
    }
}
