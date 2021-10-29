package com.example.lostarkapplication.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lostarkapplication.R;
import com.example.lostarkapplication.database.StampDBAdapter;
import com.example.lostarkapplication.ui.slideshow.objects.StampCal;

import java.util.ArrayList;

public class StampCalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampCal> stamps;
    private StampDBAdapter stampDBAdapter;

    private static final int ICON_COUNT = 15;

    public StampCalAdapter(Context context, ArrayList<StampCal> stamps, Activity activity) {
        this.context = context;
        this.stamps = stamps;
        stampDBAdapter = new StampDBAdapter(activity);
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
        if (convertView == null) convertView = View.inflate(context, R.layout.stamplayout, null);

        String[] args = stampDBAdapter.readData(stamps.get(position).getName());
        boolean isDeburf;
        if (args[2].equals("감소")) isDeburf = true;
        else isDeburf = false;

        LinearLayout layoutOver = convertView.findViewById(R.id.layoutOver);
        LinearLayout layoutTitle = convertView.findViewById(R.id.layoutTitle);
        TextView txtOver = convertView.findViewById(R.id.txtOver);
        ImageView imgCrystal = convertView.findViewById(R.id.imgCrystal);
        TextView txtX = convertView.findViewById(R.id.txtX);
        ImageView imgStamp = convertView.findViewById(R.id.imgStamp);
        TextView txtStamp = convertView.findViewById(R.id.txtStamp);
        TextView txtCount = convertView.findViewById(R.id.txtCount);
        TextView txtLevel = convertView.findViewById(R.id.txtLevel);
        ImageView[] imgIcon = new ImageView[ICON_COUNT];
        for (int i = 0; i < ICON_COUNT; i++) {
            imgIcon[i] = convertView.findViewById(context.getResources().getIdentifier("imgIcon"+(i+1), "id", context.getPackageName()));
            if (isDeburf) {
                if (i < stamps.get(position).getCnt()) imgIcon[i].setImageResource(R.drawable.deburf_success);
                else imgIcon[i].setImageResource(R.drawable.deburf_none);
                layoutTitle.setBackgroundResource(R.drawable.textdeburfgradientstyle);
                imgCrystal.setImageResource(R.drawable.deburf_success);
                txtX.setTextColor(Color.parseColor("#e34c4c"));
                txtCount.setTextColor(Color.parseColor("#e34c4c"));
            } else {
                if (i < stamps.get(position).getCnt()) imgIcon[i].setImageResource(R.drawable.success);
                else imgIcon[i].setImageResource(R.drawable.none);
                layoutTitle.setBackgroundResource(R.drawable.textgradientstyle);
                imgCrystal.setImageResource(R.drawable.success);
                txtX.setTextColor(Color.parseColor("#698BDD"));
                txtCount.setTextColor(Color.parseColor("#698BDD"));
            }
        }

        imgStamp.setImageResource(context.getResources().getIdentifier(args[1], "drawable", context.getPackageName()));
        txtStamp.setText(stamps.get(position).getName());
        txtCount.setText(Integer.toString(stamps.get(position).getCnt()));
        if (stamps.get(position).getCnt() > 15) {
            layoutOver.setVisibility(View.VISIBLE);
            txtOver.setText(Integer.toString(stamps.get(position).getCnt() - 15));
        } else layoutOver.setVisibility(View.GONE);
        int level = 0;
        if (stamps.get(position).getCnt() < 5) level = 0;
        else if (stamps.get(position).getCnt() >= 5 && stamps.get(position).getCnt() < 10) level = 1;
        else if (stamps.get(position).getCnt() >= 10 && stamps.get(position).getCnt() < 15) level = 2;
        else level = 3;
        txtLevel.setText(Integer.toString(level));

        return convertView;
    }
}
