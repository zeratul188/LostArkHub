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
    private ArrayList<StampSetting> stamps, main;

    public StampSettingAdapter(Context context, ArrayList<StampSetting> stamps) {
        this.context = context;
        this.stamps = stamps;
        this.main = stamps;
    }

    public ArrayList<StampSetting> getStamps() {
        return stamps;
    }

    public void setStamps(ArrayList<StampSetting> stamps) {
        this.stamps = stamps;
    }

    public void changeMain() {
        stamps = main;
    }

    public ArrayList<StampSetting> getMain() {
        return main;
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
                stamps.get(position).setActivate(((CheckBox)v).isChecked());
                for (int i = 0; i < main.size(); i++) {
                    if (main.get(i).getName().equals(stamps.get(position).getName())) {
                        main.get(i).setActivate(((CheckBox)v).isChecked());
                        break;
                    }
                }
                /*
                if (((CheckBox)v).isChecked()) stamps.get(position).setActivate(true);
                else stamps.get(position).setActivate(false);
                 */
            }
        });


        return convertView;
    }
}
