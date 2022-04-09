package com.lostark.lostarkapplication.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.lostark.lostarkapplication.R;
import com.lostark.lostarkapplication.database.StampDBAdapter;
import com.lostark.lostarkapplication.ui.slideshow.objects.StampCal;

import java.util.ArrayList;

public class StampCalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampCal> stamps;
    private StampDBAdapter stampDBAdapter;

    private AlertDialog alertDialog;
    private Activity activity;

    private static final int ICON_COUNT = 15;

    public StampCalAdapter(Context context, ArrayList<StampCal> stamps, Activity activity) {
        this.context = context;
        this.stamps = stamps;
        stampDBAdapter = new StampDBAdapter(activity);
        this.activity = activity;
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
                else imgIcon[i].setImageResource(R.drawable.empty_crystal);
                layoutTitle.setBackgroundResource(R.drawable.textdeburfgradientstyle);
                imgCrystal.setImageResource(R.drawable.deburf_success);
                txtX.setTextColor(Color.parseColor("#e34c4c"));
                txtCount.setTextColor(Color.parseColor("#e34c4c"));
            } else {
                if (i < stamps.get(position).getCnt()) imgIcon[i].setImageResource(R.drawable.success);
                else imgIcon[i].setImageResource(R.drawable.empty_crystal);
                layoutTitle.setBackgroundResource(R.drawable.textgradientstyle);
                imgCrystal.setImageResource(R.drawable.success);
                txtX.setTextColor(Color.parseColor("#698BDD"));
                txtCount.setTextColor(Color.parseColor("#698BDD"));
            }
        }

        imgStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getLayoutInflater().inflate(R.layout.stamp_preview_dialog, null);

                ImageView imgStamp = view.findViewById(R.id.imgStamp);
                TextView txtName = view.findViewById(R.id.txtName);
                TextView txtCount = view.findViewById(R.id.txtCount);
                LinearLayout layoutOver = view.findViewById(R.id.layoutOver);
                TextView txtOver = view.findViewById(R.id.txtOver);
                TextView[] txtLevelInfo = new TextView[3];
                TextView[] txtLevel = new TextView[3];
                for (int i = 0; i < txtLevel.length; i++) {
                    txtLevelInfo[i] = view.findViewById(activity.getResources().getIdentifier("txtLevelInfo"+(i+1), "id", activity.getPackageName()));
                    txtLevel[i] = view.findViewById(activity.getResources().getIdentifier("txtLevel"+(i+1), "id", activity.getPackageName()));
                }

                String name = stamps.get(position).getName();
                int count = stamps.get(position).getCnt();
                StampDBAdapter stampDBAdapter = new StampDBAdapter(activity);
                String[] args = stampDBAdapter.readData(name);

                txtName.setText(name);
                txtCount.setText(Integer.toString(count));
                if (count > 15) {
                    layoutOver.setVisibility(View.VISIBLE);
                    txtOver.setText(Integer.toString(count-15));
                } else {
                    layoutOver.setVisibility(View.GONE);
                }
                imgStamp.setImageResource(activity.getResources().getIdentifier(args[1], "drawable", activity.getPackageName()));
                for (int i = 0; i < txtLevel.length; i++) {
                    txtLevel[i].setText(args[3+i]);
                }
                if (count >= 0 && count < 5) { // Level 0
                    for (int i = 0; i < txtLevel.length; i++) {
                        txtLevelInfo[i].setTextColor(Color.parseColor("#444444"));
                        txtLevel[i].setTextColor(Color.parseColor("#666666"));
                    }
                } else if (count >= 5 && count < 10) { // Level 1
                    for (int i = 0; i < txtLevel.length; i++) {
                        if (i == 0) {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#AAAAAA"));
                            txtLevel[i].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#444444"));
                            txtLevel[i].setTextColor(Color.parseColor("#666666"));
                        }
                    }
                } else if (count >= 10 && count < 15) { // Level 2
                    for (int i = 0; i < txtLevel.length; i++) {
                        if (i == 1) {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#AAAAAA"));
                            txtLevel[i].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#444444"));
                            txtLevel[i].setTextColor(Color.parseColor("#666666"));
                        }
                    }
                } else { // Level 3
                    for (int i = 0; i < txtLevel.length; i++) {
                        if (i == 2) {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#AAAAAA"));
                            txtLevel[i].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            txtLevelInfo[i].setTextColor(Color.parseColor("#444444"));
                            txtLevel[i].setTextColor(Color.parseColor("#666666"));
                        }
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

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
