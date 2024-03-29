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

    public StampListAdapter(Context context, ArrayList<StampList> stampLists) {
        this.context = context;
        this.stampLists = stampLists;
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
        LinearLayout layoutContent = convertView.findViewById(R.id.layoutContent);
        LinearLayout layoutStamp = convertView.findViewById(R.id.layoutStamp);
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

        if (stampLists.get(position).isOpen()) {
            layoutContent.setVisibility(View.VISIBLE);
        } else {
            layoutContent.setVisibility(View.GONE);
        }

        layoutStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stampLists.get(position).isOpen()) {
                    stampLists.get(position).setOpen(false);
                    layoutContent.setVisibility(View.GONE);
                } else {
                    stampLists.get(position).setOpen(true);
                    layoutContent.setVisibility(View.VISIBLE);
                }
            }
        });

        return convertView;
    }
}
