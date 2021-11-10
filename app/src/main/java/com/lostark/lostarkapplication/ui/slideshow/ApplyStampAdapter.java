package com.lostark.lostarkapplication.ui.slideshow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.ui.gallery.Stamp;

import java.util.ArrayList;

public class ApplyStampAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Stamp> stamps;

    public ApplyStampAdapter(Context context, ArrayList<Stamp> stamps) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.stamp_apply_setting_list, null);

        ImageView imgStamp = convertView.findViewById(R.id.imgStamp);
        TextView txtName = convertView.findViewById(R.id.txtName);

        imgStamp.setImageResource(context.getResources().getIdentifier(stamps.get(position).getImage(), "drawable", context.getPackageName()));
        txtName.setText(stamps.get(position).getName());

        return convertView;
    }
}
